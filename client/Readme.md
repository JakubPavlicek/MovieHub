<a id="readme-top"></a>

<div align="center">
  <img src="../client/src/assets/icons/logo.png" alt="Logo" width="300" height="60">
  <h1 align="center">Movie Hub Client</h1>
</div>

<br />

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#built-with">Built With</a>
    </li>
    <li>
      <a href="#about-the-client">About The Client</a>
      <ul>
        <li><a href="#user-role-support">User Role Support</a></li>
      </ul>
    </li>
    <li>
      <a href="#i18next">i18next</a>
    </li>
    <li>
      <a href="#authorization">Authorization</a>
      <ul>
        <li><a href="#login-options">Login Options</a></li>
      </ul>
    </li>
    <li>
      <a href="#websockets---stomp">WebSockets – STOMP</a>
    </li>
    <li>
      <a href="#openapi-typescript">OpenAPI TypeScript</a>
    </li>
    <li>
      <a href="#running-the-application">Running the Application</a>
    </li>
  </ol>
</details>

## Built With

- [![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=fff)](https://www.typescriptlang.org/)
- [![React](https://img.shields.io/badge/React-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)](https://react.dev/)
- [![TailwindCSS](https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
- [![Vite](https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E)](https://vite.dev/)
- <a href="https://auth0.com/"><img width="150" height="50" alt="JWT Auth for open source projects" src="https://cdn.auth0.com/oss/badges/a0-badge-light.png"></a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## About The Client

The **Movie Hub Client** is a web application designed to provide users with a seamless interface for exploring, rating, and discussing movies.
Built with modern web technologies, it offers a responsive and intuitive user experience across various devices.
The client interacts with the Movie Hub API to retrieve and display movie data, enabling users to access up-to-date information about their favorite films.

### User Role Support
The client currently supports actions performed by users with the role of **USER**.
There are no **ADMIN** functionalities implemented at this time, ensuring that all users have a consistent experience focused on movie exploration and community interaction.

The system already has the following existing user which u can use:

- user with role USER
  - username: user
  - password: User123!
- user with role ADMIN
  - username: admin
  - password: Admin123!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## i18next

The client utilizes **i18next** for internationalization (i18n), allowing for easy localization of the application.
This enables users from different regions to interact with the application in their preferred languages.
The i18next library provides powerful features for translating text, managing language detection, and dynamically loading language resources, making it easy to support multiple languages.
Supported languages are English and Czech.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Authorization

The client application utilizes **Auth0** for user authentication and authorization, providing a secure and streamlined login experience.
By integrating Auth0, the Movie Hub Client ensures that only authenticated users can access certain features, enhancing the overall security of the application.

### Login Options:
Users can log in using their existing accounts from **Google** or **GitHub**, or they can choose to create an account using their email and password or username and password combination.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## WebSockets – STOMP

To enhance user interaction and provide real-time communication, the client implements **WebSockets** using the STOMP protocol for chat functionality.
This enables users to participate in live discussions about movies, share opinions, and send messages to one another instantly.
The STOMP client manages WebSocket connections and message handling, ensuring a smooth and responsive chat experience.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## OpenAPI TypeScript

The client leverages **OpenAPI TypeScript** to generate TypeScript definitions from the OpenAPI specification of the Movie Hub API.
This integration simplifies the process of interacting with the API, as it provides strong typing and autocompletion features, reducing the likelihood of errors when making API calls.
By using OpenAPI TypeScript, I maintained consistency between the API and client code, improving overall development efficiency.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Running the Application

The client UI can be accessed at http://localhost:5173.

<p align="right">(<a href="#readme-top">back to top</a>)</p>