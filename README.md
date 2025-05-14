# Identity Verification Backend

Backend for Identity Verification App.

## Tech Stack

*   **Java 17**
*   **Spring Boot 3.4.4**
*   **Maven**
*   **Lombok**

## Project Setup

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 17 or later installed.
    *   Apache Maven installed.
2.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd identity-verification-backend
    ```
3.  **Build the project:**
    ```bash
    mvn clean install
    ```

## How to Run

1.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the packaged JAR file:
    ```bash
    java -jar target/identity-verification-backend-0.0.1-SNAPSHOT.jar
    ```
2.  The application will typically start on `http://localhost:8080`.

## API Endpoints

The backend exposes the following RESTful API endpoints under the base path `/api/verify`:

### 1. Initiate Verification

*   **Endpoint:** `POST /initiate`
*   **Description:** Initiates a new identity verification process.
*   **Request Type:** `multipart/form-data`
*   **Parameters:**
    *   `documentFront`: (MultipartFile) The front side of the identity document.
    *   `documentBack`: (MultipartFile, optional) The back side of the identity document (if applicable).
    *   `selfie`: (MultipartFile) A selfie image of the user.
*   **Success Response (200 OK):**
    ```json
    {
      "verificationId": "some-unique-verification-id"
    }
    ```
*   **Error Responses:**
    *   `400 Bad Request`: If required files are missing.
    *   `500 Internal Server Error`: If an unexpected error occurs.

### 2. Get Verification Status

*   **Endpoint:** `GET /status/{verificationId}`
*   **Description:** Retrieves the current status of an ongoing or completed verification.
*   **Parameters:**
    *   `verificationId`: (String, Path Variable) The unique ID received from the `/initiate` endpoint.
*   **Success Response (200 OK):**
    ```json
    {
      "status": "PENDING" // Possible values: PENDING, APPROVED, REJECTED, FAILED, etc.
    }
    ```
*   **Error Responses:**
    *   `400 Bad Request`: If `verificationId` is missing.
    *   `404 Not Found`: If the `verificationId` does not exist.
    *   `500 Internal Server Error`: If an unexpected error occurs.

### 3. Create Account

*   **Endpoint:** `POST /create-account`
*   **Description:** Creates a new user account after successful verification (logic for linking verification to account creation would be in the service layer).
*   **Request Type:** `application/json`
*   **Request Body:**
    ```json
    {
      "email": "user@example.com",
      "password": "securePassword123"
    }
    ```
*   **Success Response (200 OK):**
    ```json
    {
      "message": "Account created successfully.",
      "account": {
        "id": "some-account-id",
        "email": "user@example.com"
        // Other account details as needed
      }
    }
    ```
*   **Error Responses:**
    *   `400 Bad Request`: If email or password are missing.
    *   `409 Conflict`: If the email address already exists.
    *   `500 Internal Server Error`: If an unexpected error occurs.

## Project Structure

The project follows a standard Maven project structure:

*   `identity-verification-backend/`
    *   `.mvn/`: Maven wrapper files.
    *   `src/`: Source code and resources.
        *   `main/`: Main application code.
            *   `java/com/dev/identity_verification_backend/`: Java source files.
                *   `IdentityVerificationBackendApplication.java`: Main Spring Boot application class.
                *   `controller/`: Contains Spring MVC controllers (e.g., `VerificationController.java`) that handle incoming HTTP requests.
                *   `service/`: Contains business logic and service layer components.
                *   `dto/`: Data Transfer Objects used for request/response payloads.
                *   `(model/)`: (Potentially) Domain models or entities (if applicable, not explicitly seen but common).
                *   `(repository/)`: (Potentially) Spring Data repositories for database interaction (if applicable).
            *   `resources/`: Configuration files and static assets.
                *   `application.properties`: Main application configuration file.
                *   `static/`: For static web resources (CSS, JS, images).
                *   `templates/`: For server-side rendered templates (e.g., Thymeleaf, FreeMarker).
        *   `test/`: Test code.
    *   `target/`: Compiled code and packaged JAR/WAR files.
    *   `pom.xml`: Maven Project Object Model file, defines project dependencies, build process, etc.
    *   `README.md`: This file.

## Configuration

The main configuration for the application is in `src/main/resources/application.properties`.

Key configurations include:

*   **Server Port:**
    *   `server.port=8080` (default, can be changed)
*   **File Upload Limits:**
    *   `spring.servlet.multipart.max-file-size=10MB`
    *   `spring.servlet.multipart.max-request-size=25MB`
    *   These values might need adjustment based on the expected size of uploaded documents and selfies.
*   **Identity Verification Provider API Keys:**
    *   The application expects API keys for an identity verification provider. These **should not be hardcoded** in `application.properties` for security reasons.
    *   **Recommended approach:** Use environment variables. For example:
        ```bash
        export IDV_PROVIDER_API_KEY="your_actual_api_key"
        export IDV_PROVIDER_API_SECRET="your_actual_api_secret"
        ```
    *   Then, reference these in `application.properties` or directly in your Java configuration:
        ```properties
        # In application.properties
        # idv.provider.api.key=${IDV_PROVIDER_API_KEY}
        # idv.provider.api.secret=${IDV_PROVIDER_API_SECRET}
        ```
    *   Refer to the Spring Boot documentation on externalized configuration for more options (e.g., Spring Cloud Config).

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please follow these general guidelines:

1.  **Fork the repository.**
2.  **Create a new branch** for your feature or bug fix:
    ```bash
    git checkout -b feature/your-feature-name
    ```
    or
    ```bash
    git checkout -b fix/your-bug-fix-name
    ```
3.  **Make your changes.**
4.  **Ensure your code follows the project's coding style.** (If there are specific style guides or linters, they should be mentioned here).
5.  **Write tests** for your changes to ensure robustness and prevent regressions.
6.  **Commit your changes** with clear and descriptive commit messages. Adhering to conventional commit formats is encouraged if the project uses them.
7.  **Push your branch** to your fork:
    ```bash
    git push origin feature/your-feature-name
    ```
8.  **Open a pull request** to the `main` (or `develop` - please specify the target branch) branch of the original repository.
9.  **Clearly describe your changes** in the pull request, including the problem being solved and the solution implemented.

If you're planning to add a major feature or make significant architectural changes, please open an issue first to discuss the proposed changes with the maintainers.

### Code Style

(To be documented - Specify any code style guidelines, e.g., Google Java Style Guide, or if tools like Checkstyle are used with a specific configuration.)

### Running Tests

To run the tests, you can use the following Maven command:
```bash
mvn test
```

Ensure all tests pass before submitting a pull request. 