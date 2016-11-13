package com.github.aurae.lintobox;

import java.util.ArrayList;
import java.util.List;

/**
 * An entire message output body by Lint.
 */
public final class LintSpec {

    private static final LintSpec NO_WARNINGS = LintSpec.builder().build();

    private final List<ResultSpec> results;

    LintSpec(List<ResultSpec> results) {
        this.results = results;
    }

    public static LintSpec.Builder builder() {
        return new Builder();
    }

    public static LintSpec noWarnings() {
        return NO_WARNINGS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (results.isEmpty()) {
            return "No warnings.";
        }

        int errorCount = 0;
        int warningCount = 0;
        for (ResultSpec result : results) {
            sb.append(result.toString());
            switch (result.type) {
                case ResultSpec.TYPE_ERROR:
                    errorCount++;
                    break;

                case ResultSpec.TYPE_WARNING:
                    warningCount++;
                    break;
            }
        }
        sb.append(errorCount).append(" errors, ").append(warningCount).append(" warnings");

        return sb.toString();
    }

    /* Begin inner classes */

    public static final class Builder {

        private final List<ResultSpec> results = new ArrayList<>();

        Builder() {
        }

        public Builder add(ResultSpec resultSpec) {
            this.results.add(resultSpec);
            return this;
        }

        public LintSpec build() {
            return new LintSpec(results);
        }
    }
}
