# BookMyShow Learning Project Checklist

A staged project plan for learning Java, HLD, LLD, concurrency, Redis, Kafka, Nginx, testing, and scaling by building a movie-ticket booking system.

**Project rule:** complete each stage’s acceptance checks before adding the next major technology. Start as a modular monolith. Keep the relational database authoritative for confirmed bookings.

## Project setup and scope

- [ ] Choose a first version scope: one city, a few venues, movies, shows, and reserved seating.
- [ ] Write business rules: seat hold duration, payment timeout, cancellation policy, and booking states.
- [ ] Define booking states, for example `PENDING`, `CONFIRMED`, `PAYMENT_FAILED`, `EXPIRED`, `CANCELLED`.
- [ ] Draw a simple user journey: browse → select show/seats → hold → pay → confirmation.
- [ ] Create a Git repository, README, issue board, and a way to run the app locally.
- [ ] Record architecture decisions as short ADRs when making meaningful choices.

**Done when:** the scope and rules are written down, and another person can explain the intended booking flow.

## Stage 1 — Java foundations and domain model (LLD)

- [ ] Create domain types: `Movie`, `Venue`, `Screen`, `Show`, `Seat`, `ShowSeat`, `Booking`, `BookingItem`, `Payment`.
- [ ] Separate a physical seat from its availability for a particular show (`Seat` vs. `ShowSeat`).
- [ ] Model money with a safe decimal type; do not use floating point for prices.
- [ ] Use enums/value objects for booking status, payment status, seat type, and identifiers where useful.
- [ ] Encapsulate state changes behind domain methods rather than arbitrary setters.
- [ ] Define interfaces for repositories, payment processing, and notification delivery.
- [ ] Add validation and clear domain exceptions for invalid transitions or unavailable seats.
- [ ] Practice collections, generics, streams, `equals`/`hashCode`, immutability, and exception handling.
- [ ] Draw a class diagram and sequence diagrams for booking and cancellation.

**Done when:** core booking rules can be exercised without a web server or database, and invalid state transitions are rejected.

## Stage 2 — Basic application and persistence

- [ ] Create a Spring Boot application with a documented local run command.
- [ ] Add REST endpoints to browse movies, venues, shows, and show-seat availability.
- [ ] Add endpoints to create a booking, view booking status, and cancel where allowed.
- [ ] Validate request payloads and return consistent error responses.
- [ ] Persist data in PostgreSQL (or another relational database) using migrations.
- [ ] Design tables, keys, foreign keys, unique constraints, and indexes.
- [ ] Add seed data for repeatable local development.
- [ ] Use service/repository boundaries and dependency injection without over-layering simple code.
- [ ] Add configuration via environment variables and keep secrets out of source control.

**Done when:** a user can browse seeded shows, submit a booking, and retrieve the saved booking after restarting the app.

## Stage 3 — Seat booking correctness and Java concurrency

- [ ] Define the invariant: one show-seat can have at most one active hold or confirmed booking.
- [ ] Implement a transaction for reserving seats and confirming a booking.
- [ ] Enforce correctness in the database with appropriate unique constraints and/or row locking.
- [ ] Handle simultaneous attempts to reserve the same seat; one must succeed and the other receive a useful conflict response.
- [ ] Add short-lived holds and an expiry process for unpaid bookings.
- [ ] Make state transitions safe when payment success, timeout, cancellation, and expiry race.
- [ ] Learn and apply Java threads, executors, futures, synchronization, locks, and thread-safe collections in small focused exercises.
- [ ] Avoid holding a database transaction open while waiting for an external payment provider.
- [ ] Add a concurrency test that fires competing reservation requests for the same seat.

**Done when:** repeated concurrent attempts never produce two confirmed bookings for the same show-seat.

## Stage 4 — Payment and idempotency

- [ ] Define a payment-provider interface and a local fake/sandbox implementation.
- [ ] Use a payment attempt record with provider reference and status.
- [ ] Add idempotency keys for booking/payment requests so retries do not create duplicate charges or bookings.
- [ ] Handle payment success, decline, timeout, and unknown outcome explicitly.
- [ ] Add a reconciliation path for a payment that succeeds while the app misses the response.
- [ ] Define cancellation/refund behavior and audit status changes.
- [ ] Ensure API retries return the existing result for the same idempotency key.

**Done when:** retrying a request or receiving a delayed payment result does not double-book or double-charge.

## Stage 5 — Redis caching and temporary holds

- [ ] Run Redis locally and document how to start it.
- [ ] Cache read-heavy movie, venue, and show listings with explicit TTLs.
- [ ] Define cache keys, value formats, expiry, and invalidation/update behavior.
- [ ] Measure database reads before and after caching a browse endpoint.
- [ ] Use Redis TTL/atomic operations for temporary seat holds if appropriate.
- [ ] Store hold owner/token and expiry; require the matching token to confirm or release a hold.
- [ ] Keep database constraints/transactions as the final protection against duplicate confirmed seats.
- [ ] Decide behavior when Redis is unavailable or loses data; do not silently assume a hold is permanent.
- [ ] Document cache stampede and stale-data risks and select a practical mitigation.

