package com.github.aurae.lintobox;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LintSpecTest {

    @Test
    public void noWarnings() {
        assertThat(LintSpec.noWarnings().toString())
                .isEqualTo("No warnings.");
    }

    @Test
    public void noErrorsButAWarning() {
        LintSpec lint = LintSpec.builder()
                .add(ResultSpec.warningBuilder()
                        .sourceFile("File.java", 123)
                        .description("Custom Lint Warning")
                        .issueId("CustomLintWarning")
                        .location(LocationSpec.builder()
                                .sourceLine("int test;")
                                .build())
                        .build())
                .build();

        assertThat(lint.toString())
                .endsWith("0 errors, 1 warnings\n");
    }

    @Test
    public void noWarningsButAnError() {
        LintSpec lint = LintSpec.builder()
                .add(ResultSpec.errorBuilder()
                        .sourceFile("File.java", 123)
                        .description("Custom Lint Warning")
                        .issueId("CustomLintWarning")
                        .location(LocationSpec.builder()
                                .sourceLine("int test;")
                                .build())
                        .build())
                .build();

        assertThat(lint.toString())
                .endsWith("1 errors, 0 warnings\n");
    }
}
