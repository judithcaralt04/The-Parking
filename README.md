# The Parking

A desktop application for managing a parking lot, built in Java with a layered architecture and a MySQL database. Developed as a team project for the Object Oriented Design and Programming course.

This repository is private. It is shared for portfolio purposes only, and access is limited to people who have been explicitly invited.

## What it does

- User registration and login, with separate roles for regular users and an administrator
- Real time view of parking occupancy
- Vehicle entry and exit tracking
- Slot reservation, including creating, listing and cancelling reservations
- Admin tools to add, edit and remove parking slots, and to manage all reservations
- Simulation of unregistered vehicles entering and leaving at random intervals
- An occupancy statistics screen with a historical chart

## Tech stack

- Java, with Swing and AWT for the interface
- MySQL, accessed through JDBC
- IntelliJ IDEA as the development environment

## Architecture

The project follows a layered architecture with three packages:

- `Presentation`: the Swing views and their controllers
- `Business`: the business logic and domain entities
- `Persistance`: the DAOs that talk to the database, plus the loader for `config.json`

## My contribution

This was a five person team project. My part covered:

- The persistence layer: the base DAO structure, the database connection, and the loader and validator for `config.json`
- The traffic simulation logic: the random timing generator and reading the simulation delay from the configuration
- Several screens in the interface: editing a parking slot, registering a vehicle entry, the slot detail view, and listing and cancelling a user's reservations
- Unit tests across several sprints

## Running it locally

1. Requirements: Java JDK 11 or higher, MySQL Server 8.0 or higher, and the MySQL Connector/J jar
2. Create a database and import `DataBase/parking_db.sql` to set up the tables
3. Copy `config.example.json` to `config.json` (it is ignored by Git, so your own settings are never committed) and set your own database host, port, name, user, password and admin password
4. Run `src/Main.java` from your IDE

Default login: register a new account from the login screen, or sign in as `admin` using the `adminPassword` you set in `config.json`.

## Documentation

`UML_Parking.pdf` contains the class diagram for the project.
