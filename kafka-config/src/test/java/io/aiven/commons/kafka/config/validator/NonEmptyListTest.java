package io.aiven.commons.kafka.config.validator;

import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NonEmptyListTest {

	@Test
	void testEnsureValid() {
		assertThatNoException().isThrownBy(() -> NonEmptyList.INSTANCE.ensureValid("null", null));
		assertThatThrownBy(() -> NonEmptyList.INSTANCE.ensureValid("empty list", Collections.emptyList()),
				"empty list value").isInstanceOf(ConfigException.class).hasMessageContaining("A non-empty list");
		assertThatNoException()
				.isThrownBy(() -> NonEmptyList.INSTANCE.ensureValid("good", Collections.singletonList("value")));
	}
}
