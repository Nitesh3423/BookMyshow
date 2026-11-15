# BookMyShow Learning Project

A learning project for building a movie-ticket booking backend in Java, one milestone at a time. The goal is to use the project to practice Java, LLD, HLD, concurrency, databases, Redis, Kafka, Nginx, testing, and operations.

## Current milestone

**Milestone 0: agree on the MVP scope and booking rules.** No production infrastructure is needed yet.

## Initial technical direction

- Java with Spring Boot
- PostgreSQL as the source of truth for bookings
- Maven for builds
- Start as one modular monolith; add Redis, Kafka, and Nginx in later milestones when the core flow works

See [`docs/scope-and-rules.md`](docs/scope-and-rules.md) for the initial scope, rules, and acceptance criteria.

## How we will work

We will complete one milestone at a time. Each milestone should leave a working, reviewable result and a clear completion check before we add the next major concept or technology.

## Current learning step

Follow [`docs/milestone-1-java-setup.md`](docs/milestone-1-java-setup.md) to create and run a minimal Java 21 Maven project in IntelliJ. The shell environment currently does not have Java or Maven on its PATH, so verify the JDK through IntelliJ first.
