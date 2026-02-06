# Prof or Flop?

The project is built using [`jax-rs`](https://jakarta.ee/specifications/restful-ws/). As such, it bundles the code into a [WAR](https://en.wikipedia.org/wiki/WAR_(file_format)) file.

A web server which loads `WAR`s is needed (like [Tomcat](https://tomcat.apache.org/)). For local development, you'll need to setup your editor to automatically rebuild and redeploy the `WAR` file to your local installation of Tomcat.
