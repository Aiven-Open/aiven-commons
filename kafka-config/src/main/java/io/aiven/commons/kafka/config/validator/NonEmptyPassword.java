package io.aiven.commons.kafka.config.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.types.Password;

import java.util.Objects;

/**
 * Validator to ensure that a password is set to either @{code null} or a
 * non-empty string.
 */
public class NonEmptyPassword implements ConfigDef.Validator {

	/**
	 * The instance of the NonEmptyPassword validator.
	 */
	public static NonEmptyPassword INSTANCE = new NonEmptyPassword();

	private NonEmptyPassword() {
	}

	@Override
	public void ensureValid(final String name, final Object value) {
		if (Objects.isNull(value)) {
			return;
		}
		final var pwd = (Password) value;
		if (pwd.value() == null || pwd.value().isBlank()) {
			throw new ConfigException(name, pwd, "Password must be non-empty");
		}
	}

	@Override
	public String toString() {
		return "A password string.  May be null.  May not be an empty string.";
	}
}