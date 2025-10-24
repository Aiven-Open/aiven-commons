package io.aiven.commons.kafka.config.validator;
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
import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UrlTest {

	@Test
	void testUrlValidatorWithDefaultHttps() {
		UrlValidator validator = new UrlValidator(true);

		assertThatNoException().isThrownBy(() -> validator.ensureValid("null", null));
		assertThatNoException().isThrownBy(() -> validator.ensureValid("valid", "ftp://example.com"));
		assertThatNoException().isThrownBy(() -> validator.ensureValid("no protocol", "example.com"));
		assertThatThrownBy(() -> validator.ensureValid("invalid", "not://example.com"), "invalid URL")
				.isInstanceOf(ConfigException.class).hasMessageContaining("should be valid URL");
		assertThatThrownBy(() -> validator.ensureValid("empty", ""), "empty URL").isInstanceOf(ConfigException.class)
				.hasMessageContaining("must be non-empty");
	}

	@Test
	void testUrlValidatorWithoutDefaultHttps() {
		UrlValidator validator = new UrlValidator(false);

		assertThatNoException().isThrownBy(() -> validator.ensureValid("null", null));
		assertThatNoException().isThrownBy(() -> validator.ensureValid("valid", "ftp://example.com"));
		assertThatThrownBy(() -> validator.ensureValid("no protocol", "example.com"), "invalid URL")
				.isInstanceOf(ConfigException.class).hasMessageContaining("should be valid URL");
		assertThatThrownBy(() -> validator.ensureValid("invalid", "not://example.com"), "invalid URL")
				.isInstanceOf(ConfigException.class).hasMessageContaining("should be valid URL");
		assertThatThrownBy(() -> validator.ensureValid("empty", ""), "empty URL").isInstanceOf(ConfigException.class)
				.hasMessageContaining("must be non-empty");
	}

	@Test
	void testToString() {
		String msg = new UrlValidator(true).toString();
		assertThat(msg).contains("A valid URL");
		assertThat(msg).contains("default to https protocol ");

		msg = new UrlValidator(false).toString();
		assertThat(msg).contains("A valid URL");
		assertThat(msg).doesNotContain("default to https protocol ");
	}
}
