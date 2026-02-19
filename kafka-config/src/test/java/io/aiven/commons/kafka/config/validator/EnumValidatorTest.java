/*
        Copyright 2026 Aiven Oy and project contributors

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
        KIND, either express or implied.  See the License for the
        specific language governing permissions and limitations
        under the License.

        SPDX-License-Identifier: Apache-2
 */
package io.aiven.commons.kafka.config.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EnumValidatorTest {

	enum X {
		ONE, TWO, THREE, one, One, four
	}

	private void ensureValidEnumX(ConfigDef.Validator validator) {
		assertThatNoException().isThrownBy(() -> validator.ensureValid("config.name", "ONE"));
		assertThatNoException().isThrownBy(() -> validator.ensureValid("config.name", "two"));
		assertThatNoException().isThrownBy(() -> validator.ensureValid("config.name", "Three"));
		assertThatThrownBy(() -> validator.ensureValid("config.name", "five")).isInstanceOf(ConfigException.class)
				.hasMessageStartingWith(
						"Invalid value five for configuration config.name: String must be one of (case insensitive):");
	}

	@Test
	void testEnsureValidByClass() {
		ensureValidEnumX(EnumValidator.of(X.class));
	}

	@ParameterizedTest
	@EnumSource(X.class)
	void testEnsureValidByConstant(X constant) {
		ensureValidEnumX(EnumValidator.of(constant));
	}

}
