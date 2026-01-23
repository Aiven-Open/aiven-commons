package io.aiven.commons.kafka.config;
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
import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtendedConfigKeyBuilderTest {

	private void assertStandardValues(ConfigDef.ConfigKey key, String name) {
		assertThat(key.displayName).isEqualTo(name);
		assertThat(key.group).isNull();
		assertThat(key.alternativeString).isNull();
		assertThat(key.dependents).isEmpty();
		assertThat(key.importance).isEqualTo(ConfigDef.Importance.MEDIUM);
		assertThat(key.internalConfig).isFalse();
		assertThat(key.name).isEqualTo(name);
		assertThat(key.orderInGroup).isEqualTo(-1);
		assertThat(key.recommender).isNull();
		assertThat(key.type).isEqualTo(ConfigDef.Type.STRING);
		assertThat(key.defaultValue).isNull();
		assertThat(key.validator).isNull();
	}

	@Test
	void testDefaults() {
		ExtendedConfigKey key = ExtendedConfigKey.builder("defaults").build();
		assertThat(key.isDeprecated()).isFalse();
		assertThat(key.getSince()).isEmpty();
		assertThat(key.documentation).isEmpty();
		assertStandardValues(key, "defaults");
	}

	@Test
	void testDeprecation() {
		ExtendedConfigKey key = ExtendedConfigKey.builder("deprecation").deprecatedInfo(DeprecatedInfo.builder().get())
				.build();
		assertThat(key.isDeprecated()).isTrue();
		assertThat(key.getSince()).isEmpty();
		assertThat(key.documentation).isEqualTo("deprecation is deprecated. ");
		assertStandardValues(key, "deprecation");
	}

	@Test
	void testSince() {
		SinceInfo sinceInfo = SinceInfo.builder().artifactId("artifactId").groupId("groupId").version("version").build();
		ExtendedConfigKey key = ExtendedConfigKey.builder("since").since(sinceInfo).build();
		assertThat(key.isDeprecated()).isFalse();
		assertThat(key.getSince()).isEqualTo("groupId:artifactId:version");
		assertStandardValues(key, "since");

		SinceInfo.Builder adjustedInfo = SinceInfo.builder().version("whatever");
		key.setSince(adjustedInfo);
		assertThat(key.getSince()).isEqualTo("whatever");
	}
}
