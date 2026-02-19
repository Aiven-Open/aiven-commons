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

import java.util.Arrays;

/**
 * Validates that a configuration value string is a case-insensitive match to an
 * Enum constant.
 *
 * Given an enum:
 * 
 * <pre>
 * enum X {
 * 	ONE, TWO, THREE
 * };
 * </pre>
 *
 * You can create a validator from the Enum class:
 * 
 * <pre>
 * var validator = EnumValidator.of(X.class);
 * validator.ensureValid("config.name", "THREE"); // passes
 * validator.ensureValid("config.name", "Three"); // passes
 * validator.ensureValid("config.name", "four"); // throws ConfigException
 * </pre>
 *
 * or from any one Enum constant:
 * 
 * <pre>
 * // This is identical to above, no matter which Enum constant is passed in.
 * var validator = EnumValidator.of(X.ONE);
 * validator.ensureValid("config.name", "Three"); // passes
 * validator.ensureValid("config.name", "four"); // throws ConfigException
 * </pre>
 */
public class EnumValidator {

	/**
	 * @param enumClass
	 *            the class of the Enum that defines the valid values.
	 * @return a validator that allows any string value that is a case-insensitive
	 *         match for the enum constants in the given Enum class
	 */
	public static ConfigDef.Validator of(Class<? extends Enum<?>> enumClass) {
		return ConfigDef.CaseInsensitiveValidString
				.in(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new));
	}

	/**
	 * @param enumConstant
	 *            one of the Enum constants from the Enum class that defines the
	 *            valid values.
	 * @return a validator that allows any string value that is a case-insensitive
	 *         match for any enum constants in that Enum class
	 */
	public static ConfigDef.Validator of(Enum<?> enumConstant) {
		return ConfigDef.CaseInsensitiveValidString
				.in(Arrays.stream(enumConstant.getClass().getEnumConstants()).map(Enum::name).toArray(String[]::new));
	}
}
