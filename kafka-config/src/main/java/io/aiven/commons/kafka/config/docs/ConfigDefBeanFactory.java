/*
         Copyright 2026 Aiven Oy and project contributors

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
package io.aiven.commons.kafka.config.docs;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;

import java.lang.reflect.InvocationTargetException;

@DefaultKey("ConfigDefFactory")
@ValidScope({"application"})
public class ConfigDefBeanFactory {
	/**
	 * Constructs the ConfigDefBean for the named class that will return
	 * ExtendedConfigKeyBeans.
	 * 
	 * @param className
	 *            the class to construct the bean for.
	 * @return the ConfigDefBean for velocity usage.
	 */
	public ConfigDefBean<ExtendedConfigKeyBean> open(String className) {
		try {
			Class<? extends ConfigDef> clazz = (Class<? extends ConfigDef>) Class.forName(className);
			ConfigDef configDef = clazz.getConstructor().newInstance();
			return new ConfigDefBean<>(configDef, ExtendedConfigKeyBean::new);
		} catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException
				| NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
