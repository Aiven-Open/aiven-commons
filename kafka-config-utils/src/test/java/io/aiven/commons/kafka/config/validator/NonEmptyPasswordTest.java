package io.aiven.commons.kafka.config.validator;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.types.Password;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NonEmptyPasswordTest {

	@Test
	void testEnsureValid() {
		assertThatNoException().isThrownBy(() -> NonEmptyPassword.INSTANCE.ensureValid("null", null));
		assertThatThrownBy(() -> NonEmptyPassword.INSTANCE.ensureValid("null value", new Password(null)),
				"null password value").isInstanceOf(ConfigException.class)
				.hasMessageContaining("Password must be non-empty");
		assertThatThrownBy(() -> NonEmptyPassword.INSTANCE.ensureValid("empty value", new Password("")),
				"empty password value").isInstanceOf(ConfigException.class)
				.hasMessageContaining("Password must be non-empty");
		assertThatNoException()
				.isThrownBy(() -> NonEmptyPassword.INSTANCE.ensureValid("good", new Password("p@ssw0rd")));
	}
}
