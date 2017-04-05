package com.github.aurae.lintobox;

import java.util.regex.Pattern;

/**
 * The details of the location in which a certain incident was detected by Lint.
 */
public class LocationSpec {

    private static final char LINT_HIGHLIGHT_CHAR = '~';
    private static final String REGEX_NOT_HIGHLIGHT = "[^" + LINT_HIGHLIGHT_CHAR + "]";

    private final String text;
    private final String underline;

    LocationSpec(String text, String error, int errorNum) {
        this.text = text;
        this.underline = this.createUnderline(text, error, errorNum);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return text + "\n" +
               underline + "\n";
    }

    /* Begin private */

    private String createUnderline(String text, String highlight, int instance) {
        // Create "squiggly sourceLine" with the same length as the erroneous sub-text
        int highlightLen = highlight.length();
        String squigglyLine = new String(new char[highlightLen]).replace('\0', LINT_HIGHLIGHT_CHAR);

        // Verify that there are as many instances of the sub-text as advertised
        if (instance > 1) {
            String whitespaces = new String(new char[highlightLen]).replace('\0', ' ');
            for (int i = 1; i < instance; i++) {
                int index = text.indexOf(highlight);
                text = text.replaceFirst(Pattern.quote(text.substring(0, index + highlightLen)), whitespaces);
            }
        }

        // Apply some more sourceLine manipulation and return the underline
        return text
                .replaceFirst(Pattern.quote(highlight), squigglyLine)
                .replaceAll(REGEX_NOT_HIGHLIGHT, " ")
                // Trim trailing whitespace
                .replaceAll("\\s+$", "");
    }

    /* Begin inner classes */

    public static final class Builder {

        private String text;
        private String error;
        private int errorNum = 1;

        Builder() {
        }

        public Builder sourceLine(String text) {
            this.text = text;
            return this;
        }

        public Builder sourceError(String error) {
            return sourceError(error, 1);
        }

        public Builder sourceError(String error, int errorNum) {
            this.error = error;
            this.errorNum = errorNum;
            return this;
        }

        public LocationSpec build() {
            if (text == null) {
                throw new IllegalStateException("LocationSpec requires sourceLine()");
            }
            if (error == null) {
                error = text;
            }
            if (!text.contains(error)) {
                throw new IllegalStateException("LocationSpec sourceError() must be a substring of its sourceLine()");
            }
            if (errorNum < 1) {
                throw new IllegalStateException("LocationSpec sourceError number must be greater than zero");
            }
            // Highlighted sub-sourceLine must occur at least "errorNum" times
            String cutText = text;
            for (int i = 0; i < errorNum; i++) {
                int index = cutText.indexOf(error);
                if (index == -1) {
                    throw new IllegalStateException("LocationSpec sourceError() isn't contained as many times as advertised");
                }
                cutText = cutText.substring(index + error.length());
            }

            return new LocationSpec(text, error, errorNum);
        }
    }
}
