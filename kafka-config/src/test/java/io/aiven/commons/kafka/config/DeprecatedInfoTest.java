package io.aiven.commons.kafka.config;

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