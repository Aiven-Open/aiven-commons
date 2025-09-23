package io.aiven.commons.kafka.config.validator;

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
