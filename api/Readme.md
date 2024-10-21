<a id="readme-top"></a>

<div align="center">
  <img src="../client/src/assets/icons/logo.png" alt="Logo" width="300" height="60">
  <h1 align="center">Movie Hub API</h1>

![Static Badge](https://img.shields.io/badge/coverage-95%25-brightgreen)
</div>

<br />

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#built-with">Built With</a>
    </li>
    <li>
      <a href="#architecture">Architecture</a>
    </li>
    <li>
      <a href="#database">Database</a>
      <ul>
        <li><a href="#optimization">Optimization</a></li>
      </ul>
    </li>
    <li>
      <a href="#authorization">Authorization</a>
      <ul>
        <li><a href="#key-features-of-auth0-integration">Key Features of Auth0 Integration</a></li>
        <li><a href="#how-it-works">How It Works</a></li>
      </ul>
    </li>
    <li>
      <a href="#websockets">WebSockets</a>
      <ul>
        <li><a href="#user-role-authorization">User Role Authorization</a></li>
      </ul>
    </li>
    <li><a href="#api">API</a></li>
    <li>
      <a href="#testing">Testing</a>
      <ul>
        <li><a href="#unit-testing">Unit Testing</a></li>
        <li><a href="#integration-testing">Integration Testing</a></li>
        <li><a href="#slice-testing">Slice Testing</a></li>
      </ul>
    </li>
  </ol>
</details>

## Built With

- [![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/en/)
- [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=fff)](https://spring.io/projects/spring-boot)
- [![OpenAPI](https://img.shields.io/badge/OpenAPI%20Initiative-6BA539.svg?style=for-the-badge&logo=OpenAPI-Initiative&logoColor=white)](https://www.openapis.org)
- [![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36.svg?style=for-the-badge&logo=Apache-Maven&logoColor=white)](https://maven.apache.org)
- [![Liquibase](https://img.shields.io/badge/Liquibase-2962FF.svg?style=for-the-badge&logo=Liquibase&logoColor=white)](https://www.liquibase.org)
- [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=for-the-badge&logo=PostgreSQL&logoColor=white)](https://www.postgresql.org)
- <a href="https://auth0.com/"><img width="150" height="50" alt="JWT Auth for open source projects" src="https://cdn.auth0.com/oss/badges/a0-badge-light.png"></a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Architecture

For this project, I opted for a classical layered architecture, which is widely used in Spring Boot applications. This architecture separates concerns by dividing the application into distinct layers, each responsible for specific tasks, enhancing maintainability, scalability, and testability.

The typical layers in this architecture include:

- **Controller Layer**: Handles HTTP requests and responses, forwarding requests to the appropriate service layer.
- **Service Layer**: Contains the business logic and interacts with the data layer to fetch or manipulate data.
- **Repository Layer**: Responsible for data access and interacts with the database.

<img src="architecture.svg" alt="Logo" width="800">

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Database

This section showcases the PostgreSQL database structure or schema diagram used in the project.
It provides an overview of the data relationships and design choices made for the application.
The database is created using **Liquibase**.

### Optimization

Due to the extensive relationships in the movie table (and other related tables), I implemented **LAZY fetching** for all relationships across the tables.
This approach prevents unnecessary data from being fetched when retrieving a specific entity, improving performance and efficiency.

By using lazy fetching, I avoid fetching related entities (such as comments, ratings, cast, etc.) unless they are explicitly required.
This strategy helps mitigate the **N+1 query problem**, which would occur if all relationships were fetched eagerly, potentially leading to performance bottlenecks due to excessive database queries.

With lazy fetching, when I fetch a movie entity, I only retrieve the movie itself.
If I need additional related data, I fetch it manually.
This optimization significantly improved the performance of my application, as previously, fetching a movie would also pull in all associated data, slowing down the application.

<img src="../database.jpeg" alt="database" width="900">

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Authorization

The application handles user authentication and authorization using **Auth0**, a secure and flexible authentication platform.
By integrating Auth0, the application ensures that only authenticated users can access certain endpoints, while protecting sensitive data.

### Key Features of Auth0 Integration:
- **OAuth 2.0 and OpenID Connect:** Auth0 supports industry-standard protocols, ensuring a secure and robust authentication flow.
- **JWT (JSON Web Tokens):** Auth0 generates JWTs that securely pass user data between the client and server, enabling stateless authentication.
- **Role-Based Access Control (RBAC):** Different user roles (ADMIN, USER) are defined to restrict access to specific features or resources within the application.

By leveraging Auth0’s features, the application maintains a high level of security while ensuring that users have a smooth and easy login experience.

### How It Works:
1. When a user tries to access a protected resource, they are redirected to Auth0 for authentication.
2. Auth0 handles the authentication process and, upon successful login, returns a JWT to the application.
3. The JWT is then used to verify the user’s identity and determine their permissions based on their assigned roles.

This approach enhances both the security and usability of the Movie Hub app, while offloading the complexity of managing authentication and authorization.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## WebSockets

The application utilizes **WebSockets** to enable real-time, bidirectional communication between the client and the server.
This allows users to chat without needing to constantly refresh or make repeated HTTP requests.

With WebSockets, the server can push updates to the client as soon as they happen, creating a more interactive and responsive user experience.

### User Role Authorization
WebSocket functionality is authorized only for users with the roles of **USER** or **ADMIN**.
This ensures that only authenticated users can participate in live discussions and engage with others in the community.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## API

The Movie Hub API is documented and described using **OpenAPI** Specification. You can view and interact with the API documentation locally using **SwaggerUI**.

Once the application is running, the SwaggerUI interface can be accessed by navigating to the following URL in your browser:

http://localhost:8080/swagger-ui/index.html

This interface provides a user-friendly way to explore the available API endpoints, view detailed information about each endpoint, and make requests directly from the browser.

The OpenAPI Specification file can be found here:

[openapi.yaml](../openapi.yaml)

This file is used to generate the Spring Boot Controllers (interfaces).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Testing

The application employs a robust testing strategy that includes both **unit** and **integration tests** to ensure the correctness, reliability, and performance of the codebase.
The testing suite achieves an impressive **95% test coverage**, reflecting the thoroughness of the testing process.

### Unit Testing
Unit tests focus on individual components or services in isolation, ensuring that each unit of code functions as expected. By using mocking frameworks, dependencies are replaced with mock objects, allowing for fast and reliable tests without the need to connect to external resources like databases.

### Integration Testing
Integration tests validate the interaction between different parts of the application, ensuring that the components work together as intended. These tests are crucial for verifying that the service, repository, and controller layers communicate correctly with each other and with external systems, such as the database.

### Slice Testing
In Spring Boot, **slice tests** are used to isolate specific layers of the application. This allows for more focused testing of key components, such as:

- **@WebMvcTest** for testing controllers
- **@DataJpaTest** for testing JPA repositories
    - I used **Testcontainers** to run tests with a real PostgreSQL database, rather than relying on the H2 in-memory database, ensuring more accurate test results.

By using slice tests, the application avoids unnecessary initialization of unrelated beans, speeding up the testing process while maintaining high test coverage.

This comprehensive testing approach ensures that both individual components and their interactions are functioning as expected, helping to maintain a reliable and high-performing application.

<p align="right">(<a href="#readme-top">back to top</a>)</p>