# devonfw-microservices

## reference-project
The reference-project folder contains a small Quarkus application that should show you some basics of Quarkus microservice development, as well as how to use some tkit-quarkus extensions.

#### Prerequisites
* Git
* Docker
* Java JDK 11+

You must have a running database on your machine that the application can connect to. You can use the following command to start a Docker container with a Postgres database.
`docker run --name quarkus_demo_db -e POSTGRES_USER=demo -e POSTGRES_PASSWORD=demo -e POSTGRES_DB=demo -p 5432:5432 postgres:11.5`

To run the application in the build-in development mode execute `mvn compile quarkus-dev`.
You can also package and run the application. The command `mvn package` will create the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Run the application by using `java -jar target/quarkus-app/quarkus-run.jar`.

Once the application is running, navigate to http://localhost:8080 in your browser. The page displays a list of available REST services that the application provides.