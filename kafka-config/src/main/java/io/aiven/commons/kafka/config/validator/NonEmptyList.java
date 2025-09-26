package io.aiven.commons.kafka.config.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.util.List;
import java.util.Objects;

/**
 * Validator to ensure that a list is either @{code null} or non-empty.
 */
public class NonEmptyList implements ConfigDef.Validator {

	/**
	 * The instance of the NonEmptyPassword validator.
	 */
	public static NonEmptyList INSTANCE = new NonEmptyList();

	private NonEmptyList() {
	}

	@Override
	public void ensureValid(final String name, final Object value) {
		if (Objects.isNull(value)) {
			return;
		}
		if (((List<?>) value).isEmpty()) {
			throw new ConfigException(name, "A non-empty list");
		}
	}

	@Override
	public String toString() {
		return "A non-empty list";
	}
}