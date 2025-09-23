package io.aiven.common.strings;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CasedStringTest {

	@MethodSource("testSegmentationData")
	@ParameterizedTest
	void testSegmentation(String pattern, CasedString.StringCase stringCase, String[] expected) {
		CasedString casedString = new CasedString(stringCase, pattern);
		assertThat(casedString.getSegments()).isEqualTo(expected);
	}

	static Stream<Arguments> testSegmentationData() {
		List<Arguments> lst = new ArrayList<>();
		lst.add(Arguments.of("CamelCase", CasedString.StringCase.CAMEL, new String[]{"camel", "Case"}));
		lst.add(Arguments.of("CamelPMDCase", CasedString.StringCase.CAMEL,
				new String[]{"camel", "P", "M", "D", "Case"}));
		lst.add(Arguments.of("camelCase", CasedString.StringCase.CAMEL, new String[]{"camel", "Case"}));
		lst.add(Arguments.of("snake_case", CasedString.StringCase.SNAKE, new String[]{"snake", "case"}));
		lst.add(Arguments.of("snake_Case", CasedString.StringCase.SNAKE, new String[]{"snake", "Case"}));
		lst.add(Arguments.of("snake__Case", CasedString.StringCase.SNAKE, new String[]{"snake", "", "Case"}));
		lst.add(Arguments.of("kebab-case", CasedString.StringCase.KEBAB, new String[]{"kebab", "case"}));
		lst.add(Arguments.of("kebab-Case", CasedString.StringCase.KEBAB, new String[]{"kebab", "Case"}));
		lst.add(Arguments.of("kebab--case", CasedString.StringCase.KEBAB, new String[]{"kebab", "", "case"}));
		lst.add(Arguments.of("phrase case", CasedString.StringCase.PHRASE, new String[]{"phrase", "case"}));
		lst.add(Arguments.of("phrase Case", CasedString.StringCase.PHRASE, new String[]{"phrase", "Case"}));
		lst.add(Arguments.of("phrase  case", CasedString.StringCase.PHRASE, new String[]{"phrase", "", "case"}));
		lst.add(Arguments.of("dot.case", CasedString.StringCase.DOT, new String[]{"dot", "case"}));
		lst.add(Arguments.of("dot..case", CasedString.StringCase.DOT, new String[]{"dot", "", "case"}));
		lst.add(Arguments.of("dot.Case", CasedString.StringCase.DOT, new String[]{"dot", "Case"}));
		return lst.stream();
	}

	@MethodSource("testAssembleSegmentsData")
	@ParameterizedTest
	void testAssembleSegments(CasedString casedString, CasedString.StringCase stringCase, String expected) {
		assertThat(casedString.toCase(stringCase)).isEqualTo(expected);
	}

	static Stream<Arguments> testAssembleSegmentsData() {
		List<Arguments> lst = new ArrayList<>();

		CasedString underTest = new CasedString(CasedString.StringCase.CAMEL, "camelCase");
		lst.add(Arguments.of(underTest, CasedString.StringCase.CAMEL, "camelCase"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.SNAKE, "camel_Case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.KEBAB, "camel-Case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.PHRASE, "camel Case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.DOT, "camel.Case"));

		underTest = new CasedString(CasedString.StringCase.SNAKE, "snake_case");
		lst.add(Arguments.of(underTest, CasedString.StringCase.CAMEL, "snakeCase"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.SNAKE, "snake_case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.KEBAB, "snake-case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.PHRASE, "snake case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.DOT, "snake.case"));

		underTest = new CasedString(CasedString.StringCase.KEBAB, "kebab-case");
		lst.add(Arguments.of(underTest, CasedString.StringCase.CAMEL, "kebabCase"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.SNAKE, "kebab_case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.KEBAB, "kebab-case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.PHRASE, "kebab case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.DOT, "kebab.case"));

		underTest = new CasedString(CasedString.StringCase.PHRASE, "phrase case");
		lst.add(Arguments.of(underTest, CasedString.StringCase.CAMEL, "phraseCase"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.SNAKE, "phrase_case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.KEBAB, "phrase-case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.PHRASE, "phrase case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.DOT, "phrase.case"));

		underTest = new CasedString(CasedString.StringCase.DOT, "dot.case");
		lst.add(Arguments.of(underTest, CasedString.StringCase.CAMEL, "dotCase"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.SNAKE, "dot_case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.KEBAB, "dot-case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.PHRASE, "dot case"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.DOT, "dot.case"));

		underTest = new CasedString(CasedString.StringCase.DOT, "one..two");
		lst.add(Arguments.of(underTest, CasedString.StringCase.CAMEL, "oneTwo"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.SNAKE, "one__two"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.KEBAB, "one--two"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.PHRASE, "one  two"));
		lst.add(Arguments.of(underTest, CasedString.StringCase.DOT, "one..two"));

		return lst.stream();
	}
}
