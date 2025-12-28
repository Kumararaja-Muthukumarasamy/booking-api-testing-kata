  #                              🏨 Hotel Booking API Automation Framework

 ### Objective

The objective of this project is to design and implement a clean, scalable, and maintainable API automation framework for validating a Hotel Booking REST API.

#### Key goals:

  * Validate core booking workflows (Create, Get, Update, Delete) 
  * Ensure request/response contract validation using schemas
  * Provide a structured, extensible automation framework
  * Enable controlled CI execution with clear test visibility
  * Serve as a baseline for future enhancements (parallelism, retries, reporting)

### Background
The application under test is a Hotel Booking REST API that allows users to:

  * Create a booking
  * Retrieve booking details
  * Update an existing booking
  * Delete a booking
  * Perform health checks
  * Authenticate using an authorization endpoint

The application is publicly accessible and maintained externally.

Application URL : https://automationintesting.online/

### API Documentation (Swagger)

The Swagger/OpenAPI documentation for the endpoints can be accessed at:

Booking API : https://automationintesting.online/booking/swagger-ui/index.html

Authentication API : https://automationintesting.online/auth/swagger-ui/index.html

⚠️ Note
This is an external application not under our control. Occasionally, the Swagger UI may be unavailable or temporarily unreachable.

### Authentication
Some API operations (such as update and delete) require authentication.

The default credentials used for testing are:
      * Username: "admin"
      * Password: "password"

### Swagger Availability Note

This framework primarily relies on runtime validation and schema checks rather than static OpenAPI files.

If the Swagger UI is unavailable:

API behavior can still be validated using existing feature files and schemas

Request/response contracts are validated using JSON schema files stored under: src/test/resources/schemas

External Swagger editors such as https://editor.swagger.io/  may be used for visualization if needed.

### Technology Stack
   *  Java 17
   *  Cucumber (BDD)
   *  Rest Assured
   *  JUnit 5
   *  Maven
   *  Log4j2
   *  GitHub Actions

### Dependencies & Tools (with versions)
   Key dependencies used in the project:
 
   *  cucumber-java:  7.x
   *  cucumber-junit-platform-engine: 7.x
   *  rest-assured:  5.x
   *  junit-platform-suite:  1.x
   *  log4j-core:  2.x
   *  jackson-databind:  2.x
   *  maven-cucumber-reporting:  5.x

👉 Exact versions are maintained in pom.xml.

### Code Quality & Metrics :

The project follows clean code and modular design principles with a focus on
readability, maintainability, and low cognitive complexity.

#### Metrics Guidelines
 - Cognitive complexity is generally kept **≤ 5** per method
 - Complexity is monitored during refactoring and code reviews
 - Minor deviations may be accepted to preserve functional stability
 - Larger or complex logic is isolated into reusable utilities

#### Tooling
- Static code analysis tools may be used to monitor code quality
- Metrics are typically reviewed locally via IDE inspections or build plugins
- Tooling setup is optional and can be enabled as needed

#### Observations
- Client and utility classes are designed with minimal complexity
- Common request specifications are centralized
- Shared logic is reused to avoid duplication
- Some scenarios may exhibit intermittent failures due to shared state

### Folder Structure & Responsibilities

src/test/java
 └── com.booking
     ├── client          → API clients (one per endpoint)
     ├── config          → Configuration reader & keys
     ├── constants       → API constants (response keys, HTTP codes, schema paths)
     ├── model           → POJO models (AuthRequest, BookingRequest, BookingDates)
     ├── runner          → Cucumber JUnit 5 test runner
     ├── spec            → Rest Assured request specification factory
     ├── stepdefinitions → Cucumber step definitions
     ├── testdata        → Test data builders/factories
     └── utils
         ├── auth        → Token management
         ├── context     → Shared scenario context
         ├── data        → Common utility helpers
         ├── logging     → Logger utility
         └── validation  → Schema validation utilities

src/test/resources
 ├── features           → Cucumber feature files
 ├── schemas            → JSON request/response schemas
 ├── spec               → Config files (config.properties, log4j2.xml)
 └── config.properties  → Environment configuration

.github
 └── workflows          → GitHub Actions workflows

pom.xml                 → Maven build configuration
README.md               → Project documentation

### Getting Started (Clone & Setup)

  #### Step 1: Clone the repository
   Clone the project from GitHub:

   git clone https://github.com/Kumararaja-Muthukumarasamy/booking-api-testing-kata.git   
   cd booking-api-testing-kata

  #### Step 2: Prerequisites
   Ensure the following are installed on your system:

   * Java 17
   * Maven 3.8+
   * Git
   * IntelliJ IDEA (Community or Ultimate)

