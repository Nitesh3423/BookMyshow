# Milestone 1 — Java project setup

## Goal

Create a minimal Java 21 Maven project in this repository. Do not add Spring Boot, Redis, Kafka, or Nginx yet. First make sure plain Java code can compile and run.

## Why this is the next step

The project currently has planning documents and IntelliJ project metadata, but no `pom.xml` or `src` tree. The `java` and `mvn` commands are also not currently available in the shell environment.

## IntelliJ steps

1. Open `D:\Idea Projects\BookMyShow` in IntelliJ IDEA.
2. Open **File → Project Structure → Project** and check whether a JDK is configured.
3. If no JDK is configured, use **Add SDK → Download JDK**, select **Version 21**, and choose a vendor such as Eclipse Temurin.
4. Create a Maven module in this existing project (or use **New Project → Java → Maven**, set the project location to this folder, and preserve the existing `README.md`, `docs`, and `.idea` files).
5. Set the project language level to Java 21 and use the Maven wrapper if IntelliJ offers to create it.
6. Add a small `Main` class and run it from IntelliJ. It should print `BookMyShow domain model is ready`.
7. In IntelliJ's terminal, try `java -version` and `./mvnw -version` (Windows wrapper command: `mvnw.cmd -version`).

Avoid generating over the existing project. If IntelliJ asks to replace or delete existing files, cancel and tell me what it shows.

## Expected starter structure

```text
BookMyShow/
  pom.xml
  mvnw
  mvnw.cmd
  src/
    main/java/com/example/bookmyshow/Main.java
    test/java/
```

The package name can be adjusted to your preference; use one package consistently.

## Completion check

- [ ] IntelliJ recognizes Java 21 for this project.
- [ ] A Maven `pom.xml` exists at the repository root.
- [ ] A `Main` class runs and prints the expected message.
- [ ] Maven wrapper runs from the project terminal.

When this is done, send me the `pom.xml` and `Main.java` (or paste their contents). Then we’ll build the first domain types together: `Seat`, `ShowSeat`, `Booking`, and `BookingStatus`, with a class diagram and small exercises.
