# Project Name
> Wex_Test - An application developed for the Wex challenge

## General Information
- Store a purchase transaction
- Retrieve a transaction with a converted money amount from a specific service
- Adhere to specified rules

## Technologies Used
- Java - OpenJDK 17
- Maven - 3.8.1 (handles all Java dependencies, including Spring Boot and SQLite)
- SQLite - 3.42.0
- Liquibase - 4.27.0
- Spring Boot - 3.3.5
- JUnit - 5
- Docker - 27.2.0

## Features
- Basic CRUD operations for purchase transactions
- Specific API to convert the transaction's money amount into another currency
- Database stored in a single file with migrations handled by Liquibase
- Can be run using Docker

## Setup
### To run with Docker:
- Simply execute the `run_project_withDocker` script. (This will build a Docker image and run it locally)

### To run locally:
- Compile the application with Maven: 
  ```bash
  mvn clean install

- The application runs on 8090 port. (even on a docker run).


## Usage
A basic postman collection is avaliable on this repository

## Project Status
Demo complete!