**Done when:** cached browse results expire or refresh correctly, and a Redis outage does not permit duplicate confirmed bookings.

## Stage 6 — Kafka and asynchronous work

- [ ] Run Kafka locally and document topics, partitions, and consumer groups.
- [ ] Publish events such as `BookingConfirmed`, `BookingCancelled`, and `PaymentFailed`.
- [ ] Build a notification consumer (email/SMS can be mocked) and an analytics consumer.
- [ ] Include event ID, booking ID, event type, and creation time in event envelopes.
- [ ] Make consumers idempotent so redelivery does not duplicate side effects.
- [ ] Add retry handling and a dead-letter topic for messages that repeatedly fail.
- [ ] Decide which operations must be synchronous and which can be eventually consistent.
- [ ] Implement the transactional outbox pattern so a committed booking cannot lose its event.
- [ ] Document ordering assumptions and partitioning key (for example, booking ID).

**Done when:** booking confirmation commits reliably, events are eventually consumed, and replaying a message has no duplicate effect.

## Stage 7 — Nginx, multiple instances, and deployment

- [ ] Containerize the app and its local dependencies with Docker.
- [ ] Run at least two application instances behind Nginx.
- [ ] Configure Nginx upstream load balancing and proxy headers/timeouts.
- [ ] Add liveness/readiness health endpoints and configure health-aware routing where supported.
- [ ] Keep application instances stateless; move shared state to database/Redis/Kafka.
- [ ] Verify the app still works when one instance is stopped.
- [ ] Configure TLS for a deployed environment and avoid exposing internal dependencies publicly.
- [ ] Add a CI pipeline for build, static checks, and requested automated checks.
- [ ] Document deployment, configuration, rollback, and local development steps.

**Done when:** requests are served through Nginx by multiple instances, and stopping one instance does not take down the service.

## Stage 8 — Testing, load, and performance

- [ ] Unit test domain rules and state transitions.
- [ ] Integration test database transactions, migrations, Redis behavior, and Kafka consumers.
- [ ] API test successful flows and failures: invalid input, unavailable seat, expired hold, payment failure.
- [ ] Test idempotency and duplicate Kafka delivery.
- [ ] Load test browsing and booking separately with a documented workload.
- [ ] Track latency percentiles, throughput, error rate, and resource use.
- [ ] Use results to find bottlenecks before adding more infrastructure.
- [ ] Re-test seat contention and correctness under load.
- [ ] Record a baseline and changes made after optimization.

**Done when:** test results are repeatable and show correctness under contention plus measured behavior under load.

## Stage 9 — Observability, resilience, and operations

- [ ] Use structured logs with request/correlation IDs; do not log sensitive payment data.
- [ ] Add metrics for booking attempts, conflicts, payment outcomes, hold expiry, Kafka lag, and cache hit rate.
- [ ] Add tracing across API, database, Redis, and Kafka where practical.
- [ ] Define timeouts and bounded retries for dependencies; avoid retry storms.
- [ ] Add graceful degradation for browsing when optional dependencies fail.
- [ ] Document failure cases: database outage, Redis outage, Kafka outage, app instance loss, and payment timeout.
- [ ] Define backup and restore steps for the database and test a restore.
- [ ] Define retention and archival rules for old booking/event data.
- [ ] Write a short runbook for common operational problems.

**Done when:** failures are visible, recovery steps are documented, and a database restore has been rehearsed.

## Stage 10 — HLD evolution and optional extensions

- [ ] Draw the initial HLD: clients → Nginx → app instances → database/Redis/Kafka/payment provider.
- [ ] Annotate the booking sequence, data ownership, consistency needs, and failure boundaries.
- [ ] Estimate traffic and storage with stated assumptions.
- [ ] Identify the measured bottleneck before proposing a scale change.
- [ ] Consider read replicas, database partitioning, search, rate limiting, or service separation only when justified.
- [ ] If splitting services, define ownership and communication contracts first.
- [ ] Compare tradeoffs for strong consistency in seat booking vs. eventual consistency in notifications/analytics.
- [ ] Update diagrams and ADRs as the design evolves.

**Done when:** you can explain the architecture and justify each component with a concrete requirement or measurement.

## Progress tracker

| Stage | Status | Notes / link |
|---|---|---|
| Scope and setup | Not started | |
| Java domain model (LLD) | Not started | |
| Spring Boot and database | Not started | |
| Concurrency and booking correctness | Not started | |
| Payment and idempotency | Not started | |
| Redis | Not started | |
| Kafka | Not started | |
| Nginx and multiple instances | Not started | |
| Testing and load | Not started | |
| Observability and recovery | Not started | |
| HLD evolution | Not started | |

## Suggested first milestone

Build only the browse and booking flow with seeded data, Spring Boot, and PostgreSQL. First make the booking rules correct with a database transaction and a concurrency test. Then add Redis, Kafka, and Nginx one at a time, recording the behavior each one improves.
