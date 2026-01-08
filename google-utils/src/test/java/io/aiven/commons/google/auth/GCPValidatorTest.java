package io.aiven.commons.google.auth;
/*
         Copyright 2025 Aiven Oy and project contributors

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
        KIND, either express or implied.  See the License for the
        specific language governing permissions and limitations
        under the License.

        SPDX-License-Identifier: Apache-2
 */
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import io.aiven.commons.system.EnvCheck;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GCPValidatorTest {

	private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

	private GenericJson newKeyFile() {
		final GenericJson keyFile = new GenericJson();
		keyFile.setFactory(jsonFactory);
		return keyFile;
	}

	@Test
	void testTokenUrl() throws IOException {
		final GenericJson keyFile = newKeyFile();
		keyFile.put("token_url", "https://example.com/badURL");
		Throwable throwable = assertThrows(IllegalArgumentException.class,
				() -> GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
		assertEquals("token_url must have the value: 'https://sts.googleapis.com/v1/token'", throwable.getMessage());

		keyFile.clear();
		keyFile.put("token_url", "https://sts.googleapis.com/v1/token");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@Disabled("token URI have different -- unknown restrictions")
	void testTokenUri() throws IOException {
		final GenericJson keyFile = newKeyFile();
		keyFile.put("token_uri", "https://example.com/badURL");
		Throwable throwable = assertThrows(IllegalArgumentException.class,
				() -> GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
		assertEquals("token_uri must have the value: 'https://sts.googleapis.com/v1/token'", throwable.getMessage());

		keyFile.clear();
		keyFile.put("token_uri", "https://sts.googleapis.com/v1/token");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testImpersonationUrl() throws IOException {
		final GenericJson keyFile = newKeyFile();
		keyFile.put("service_account_impersonation_url", "https://example.com/badURL");
		Throwable throwable = assertThrows(IllegalArgumentException.class,
				() -> GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
		assertEquals("'https://example.com/badURL' is not an allowed value for service_account_impersonation_url",
				throwable.getMessage());

		keyFile.clear();
		keyFile.put("service_account_impersonation_url",
				"https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/some stuff here:generateAccessToken");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testCredentialSourceUrl() throws IOException, NoSuchFieldException, IllegalAccessException {
		final GenericJson keyFile = newKeyFile();
		final GenericJson credentialSource = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		keyFile.put("credential_source", credentialSource);

		credentialSource.put("url", "https://example.com/badURL");

		Map<String, String> envVar = EnvCheck.getEnvVars();
		String oldValue = envVar.get(EnvCheck.Type.URI.envVar());
		try {
			envVar.remove(EnvCheck.Type.URI.envVar());
			Throwable throwable = assertThrows(IllegalArgumentException.class, () -> GCPValidator
					.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
			assertEquals(EnvCheck.formatError(EnvCheck.Type.URI, "https://example.com/badURL"), throwable.getMessage());

			envVar.put(EnvCheck.Type.URI.envVar(), "https://example.com/badURL/andSome");
			throwable = assertThrows(IllegalArgumentException.class, () -> GCPValidator
					.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
			assertEquals(EnvCheck.formatError(EnvCheck.Type.URI, "https://example.com/badURL"), throwable.getMessage());

			envVar.put(EnvCheck.Type.URI.envVar(), "https://example.com/badURL");
			GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
		} finally {
			if (oldValue != null) {
				envVar.put(EnvCheck.Type.URI.envVar(), oldValue);
			} else {
				envVar.remove(EnvCheck.Type.URI.envVar());
			}
		}
	}

	@Test
	void testCredentialSourceFile() throws IOException, NoSuchFieldException, IllegalAccessException {
		final GenericJson keyFile = newKeyFile();
		final GenericJson credentialSource = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		keyFile.put("credential_source", credentialSource);

		credentialSource.put("file", "/tmp/example/badFile");

		Map<String, String> envVar = EnvCheck.getEnvVars();
		String oldValue = envVar.get(EnvCheck.Type.FILE.envVar());
		try {
			envVar.remove(EnvCheck.Type.FILE.envVar());
			Throwable throwable = assertThrows(IllegalArgumentException.class, () -> GCPValidator
					.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
			assertEquals(EnvCheck.formatError(EnvCheck.Type.FILE, "/tmp/example/badFile"), throwable.getMessage());

			envVar.put(EnvCheck.Type.FILE.envVar(), "/tmp/example/badFile/andSome");
			throwable = assertThrows(IllegalArgumentException.class, () -> GCPValidator
					.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
			assertEquals(EnvCheck.formatError(EnvCheck.Type.FILE, "/tmp/example/badFile"), throwable.getMessage());

			envVar.put(EnvCheck.Type.FILE.envVar(), "/tmp/example/badFile");
			GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
		} finally {
			if (oldValue != null) {
				envVar.put(EnvCheck.Type.FILE.envVar(), oldValue);
			} else {
				envVar.remove(EnvCheck.Type.FILE.envVar());
			}
		}
	}

	@Test
	void testCredentialSourceCommand() throws IOException, NoSuchFieldException, IllegalAccessException {
		final GenericJson keyFile = newKeyFile();
		final GenericJson credentialSource = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		keyFile.put("credential_source", credentialSource);

		final GenericJson executable = new GenericJson();
		executable.setFactory(jsonFactory);
		credentialSource.put("executable", executable);
		executable.put("command", "/my/badCommand");

		Map<String, String> envVar = EnvCheck.getEnvVars();
		String oldValue = envVar.get(EnvCheck.Type.CMD.envVar());
		try {
			envVar.remove(EnvCheck.Type.CMD.envVar());
			Throwable throwable = assertThrows(IllegalArgumentException.class, () -> GCPValidator
					.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
			assertEquals(EnvCheck.formatError(EnvCheck.Type.CMD, "/my/badCommand"), throwable.getMessage());

			envVar.put(EnvCheck.Type.CMD.envVar(), "/my/badCommand/andSome");
			throwable = assertThrows(IllegalArgumentException.class, () -> GCPValidator
					.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
			assertEquals(EnvCheck.formatError(EnvCheck.Type.CMD, "/my/badCommand"), throwable.getMessage());

			envVar.put(EnvCheck.Type.CMD.envVar(), "/my/badCommand");
			GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
		} finally {
			if (oldValue != null) {
				envVar.put(EnvCheck.Type.CMD.envVar(), oldValue);
			} else {
				envVar.remove(EnvCheck.Type.CMD.envVar());
			}
		}
	}

	@Test
	void testCredentialSourceAwsUrl() throws IOException {
		final GenericJson keyFile = newKeyFile();
		final GenericJson credentialSource = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		keyFile.put("credential_source", credentialSource);
		final GenericJson aws = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		credentialSource.put("aws", aws);

		aws.put("url", "https://example.com/badURL");
		Throwable throwable = assertThrows(IllegalArgumentException.class,
				() -> GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
		assertEquals(
				"credential_source.aws.url must be one of 'https://169.254.169.254/latest/meta-data/iam/security-credentials', 'https://[fd00:ec2::254]/latest/meta-data/iam/security-credentials'",
				throwable.getMessage());

		aws.put("url", "https://169.254.169.254/latest/meta-data/iam/security-credentials");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));

		aws.put("url", "https://[fd00:ec2::254]/latest/meta-data/iam/security-credentials");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testCredentialSourceAwsRegionUrl() throws IOException {
		final GenericJson keyFile = newKeyFile();
		final GenericJson credentialSource = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		keyFile.put("credential_source", credentialSource);

		final GenericJson aws = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		credentialSource.put("aws", aws);

		aws.put("region_url", "https://example.com/badURL");
		Throwable throwable = assertThrows(IllegalArgumentException.class,
				() -> GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
		assertEquals(
				"credential_source.aws.region_url must be one of 'http://169.254.169.254/latest/meta-data/placement/availability-zone', 'http://[fd00:ec2::254]/latest/meta-data/placement/availability-zone'",
				throwable.getMessage());

		aws.put("region_url", "http://169.254.169.254/latest/meta-data/placement/availability-zone");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));

		aws.put("region_url", "http://[fd00:ec2::254]/latest/meta-data/placement/availability-zone");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testCredentialSourceAwsSessionToken() throws IOException {
		final GenericJson keyFile = newKeyFile();
		final GenericJson credentialSource = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		keyFile.put("credential_source", credentialSource);

		final GenericJson aws = new GenericJson();
		credentialSource.setFactory(jsonFactory);
		credentialSource.put("aws", aws);

		aws.put("imdsv2_session_token_url", "https://example.com/badURL");
		Throwable throwable = assertThrows(IllegalArgumentException.class,
				() -> GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8)));
		assertEquals(
				"credential_source.aws.imdsv2_session_token_url must be one of 'http://169.254.169.254/latest/api/token', 'http://[fd00:ec2::254]/latest/api/token'",
				throwable.getMessage());

		aws.put("imdsv2_session_token_url", "http://169.254.169.254/latest/api/token");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));

		aws.put("imdsv2_session_token_url", "http://[fd00:ec2::254]/latest/api/token");
		GCPValidator.validateCredentialJson(keyFile.toPrettyString().getBytes(StandardCharsets.UTF_8));
	}
}
