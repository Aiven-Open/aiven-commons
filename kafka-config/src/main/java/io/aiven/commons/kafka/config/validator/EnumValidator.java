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
package io.aiven.commons.kafka.config.validator;

import org.apache.kafka.common.config.ConfigDef;

import java.util.Arrays;

/**
 * Validates that input is the name of an Enumeration value. Will accept
 * enumeration class or exemplar. Test is not case specific.
 */
public class EnumValidator {

	/**
	 * Create a validator from an Enum class. {@code enum X {ONE, TWO, THREE};
	 *
	 * EnumValidator.of(X.class); }
	 * 
	 * @param enumClass
	 *            the class of the enum
	 * @return the validator for the enum names
	 */
	public static ConfigDef.Validator of(Class<? extends Enum<?>> enumClass) {
		return ConfigDef.CaseInsensitiveValidString
				.in(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new));
	}

	/**
	 * Create a validator from an Enum exemplar. {@code enum X {ONE, TWO, THREE};
	 *
	 * EnumValidator.of(X.ONE); }
	 * 
	 * @param exemplar
	 *            An example of the Enum.
	 * @return the validator for the enum names.
	 */
	public static ConfigDef.Validator of(Enum<?> exemplar) {
		return ConfigDef.CaseInsensitiveValidString
				.in(Arrays.stream(exemplar.getClass().getEnumConstants()).map(Enum::name).toArray(String[]::new));
	}
}
