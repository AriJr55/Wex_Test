# Project Name
> Wex_Test - An application made for wex challenge

## General Information
- Store a puchase transaction;
- retreive a transaction with a converted money amount froma especific service;
- Attend so described rules.


## Technologies Used
- Java - openjdk:17
- Maven - 3.8.1 (all java dependencies, including spring boot and sqlite)
- Sqlite - 3.42.0
- Liquibase - 4.27.0
- Spring-boot - 3.3.5
- Junit - 5
- Docker - 27.2.0


## Features
- Basic CRUD for puchase transactions;
- Especific api to converte the transaction money amount into another currency;
- Database on just one file, with migrations from liquibase
- Run with Docker.

## Setup
 - To run with docker:
   - Just run the aproprieted run_project_withDocker file. (It will build a docker image and run this local image);
 - To run locally:
   - Just compile the application with maven -> mvn clean install 
The application runs on 8090 port. (even on a docker run).


## Usage
A basic postman collection is avaliable on this repository

`write-your-code-here`


## Project Status
Demo complete!
