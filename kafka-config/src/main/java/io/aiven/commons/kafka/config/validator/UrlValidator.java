package io.aiven.commons.kafka.config.validator;
/*
         Copyright 2020-2025 Aiven Oy and project contributors

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
import java.net.URI;
import java.util.Objects;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

/**
 * Validates that the string is a valid URL.
 */
public class UrlValidator implements ConfigDef.Validator {

	private final boolean defaultHttps;

	/**
	 * Constructor.
	 * 
	 * @param defaultHttps
	 *            if {@code true} the string "https://" will be prepended to the
	 *            argument if no
	 */
	public UrlValidator(boolean defaultHttps) {
		this.defaultHttps = defaultHttps;
	}

	@Override
	public void ensureValid(final String name, final Object value) {
		if (Objects.nonNull(value)) {
			var valueStr = value.toString();

			if (valueStr.isBlank()) {
				throw new ConfigException(name, value, "must be non-empty");
			}

			if (defaultHttps && !valueStr.contains("://")) {
				valueStr = "https://" + valueStr;
			}

			try {
				new URI(valueStr).toURL();
			} catch (final Exception e) {
				throw new ConfigException(name, value, "should be valid URL");
			}
		}
	}

	@Override
	public String toString() {
		return "A valid URL." + (defaultHttps ? "  Will default to https protocol if not otherwise specified." : "");
	}
}
