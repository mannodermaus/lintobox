# lintobox

A delicious and lightweight toolkit for Lint Detector assertions.

## Download

No `jcenter()` yet; sorry!

```gradle
buildscript {
    repositories {
        maven { url "https://dl.bintray.com/aurae/maven" }
    }
}

testCompile "de.mannodermaus.lint:lintobox:0.1.0"
```

## Usage

*Lintobox* provides you with three classes that, when used together, will produce nicely formatted output as expected by Lint, which can be used for assertions of your custom Lint detectors or checks in unit tests.

```java
// In your LintDetectorTest...

public void testSomeIssue() {
    String file = "MyLintTestCase.java";

    LintSpec expected = LintSpec.builder()
            .add(ResultSpec.errorBuilder()
                    .sourceFile(file, 10)
                    .description("Lint is great, don't you dare say otherwise")
                    .issueId("WithdrawnAppraisal")
                    .location(LocationSpec.builder()
                            .sourceLine("boolean lintIsGreat = false;")
                            .sourceError("false")
                            .build())
                    .build())
            .build();

    assertThat(lintFiles(file))
            .isEqualTo(expected.toString());
}
```

In the above example, `LintSpec#toString()` yields the following string:

```
MyLintTestCase.java:10: Error: Lint is great, don't you dare say otherwise [WithdrawnAppraisal]\n" +
                        "  boolean lintIsGreat = false;\n" +
                        "                        ~~~~~\n" +
                        "1 errors, 0 warnings
```

## API

### LintSpec

The `LintSpec` class is the entry point to construct a set of Lint outputs. First, obtain a builder with `LintSpec.builder()`. You can then configure this further using `LintSpec.Builder#add(ResultSpec)`. Once configured, call `build()` to obtain the spec object. Whenever you need the actual Lint output, call `LintSpec#toString()` in your assertion.

If no results are added to the builder, you create the no-error output. Alternatively, you can just use `LintSpec.noWarnings()` as a shorthand function:

```java
LintSpec spec = LintSpec.noWarnings();
System.out.println(spec.toString()); // Prints "No warnings."
```

### ResultSpec

A `ResultSpec` represents a single issue within Lint's output, divided into the two main categories of "warning" and "error". Each kind of result is built using `ResultSpec.warningBuilder()` and `ResultSpec.errorBuilder()` respectively.

You are required to specify the location of the expected incident, alongside its description:

* `ResultSpec.Builder#sourceFile(String, int)`: File name and line number
* `ResultSpec.Builder#description(String)`: Description text of the expected issue
* `ResultSpec.Builder#issueId(String)`: ID of the expected issue
* `ResultSpec.Builder#location(LocationSpec)`: Location of this item, i.e. the line in the source code that triggers this issue

### LocationSpec

A `LocationSpec` is attached to each result, and contains the information which highlights the actual piece of code in which the Lint issue occurred. Again, obtain an instance of its `Builder` and configure it:

* `LocationSpec.Builder#sourceLine(String)`: The entire LOC that triggers the issue

Optionally, you can provide the line's segment that actually triggered Lint. This call can be omitted, in which case Lintobox will render the entire line as the culprit, using Lint's squiggly line markers ("~~~~~~~~~~"):

* `LocationSpec.Builder#sourceError(String)`: A segment of the argument passed into `sourceLine(String)`, representing the scope of Lint's error marker
* `LocationSpec.Builder#sourceError(String, int)`: In case the error marker's content appears multiple times in the LOC, the integer argument specifies the occurrence to highlight, with `1` being the first one, which is the default

The latter variant of `sourceError(String, int)` allows you to differentiate occurrences of the same sub-text within a given line.

```java
LocationSpec l1 = LocationSpec.builder()
    .sourceLine("InternalClass ic = new InternalClass();")
    .sourceError("InternalClass") // Identical to .sourceError("InternalClass", 1)
    .build();
assertThat(l1.toString())
    .isEqualTo(
        "  InternalClass ic = new InternalClass();\n" +
        "  ~~~~~~~~~~~~~\n"
    );

LocationSpec l2 = LocationSpec.builder()
    .sourceLine("InternalClass ic = new InternalClass();")
    .sourceError("InternalClass", 2)
    .build();
assertThat(l2.toString())
    .isEqualTo(
        "  InternalClass ic = new InternalClass();\n" +
        "                         ~~~~~~~~~~~~~\n"
    );
```

## Limitations

I designed this library during my ventures with custom Lint rules that apply to Java files, which is why the current API is catering to this file type only at the moment. It might just work for other file types out-of-the-box, but I won't give any guarantee for that. If your particular use case can't be represented by the current Lintobox API, e.g. for an XML-based detector, let me know by opening an Issue!

## License

	Copyright 2016 Marcel Schnelle

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