#### Step 3: IDE Setup (IntelliJ IDEA)

   * Open IntelliJ IDEA
   * Select Open Project
   * Choose the cloned project directory
   * Allow Maven dependencies to download
   * Verify Project SDK = Java 17

#### Step 4: Environment Configuration

Update the configuration file: src/test/resources/config.properties

  * base.url=https://automationintesting.online
  * auth.username=admin
  * auth.password=password

#### Step 5: Run Tests

 ##### Run all tests:
   Run the complete test suite:  
   * mvn clean test

 ##### Run tests using tags:
   You can execute specific scenarios or features using Cucumber tags.
   * mvn test -Dcucumber.filter.tags="@booking"
     
##### Run multiple tags:
   * mvn test -Dcucumber.filter.tags="@smoke or @regression"
##### Exclude a tag:
   *  mvn test -Dcucumber.filter.tags="not @smoke"

#####  Run a specific feature file:
  *  mvn test -Dcucumber.features=src/test/resources/features/create-booking.feature

##### Run from IntelliJ IDEA:
  *  Open TestRunner.java -> Right-click → Run

##### To run specific tags, update the tag filter in the runner configuration:

##### Step 7: Reports
 Generated Reports:
   The framework generates different reports based on the Maven goal used.

#### Running with mvn clean test
   *  mvn clean test

  ##### Generates:
  *  Cucumber HTML Report – Human-readable execution report
  *  Cucumber JSON Report – Used for further processing and integrations

   target/
   └── reports/
     ├── cucumber-reports.html
     └── cucumber-reports.json
     
  These reports provide basic execution details for scenarios and steps.

#### Running with mvn test verify
  *  mvn test verify

In addition to the standard Cucumber reports, this command generates a detailed aggregated HTML report using the reporting plugin.

Location:
target/
 └── cucumber-html-reports/
     └── overview-features.html

This report includes:
    >  Feature-wise execution summary
    >  Scenario and step-level details
    >  Passed / failed / skipped statistics

#### Failed Scenarios (Rerun File)
   When tests fail, failed scenarios are captured in a rerun file.

 Location:
         target/
            └── rerun.txt
 
This file contains the list of failed scenarios and can be used for analysis or selective reruns.

The rerun file is generated via the configured Cucumber plugin:
     
  *  rerun:target/rerun.txt

##### Notes:

  * Reports are generated automatically after execution
  * No additional configuration is required to generate reports
  * The detailed HTML report is available only when running with mvn test verify
  * Logs are generated using Log4j2 and can be configured via log4j2.xml.

### CI/CD (GitHub Actions)

  *   Current CI Setup
  *   GitHub Actions configured
  *   Manual trigger only (workflow_dispatch)
  *   Runs mvn clean test
  *   No automatic PR or push triggers (intentional)

#### Adding New Tests / Files
  When adding a new API test, follow these steps:

  ##### Create a feature file: 
   Add a new .feature file under:
    
   *  src/test/resources/features

  ##### Add step definitions:
   Implement step definitions under:   
  
  * src/test/java/com/booking/stepdefinitions

  ##### Create or reuse an API client:
   Add a new client or reuse an existing one under:   
  * src/test/java/com/booking/client

  ##### Add or reuse POJO models:
   Define request/response models (if required) under:   
   * src/test/java/com/booking/model

  ##### Reuse test data factories: 
  Use existing builders and factories under:    
   * src/test/java/com/booking/testdata

  ##### Reuse constants:
   Use predefined constants for response keys, HTTP codes, and schema paths under:
    * src/test/java/com/booking/constants

  ##### Add schema validation (if required)
   Add JSON schema files under:      
    * src/test/resources/schemas

### Guidelines

  * Prefer reusing existing clients, models, and utilities
  * Avoid hard-coded values in step definitions
  * Centralize common logic in factories or utility classes
  * Keep feature files readable and business-focused

 ##### Assertions

   * Perform assertions primarily in step definition classes
   * API clients should focus only on request execution and response retrieval
   * Avoid embedding assertions inside client classes

 ##### Logging

   * Use logging judiciously and only where it adds value
   * Prefer logging at the client layer for request and response details
   * Avoid excessive logging in step definitions to keep test output clean

### Observations & Known Limitations

  #### Current Observations

   *  PATCH scenarios pass when run independently
   *  PATCH scenarios are excluded from full-suite execution
   *  Some scenarios may intermittently fail due to shared state

### Final Note

This framework is designed as a clean baseline with a clear roadmap for future improvements.
Stability, readability, and maintainability are prioritized over aggressive optimizations.
