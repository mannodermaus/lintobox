package com.github.aurae.lintobox;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class LocationSpecTest {

    @Test
    public void highlightsEntireLineWhenHighlightNotSpecified() {
        LocationSpec location = LocationSpec.builder()
                .sourceLine("Object object = String.valueOf(123);")
                .build();

        assertThat(location.toString())
                .isEqualTo(
                        // @formatter:off
                        "  Object object = String.valueOf(123);\n" +
                        "  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                        // @formatter:on
                );
    }

    @Test
    public void highlightsOnlySectionOfLocationIfSpecifiedUniquely() {
        LocationSpec location = LocationSpec.builder()
                .sourceLine("Object object = String.valueOf(123);")
                .sourceError("String.valueOf(123)")
                .build();

        assertThat(location.toString())
                .isEqualTo(
                        // @formatter:off
                        "  Object object = String.valueOf(123);\n" +
                        "                  ~~~~~~~~~~~~~~~~~~~\n"
                        // @formatter:on
                );
    }

    @Test
    public void highlightsFirstOccurrenceByDefaultIfNotSpecified() {
        LocationSpec location = LocationSpec.builder()
                .sourceLine("InternalClass object = new InternalClass();")
                .sourceError("InternalClass")
                .build();

        assertThat(location.toString())
                .isEqualTo(
                        // @formatter:off
                        "  InternalClass object = new InternalClass();\n" +
                        "  ~~~~~~~~~~~~~\n"
                        // @formatter:on
                );
    }

    @Test
    public void highlightsNthOccurrenceIfSpecified() {
        LocationSpec location = LocationSpec.builder()
                .sourceLine("InternalClass object = new InternalClass();")
                .sourceError("InternalClass", 2)
                .build();

        assertThat(location.toString())
                .isEqualTo(
                        // @formatter:off
                        "  InternalClass object = new InternalClass();\n" +
                        "                             ~~~~~~~~~~~~~\n"
                        // @formatter:on
                );
    }

    @Test public void throwsExceptionIfHighlightIsntSubstringOfWholeText() {
        try {
            LocationSpec.builder()
                    .sourceLine("Object object = String.valueOf(123);")
                    .sourceError("throw new RuntimeException();")
                    .build();

            failBecauseExceptionWasNotThrown(IllegalStateException.class);

        } catch (IllegalStateException expected) {
        }
    }

    @Test public void throwsExceptionIfHighlightIsntContainedAsOftenAsAdvertised() {
        try {
            LocationSpec.builder()
                    .sourceLine("Object object = String.valueOf(123);")
                    .sourceError("String.valueOf(123);", 2)
                    .build();

            failBecauseExceptionWasNotThrown(IllegalStateException.class);

        } catch (IllegalStateException expected) {
        }
    }

    @Test public void throwsExceptionIfHighlightInstanceLessThan1() {
        try {
            LocationSpec.builder()
                    .sourceLine("Object object = String.valueOf(123);")
                    .sourceError("String.valueOf(123);", 0)
                    .build();

            failBecauseExceptionWasNotThrown(IllegalStateException.class);

        } catch (IllegalStateException expected) {
        }
    }
}
