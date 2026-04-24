
# ProductService — Unit Tests

A comprehensive JUnit 5 test suite for `ProductService`, covering all CRUD operations with Mockito-based mocking of the `ProductRepository` layer.

---

## Overview

This test suite validates the business logic of `ProductService` in isolation from the database. It uses **Mockito** to stub repository calls and 
**AssertJ** for fluent assertions. Tests are organized into nested classes, one per service method, for clarity and structure.

---

## Tech Stack

| Tool | Purpose |
|---|---|
| Java | Language |
| JUnit 5 | Test framework |
| Mockito | Mocking & verification |
| AssertJ | Fluent assertions |
| Spring Boot (implied) | Application framework |

---

## Project Structure

```
src/
└── test/
    └── java/
        └── com/josamtechie/api/service/
            └── ProductServiceTest.java
```

---

## Test Coverage

### `saveProduct()`
- ✅ Saves and returns a product when the name is valid
- ✅ Throws `RuntimeException` when name is `null`
- ✅ Throws `RuntimeException` when name is empty

### `addProducts()`
- ✅ Saves and returns all products in the list
- ✅ Returns an empty list when input is empty

### `findAll()`
- ✅ Returns all products from the repository
- ✅ Returns an empty list when no products exist

### `findById()`
- ✅ Returns the product when found by ID
- ✅ Returns `null` when no product matches the given ID

### `findByName()`
- ✅ Returns the product when found by name
- ✅ Returns `null` when no product matches the given name

### `updateProduct()`
- ✅ Updates and returns the product when it exists
- ✅ Throws `AssertionError` when the product does not exist

### `deleteProduct()`
- ✅ Calls `deleteById` on the repository with the correct ID
- ✅ Propagates exceptions thrown by the repository

---

## Running the Tests

### Prerequisites
- Java 17+
- Maven or Gradle

### Maven
```bash
mvn test
```

### Gradle
```bash
./gradlew test
```

To run only this test class:
```bash
# Maven
mvn test -Dtest=ProductServiceTest

# Gradle
./gradlew test --tests "com.josamtechie.api.service.ProductServiceTest"
```

---

## Key Design Decisions

- **`@ExtendWith(MockitoExtension.class)`** — enables Mockito annotation support without a Spring context, keeping tests fast.
- **`@InjectMocks`** — instantiates `ProductService` and injects the mocked `ProductRepository` automatically.
- **`@Nested` classes** — groups related test cases per method, improving readability and IDE navigation.
- **BDD-style Mockito** (`given` / `then`) — aligns test structure with the Arrange–Act–Assert pattern.
- **`@BeforeEach` setup** — reuses a shared `Product` fixture across tests, reducing boilerplate.

---

## Dependencies

Add the following to your `pom.xml` (Spring Boot projects include these via `spring-boot-starter-test`):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

This pulls in JUnit 5, Mockito, and AssertJ automatically.

---

## License

This project is open source. See [LICENSE](LICENSE) for details.
