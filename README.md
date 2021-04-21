# devonfw-microservices

## reference-project
The reference-project folder contains a small Quarkus application that should show you some basics of Quarkus microservice development, as well as how to use some tkit-quarkus extensions.

#### Prerequisites
* Git
* Docker
* Java JDK 11+

The application uses a Postgres database as datasource. The easiest way to run a Postgres database is to start a Docker container that runs the database. You can find the instructions for doing this at https://hub.docker.com/_/postgres. If you want to use a different database, you need to change the JDBC driver in the pom.xml file.
In order for the application to connect to the database, you must configure the username and password of the database as environment variables. Set QUARKUS_DATASOURCE_PASSWORD and QUARKUS_DATASOURCE_USERNAME with the respective credentials.

To run the application in the build-in development mode execute `mvn compile quarkus-dev`.
Once the application is running, navigate to http://localhost:8080 in your browser. The page displays a list of available REST services that the application provides.

You can also package and run the application. The command `mvn package` will create the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Run the application by using `java -jar target/quarkus-app/quarkus-run.jar`.
