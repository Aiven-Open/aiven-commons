package io.aiven.commons.kafka.config;
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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeprecatedInfoTest {

	@Test
	void testEmpty() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated");
	}

	@Test
	void testForRemoval() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setForRemoval(true).get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated for removal");
		underTest = DeprecatedInfo.builder().setForRemoval(false).get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated");
	}

	@Test
	void testSince() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setSince("whenever").get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated since whenever");
	}

	@Test
	void testDescription() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setDescription("why not").get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated: why not");
	}

	@Test
	void testForRemovalSince() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setSince("whenever").setForRemoval(true).get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated for removal since whenever");
	}

	@Test
	void testForRemovalDescription() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setDescription("why not").setForRemoval(true).get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated for removal: why not");
	}

	@Test
	void testSinceDescription() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setDescription("why not").setSince("whenever").get();
		assertThat(underTest.formatted("emptyTest")).isEqualTo("emptyTest is deprecated since whenever: why not");
	}

	@Test
	void testSinceDescriptionForRemoval() {
		DeprecatedInfo underTest = DeprecatedInfo.builder().setDescription("why not").setSince("whenever")
				.setForRemoval(true).get();
		assertThat(underTest.formatted("emptyTest"))
				.isEqualTo("emptyTest is deprecated for removal since whenever: why not");
	}
}