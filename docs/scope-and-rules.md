# MVP scope and booking rules

## Purpose

Build a small but correct movie-ticket booking backend that can grow into a learning project for Java, low-level design (LLD), high-level design (HLD), concurrency, and distributed systems.

## MVP boundary

The first usable version covers:

- One city
- A small seeded catalog of movies, venues, screens, shows, and seats
- Reserved seating
- Browsing movies, shows, and a show's seat availability
- Selecting one or more seats for one show
- Creating a temporary booking/seat hold
- A simulated payment result (no real payment provider)
- Confirming or failing a booking
- Viewing a booking by its identifier
- Cancellation only after its policy is specified in a later milestone

Not in the first version: multiple cities, general admission, real payment processing, refunds, coupons, recommendations, reviews, multiple currencies, or microservices.

## Core rules

1. A seat belongs to a screen. Its availability and price are specific to a show.
2. A seat for a given show can be held by at most one active booking at a time.
3. A successful booking must never sell the same show-seat twice, even if requests arrive concurrently.
4. A hold has an expiry time. An unpaid/unfinished hold must stop blocking the seat after expiry.
5. Only a valid, unexpired hold can be confirmed by the simulated payment flow.
6. A failed payment does not create a confirmed booking; the hold is released or expires according to the implemented transition.
7. Booking and payment state changes follow explicit, validated transitions. Invalid transitions return a clear error.
8. PostgreSQL will ultimately be authoritative for confirmed bookings. Redis, when introduced, may support short-lived holds/cache, but must not be the only safeguard against duplicate confirmed seats.
9. Retrying a booking/payment request must not create duplicate bookings or payment effects. Idempotency will be implemented in the payment milestone.
10. Notifications and analytics may be asynchronous later; they do not determine whether a seat sale is valid.

## Initial booking lifecycle

`PENDING` (seat hold created) → `CONFIRMED` (simulated payment succeeds)

`PENDING` → `PAYMENT_FAILED` (simulated payment declines)

`PENDING` → `EXPIRED` (hold deadline passes)

`CONFIRMED` → cancellation is intentionally out of initial scope until cancellation/refund rules are defined.

## First API shape (to refine during implementation)

- `GET /movies`
- `GET /shows?movieId=...`
- `GET /shows/{showId}/seats`
- `POST /bookings` (show ID and requested seat IDs; create a hold)
- `POST /bookings/{bookingId}/payment` (simulate payment outcome)
- `GET /bookings/{bookingId}`

The API paths are a starting point, not a frozen contract. We will add request/response examples and error semantics when implementing the API milestone.

## Acceptance checks for this scope milestone

- [ ] MVP features and exclusions are understood.
- [ ] Booking states and transitions are agreed.
- [ ] The no-double-sale invariant is explicit.
- [ ] Initial technology direction is agreed: Java/Spring Boot, Maven, PostgreSQL, modular monolith.
- [ ] Next milestone is the Java domain model and LLD, before adding Redis/Kafka/Nginx.

## Open decisions for later milestones

- Hold duration and cleanup mechanism
- Seat pricing and fees
- Cancellation deadline and refund behavior
- Authentication and authorization
- Exact Java/Spring Boot versions and packaging
- Database locking/constraint approach after comparing options
- Redis key design and failure behavior
- Kafka event schemas, retry policy, and outbox
- Deployment target and traffic/load assumptions
