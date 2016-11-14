package com.github.aurae.lintobox;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ResultSpecTest {

    @Test
    public void throwExceptionIfSourceFileMissing() {
        try {
            ResultSpec.warningBuilder()
                    //.sourceFile("File.java", 123)
                    .description("Custom Lint Warning")
                    .issueId("CustomLintWarning")
                    .build();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);

        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void throwExceptionIfDescriptionMissing() {
        try {
            ResultSpec.warningBuilder()
                    .sourceFile("File.java", 123)
                    //.description("Custom Lint Warning")
                    .issueId("CustomLintWarning")
                    .build();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);

        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void throwExceptionIfIssueIdMissing() {
        try {
            ResultSpec.warningBuilder()
                    .sourceFile("File.java", 123)
                    .description("Custom Lint Warning")
                    //.issueId("CustomLintWarning")
                    .build();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);

        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void correctFormatOfWarningWithoutLocation() {
        ResultSpec spec = ResultSpec.warningBuilder()
                .sourceFile("File.java", 123)
                .description("Custom Lint Warning")
                .issueId("CustomLintWarning")
                .location(LocationSpec.builder()
                        .sourceLine("int test;")
                        .build())
                .build();

        String expected = "File.java:123: Warning: Custom Lint Warning [CustomLintWarning]\n" +
                "  int test;\n" +
                "  ~~~~~~~~~\n";

        assertThat(spec.toString())
                .isEqualTo(expected);
    }

    @Test
    public void correctErrorFormat() {
        ResultSpec spec = ResultSpec.errorBuilder()
                .sourceFile("File.java", 123)
                .description("Custom Lint Error")
                .issueId("CustomLintError")
                .location(LocationSpec.builder()
                        .sourceLine("int test;")
                        .build())
                .build();

        assertThat(spec.toString())
                .startsWith("File.java:123: Error: Custom Lint Error [CustomLintError]\n");
    }

    @Test
    public void correctFormatWithLocation() {
        ResultSpec spec = ResultSpec.errorBuilder()
                .sourceFile("File.java", 123)
                .description("Custom Lint Error")
                .issueId("CustomLintError")
                .location(LocationSpec.builder()
                        .sourceLine("Object object = new RuntimeException();")
                        .sourceError("new RuntimeException()")
                        .build())
                .build();

        assertThat(spec.toString())
                .startsWith("File.java:123: Error: Custom Lint Error [CustomLintError]\n");
    }
}
