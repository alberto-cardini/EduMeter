# EduMeter

The project is built using [`jax-rs`](https://jakarta.ee/specifications/restful-ws/). As such, it bundles the code into a [WAR](https://en.wikipedia.org/wiki/WAR_(file_format)) file.

A web server which loads `WAR`s is needed (like [Tomcat](https://tomcat.apache.org/)). For local development, you'll need to setup your editor to automatically rebuild and redeploy the `WAR` file to your local installation of Tomcat.

## Running tests

Tests live under [`src/test`](./src/test) and they follow the same package structure as [`src/java`](./src/java).
That keeps the structure coherent and enables testing of package-private fields and methods.

Unit tests are run by JUnit. They can be individually ran (per method or per class) directly from the IDE. A purposefully made run configuration may also run all unit tests at once (which is included in the repo).