# EduMeter

The project is built using [`jax-rs`](https://jakarta.ee/specifications/restful-ws/). As such, it bundles the code into a [WAR](https://en.wikipedia.org/wiki/WAR_(file_format)) file.

A web server which loads `WAR`s is needed (like [Tomcat](https://tomcat.apache.org/)). For local development, you'll need to setup your editor to automatically rebuild and redeploy the `WAR` file to your local installation of Tomcat.

## Database configuration
EduMeter supports two modes of operation: In-Memory and Persistent PostgreSQL.
The application uses the `IN_MEM_DB` environment variable to determine which storage engine to initialize.
Setting `IN_MEM_DB` to any non-null value will activate the in-memory mode, while leaving it null or unset will configure the application to connect to the database.

### PostgreSQL Setup

To use the persistent database, you must manually create a PostgreSQL database on your system. The application will automatically initialize the schema (tables) upon connection.

You must also set the following environment variables for the connection to succeed:

- `DB_URL`: The JDBC connection string (e.g., `jdbc:postgresql://localhost:5432/edumeter`)
- `DB_USER`: Your database username
- `DB_PASSWORD`: Your database password

## Running tests

Tests live under [`src/test`](./src/test) and they follow the same package structure as [`src/java`](./src/java).
That keeps the structure coherent and enables testing of package-private fields and methods.

Unit tests are run by JUnit. They can be individually ran (per method or per class) directly from the IDE. A purposefully made run configuration may also run all unit tests at once (which is included in the repo).

## Authentication

The `/api/auth/login` endpoint currently accepts every kind of PIN sent, and creates an access token with the user email as the user hash, so it can be used for testing.

Commits can be secured with `@AuthGuard`, which requires authentication and extracts user data in the request's SecurityContext.