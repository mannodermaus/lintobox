package com.github.aurae.lintobox;

/**
 * A single incident detected by Lint within a certain file.
 */
public final class ResultSpec {

    static final int TYPE_WARNING = 0;
    static final int TYPE_ERROR = 1;

    final int type;
    private final String sourceFile;
    private final int sourceLine;
    private final String description;
    private final String issueId;

    private final LocationSpec location;

    ResultSpec(int type, String sourceFile, int sourceLine, String description, String issueId, LocationSpec location) {
        this.type = type;
        this.sourceFile = sourceFile;
        this.sourceLine = sourceLine;
        this.description = description;
        this.issueId = issueId;

        // Dissect a custom location
        this.location = location;
    }

    public static ResultSpec.Builder warningBuilder() {
        return new ResultSpec.Builder(TYPE_WARNING);
    }

    public static ResultSpec.Builder errorBuilder() {
        return new ResultSpec.Builder(TYPE_ERROR);
    }

    @Override
    public String toString() {
        // Lint Output format:
        //
        // <file>:<sourceLine>: <type>: <description> [<id>]\n
        //   <location\n
        //           >\n
        StringBuilder sb = new StringBuilder();

        sb.append(sourceFile).append(':').append(sourceLine).append(": ");

        switch(type) {
            case TYPE_ERROR:
                sb.append("Error: ");
                break;

            case TYPE_WARNING:
                sb.append("Warning: ");
                break;
        }

        sb.append(description).append(" [").append(issueId).append("]\n");

        sb.append(location.toString());

        return sb.toString();
    }

    /* Begin inner classes */

    public static final class Builder {

        private final int type;

        private String sourceFile;
        private int sourceLine;
        private String description;
        private String issueId;

        private LocationSpec location;

        Builder(int type) {
            this.type = type;
        }

        public Builder sourceFile(String sourceFile, int sourceLine) {
            this.sourceFile = sourceFile;
            this.sourceLine = sourceLine;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder issueId(String issueId) {
            this.issueId = issueId;
            return this;
        }

        public Builder location(LocationSpec location) {
            this.location = location;
            return this;
        }

        public ResultSpec build() {
            if (sourceFile == null) {
                throw new IllegalStateException("ResultSpec requires sourceFile()");
            }
            if (sourceLine == 0) {
                throw new IllegalStateException("ResultSpec requires sourceLine()");
            }
            if (description == null) {
                throw new IllegalStateException("ResultSpec requires description()");
            }
            if (issueId == null) {
                throw new IllegalStateException("ResultSpec requires issueId()");
            }
            if (location == null) {
                throw new IllegalStateException("ResultSpec requires location()");
            }

            return new ResultSpec(type, sourceFile, sourceLine, description, issueId, location);
        }
    }
}
