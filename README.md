# Recipe Application
This is a simple web application for managing recipes and ingredients. It is built using Java 17, Spring Boot, and H2 database.

## Requirements
To build and run this application, you will need:

* Java 17 or later
* Maven 3.6 or later

## Building the Application
To build the application, run the following command:

````
mvn clean install
````

This will compile the Java code and create a JAR file in the target/ directory.

## Running the Tests
To run the tests, run the following command:

````
mvn test
````
This will run all of the tests in the src/test/java directory.

## Running the Application
To run the application, run the following command:

````
mvn spring-boot:run
````

## Accessing the Swagger Documentation
To access the Swagger documentation for the application, go to http://localhost:8080/swagger-ui.html. This will open the Swagger UI, which provides a user-friendly interface for exploring the API endpoints and making requests. You can use this interface to create ingredients and recipes by sending POST requests to the corresponding endpoints.

## API Endpoints
The following API endpoints are available in the application:

### Ingredients
* #### GET /ingredients - Retrieves a list of all ingredients
* #### GET /ingredients/{id} - Retrieves an ingredient with the specified ID
* #### POST /ingredients - Creates a new ingredient
* #### PUT /ingredients/{id} - Updates an existing ingredient with the specified ID
* #### DELETE /ingredients/{id} - Deletes an ingredient with the specified ID

### Recipes
* #### GET /recipes - Retrieves a list of all recipes
* #### GET /recipes/{id} - Retrieves a recipe with the specified ID
* #### POST /recipes - Creates a new recipe
* #### PUT /recipes/{id} - Updates an existing recipe with the specified ID
* #### DELETE /recipes/{id} - Deletes a recipe with the specified ID

Each endpoint has a corresponding Swagger documentation that includes a description of the endpoint, the HTTP method, the request/response parameters, and example requests and responses.
