# Semantic versioning

Semantic versioning are providing strict guidelines regarding version numbers.

Given a version number `MAJOR.MINOR.PATCH`, increment the:

* `MAJOR` version when you make incompatible API changes.
* `MINOR` version when you add/change functionality which is backward compatible.
* `PATCH` version when you make backward compatible bug fixes.

The numbers that are used for `MAJOR`, `MINOR`, and `PATCH` are non negative integers, and don't contain leading zeroes. A `MAJOR` version 0 is for initial development and anything may change at any time. Version `1.0.0` defines the first public stable version.

A pre-release version may be denoted by appending a hyphen and a series of dot separated identifiers immediately following the `PATCH` number. The pre-release version identifier must comprise only ASCII alphanumerics ([0-9A-Za-z]). A pre-release version indicates that the version is unstable and might not satisfy the intended compatibility requirements as denoted by its associated normal version. An example of a pre-release version is `1.0.0-alpha`.

Build metadata may be denoted by appending a plus sign and a series of dot separated identifiers immediately following the `PATCH` or pre-release version number. The build metadata must comprise only ASCII alphanumerics ([0-9A-Za-z]). An example of a version number with build metadata is `1.0.0+201506221617`

Once a versioned package is released, the content of that package MUST NOT be modified. Any modifications MUST be released as a new version.

## See also

* [Semantic versioning](http://semver.org/)
* [Best practices for Artifact versioning in Service Oriented Systems](http://resources.sei.cmu.edu/asset_files/TechnicalNote/2012_004_001_15356.pdf)
* http://www.sitepoint.com/semantic-versioning-why-you-should-using/