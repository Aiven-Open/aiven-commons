package io.aiven.commons.kafka.config;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtendedConfigKeyBuilderTest {

	private void assertStandardValues(ConfigDef.ConfigKey key, String name) {
		assertThat(key.displayName).isEqualTo(name);
		assertThat(key.documentation).isEmpty();
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
		assertStandardValues(key, "defaults");
	}

	@Test
	void testDeprecation() {
		ExtendedConfigKey key = ExtendedConfigKey.builder("deprecation").deprecatedInfo(DeprecatedInfo.builder().get())
				.build();
		assertThat(key.isDeprecated()).isTrue();
		assertThat(key.getSince()).isEmpty();
		assertStandardValues(key, "deprecation");
	}

	@Test
	void testSince() {
		ExtendedConfigKey key = ExtendedConfigKey.builder("since").since("whenever").build();
		assertThat(key.isDeprecated()).isFalse();
		assertThat(key.getSince()).isEqualTo("whenever");
		assertStandardValues(key, "since");
	}
}
