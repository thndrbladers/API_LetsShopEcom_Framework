# 🛒 API_LetsShopEcom_Framework
### Cucumber · JUnit · REST Assured · Java — BDD API Automation Framework

A production-style **BDD API test automation framework** built for the *LetsShop E-commerce* REST API. It covers the full e-commerce flow — **User Registration → Login → Product Management → Cart** — and is designed for maintainability, scalability, and CI/CD readiness.

---

## 📑 Table of Contents
1. [Tech Stack](#tech-stack)
2. [Project Structure](#project-structure)
3. [Architecture Overview](#architecture-overview)
4. [Core Concepts Explained](#core-concepts-explained)
   - [Feature File](#1-feature-file)
   - [Gherkin Keywords](#2-gherkin-keywords)
   - [DataTable](#3-datatable)
   - [Scenario Outline + Examples](#4-scenario-outline--examples)
   - [Tags](#5-tags)
   - [Step Definitions](#6-step-definitions)
   - [Hooks](#7-hooks)
   - [Runner](#8-runner)
   - [ApiClient](#9-apiclient)
   - [Domain Clients](#10-domain-clients-authclient-productclient-cartclient)
   - [Payload POJOs](#11-payload-pojos-requestresponse)
   - [ConfigManager](#12-configmanager)
   - [ScenarioContext & PicoContainer DI](#13-scenariocontext--picocontainer-dependency-injection)
   - [TestDataGenerator](#14-testdatagenerator)
   - [Cucumber Reports](#15-cucumber-reports)
5. [End-to-End Flow Example](#end-to-end-flow-example)
6. [Running the Tests](#running-the-tests)
7. [Environment Configuration](#environment-configuration)
8. [Key Design Decisions](#key-design-decisions)

---

## Tech Stack

| Tool | Role |
|---|---|
| **Java 11** | Core language |
| **Maven** | Build & dependency management |
| **Cucumber 7 (JUnit)** | BDD test runner — bridges Gherkin ↔ Java |
| **JUnit 4** | Test execution engine used by Cucumber |
| **REST Assured 6** | Fluent HTTP client for API calls & assertions |
| **PicoContainer** | Dependency Injection — shares state between step definition classes |
| **Jackson Databind** | Serializes/Deserializes Java POJOs ↔ JSON |
| **JavaFaker** | Generates dynamic, realistic test data (names, emails, phones) |
| **Masterthought Reporting** | Generates rich HTML Cucumber reports from JSON output |
| **Maven Surefire Plugin** | Executes tests in parallel during `mvn verify` |

---

## Project Structure

```
API_LetsShopEcom_Framework/
├── pom.xml
└── src/
    ├── main/java/com/apiletsshopecom/
    │   ├── clients/
    │   │   ├── ApiClient.java           # Core HTTP wrapper (GET/POST/PUT/DELETE)
    │   │   ├── AuthClient.java          # Auth domain API calls
    │   │   ├── ProductClient.java       # Product domain API calls
    │   │   └── CartClient.java          # Cart domain API calls
    │   ├── config/
    │   │   └── ConfigManager.java       # Singleton — loads env-specific .properties
    │   └── payloads/
    │       ├── request/
    │       │   ├── LoginRequest.java
    │       │   ├── RegisterRequest.java
    │       │   ├── AddProductRequest.java
    │       │   └── AddToCartRequest.java
    │       └── response/
    │           ├── LoginResponse.java
    │           ├── RegisterResponse.java
    │           ├── AddProductResponse.java
    │           ├── DeleteProductResponse.java
    │           ├── GetProductsResponse.java
    │           └── Product.java
    │
    └── test/
        ├── java/com/apiletsshopecom/
        │   ├── runners/
        │   │   ├── TestRegressionRunner.java   # Runs @Regression tagged scenarios
        │   │   └── TestSmokeRunner.java        # Runs @Smoke tagged scenarios
        │   ├── stepdefinitions/
        │   │   ├── AuthStepDefinitions.java
        │   │   ├── ProductStepDefinitions.java
        │   │   ├── CartStepDefinitions.java
        │   │   └── CommonStepDefinitions.java  # Shared steps (status code, message)
        │   ├── hooks/
        │   │   └── Hooks.java                  # @Before / @After lifecycle hooks
        │   ├── utils/
        │   │   ├── ScenarioContext.java         # Shared state via PicoContainer DI
        │   │   └── TestDataGenerator.java       # JavaFaker-based dynamic data
        │   └── resources/
        │       └── CucumberReports.java         # Masterthought HTML report generator
        └── resources/
            ├── features/
            │   ├── auth/
            │   │   ├── Login.feature
            │   │   └── RegisterUser.feature
            │   ├── product/
            │   │   └── AddProduct.feature
            │   └── cart/
            │       └── Cart.feature
            ├── config/
            │   ├── dev.properties
            │   ├── svt.properties           # Default environment
            │   └── prod.properties
            └── testdata/
                └── iphone.jpg               # Multipart file upload test data
```

---

## Architecture Overview

```
  .feature (Gherkin)                       ← Business-readable test scenarios
       ↓
  StepDefinitions (Java)                   ← Maps Gherkin steps to code
       ↓ injects via PicoContainer
  ScenarioContext                          ← Shared state between step classes
       ↓ delegates to
  Domain Clients (AuthClient, ProductClient, CartClient)
       ↓ extends/uses
  ApiClient (REST Assured)                 ← Central HTTP request engine
       ↓ reads from
  ConfigManager (Singleton)               ← Env-specific base URL, credentials
       ↓ uses
  Payload POJOs (Jackson)                 ← Request/Response serialization
```

---

## Core Concepts Explained

### 1. Feature File
A `.feature` file is written in **Gherkin** — a plain English, structured language that describes application behaviour from a user's perspective. It lives in `src/test/resources/features/` and is the **single source of truth** for what is being tested.

```gherkin
Feature: As a registered user,
  I want to authenticate via the login API
  So that I can receive a secure JWT token to access my account.

  @Regression @Smoke
  Scenario: User should be able to login with valid credentials
    Given the user possesses valid login credentials
    When the user sends a "POST" request to the endpoint "/api/ecom/auth/login"
    Then the API should respond with status code 200
    And the response body should contain a valid JWT "token", "userId" and message "Login Successfully"
```

> **Why it matters in an interview:** A Feature file decouples the *what* (business intent) from the *how* (implementation). Non-technical stakeholders can read and validate it without touching any Java code.

---

### 2. Gherkin Keywords

| Keyword | Purpose |
|---|---|
| `Feature` | High-level description of the functionality under test |
| `Scenario` | A single, independent test case with a specific outcome |
| `Given` | Sets up the **precondition / context** (e.g., user has valid credentials) |
| `When` | Describes the **action / trigger** (e.g., sends a POST request) |
| `Then` | Defines the **expected outcome** (e.g., status code is 200) |
| `And` | Continues the previous keyword's intent (chaining `Given`, `When`, or `Then`) |
| `But` | Used for negative continuation (e.g., `But the cart should not be empty`) |
| `Background` | Steps that run before **every** scenario in a feature file (like a shared `Given`) |

**Key point:** `And` and `But` are syntactic sugar — they behave identically to whatever keyword preceded them. Cucumber maps each line to the same `@Given`, `@When`, `@Then` annotations in Java.

---

### 3. DataTable

A **DataTable** passes structured data directly inline in a step — ideal when a single step needs multiple key-value pairs or a list of rows.

**In the feature file:**
```gherkin
Given the user provides the following product details:
  | _id             | 6960eae1c941646b7a8b3ed3 |
  | productName     | ADIDAS ORIGINAL           |
  | productCategory | electronics               |
  | productPrice    | 11500                     |
```

**In the step definition (Java):**
```java
@Given("the user provides the following product details:")
public void userProvidesProductDetails(DataTable dataTable) {
    Map<String, String> productData = dataTable.asMap(String.class, String.class);
    String productId = productData.get("_id");
    // build your request object from the map
}
```

> **DataTable modes:**
> - `dataTable.asMap()` → single-column key-value pairs (used in this project for Cart)
> - `dataTable.asMaps()` → list of rows as `List<Map<String, String>>` (each row is one record)
> - `dataTable.asLists()` → raw `List<List<String>>` grid

---

### 4. Scenario Outline + Examples

**Scenario Outline** is used for **data-driven testing** — run the same scenario with multiple sets of input data. The `Examples` table feeds values into `<placeholder>` variables.

```gherkin
Scenario Outline: Verify login failure with invalid credentials
  Given the user possesses credentials with email "<email>" and password "<password>"
  When the user sends a "POST" request to the endpoint "/api/ecom/auth/login"
  Then the API should respond with status code 400
  And the response message should be "Incorrect email or password."

  Examples:
    | email                | password     |
    | valid@example.com    | wrongpass123 |
    | unregistered@abc.com | ValidPass!1  |
```

> This generates **two separate test executions** — one per row. Cucumber substitutes `<email>` and `<password>` at runtime.

---

### 5. Tags

Tags (`@TagName`) are annotations on a `Feature` or `Scenario` that allow **selective execution**. They are defined in the Runner via `tags = "@Regression"`.

```gherkin
@Regression @Smoke
Scenario: User should be able to login with valid credentials
```

| Tag | Purpose in this project |
|---|---|
| `@Regression` | Full regression suite — `TestRegressionRunner.java` |
| `@Smoke` | Critical path only — `TestSmokeRunner.java` |
| `@AddProduct` | Scoped hook — triggers cleanup (`deleteProduct`) only after product tests |

**Tag expressions** supported in the runner:
- `@Regression` → runs all regression tests
- `@Regression and @Smoke` → intersection
- `@Regression and not @Slow` → exclusion

---

### 6. Step Definitions

Step definitions are Java methods **annotated with Gherkin keywords** (`@Given`, `@When`, `@Then`) that contain the actual automation logic. Cucumber matches each Gherkin step to a method using regex or Cucumber expressions.

```java
// CommonStepDefinitions.java
@Then("the API should respond with status code {int}")
public void apiRespondsWithStatusCode(int expectedStatusCode) {
    int actualStatus = context.getRawResponse().getStatusCode();
    Assert.assertEquals(expectedStatusCode, actualStatus);
}
```

**Separation of concerns in this project:**

| Class | Responsibility |
|---|---|
| `AuthStepDefinitions` | Login, Register steps |
| `ProductStepDefinitions` | Add, Get, Delete product steps |
| `CartStepDefinitions` | Add to cart steps |
| `CommonStepDefinitions` | Reusable steps — status code, response message assertions |

> **Why split?** Each class is injected with `ScenarioContext` via PicoContainer. Splitting avoids a God class and keeps methods cohesive by domain.

---

### 7. Hooks

Hooks are methods that execute **before or after** a scenario (or the entire suite). They handle setup and teardown logic that shouldn't pollute the Gherkin steps.

```java
// Hooks.java
public class Hooks {
    private ScenarioContext context;

    public Hooks(ScenarioContext context) {   // PicoContainer injects this
        this.context = context;
    }

    @After("@AddProduct")                     // Only runs after @AddProduct scenarios
    public void deleteProduct() {
        ProductClient prodClient = new ProductClient();
        String productId = context.getRawResponse().jsonPath().getString("productId");
        prodClient.deleteProductResponse(productId);   // Cleanup — delete the created product
    }
}
```

| Annotation | When it runs |
|---|---|
| `@Before` | Before each scenario (global or tag-scoped) |
| `@After` | After each scenario (global or tag-scoped) |
| `@Before("@AddProduct")` | Only before scenarios tagged `@AddProduct` |
| `@BeforeAll` / `@AfterAll` | Before/after the entire test suite (Cucumber 7+) |

---

### 8. Runner

The Runner class is the **entry point** for test execution. It wires Cucumber to JUnit and configures features, step definitions, plugins, and tags.

```java
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",        // Where to find .feature files
    glue = {
        "com.apiletsshopecom.stepdefinitions",       // Step definition packages
        "com.apiletsshopecom.hooks"                  // Hook packages
    },
    plugin = {
        "json:target/jsonReports/cucumber-report.json",  // JSON output for Masterthought report
        "pretty"                                          // Console output formatting
    },
    tags = "@Regression"
)
public class TestRegressionRunner {
    @AfterClass
    public static void tearDown() {
        new CucumberReports().generateReports();     // Trigger HTML report generation
    }
}
```

> **Two runners** allow you to run `@Smoke` only (fast feedback) or full `@Regression` (CI pipeline). Maven Surefire picks them both up via `**/*Runner.java` pattern.

---

### 9. ApiClient

`ApiClient` is the **central HTTP engine** of the framework. It wraps REST Assured's `RequestSpecification` with a reusable base configuration — base URL, content type, logging, and auth — so every domain client inherits consistent behaviour without repetition.

```java
// Initialised once per scenario via PicoContainer
ApiClient client = new ApiClient();

// Fluent builder pattern — chain auth or headers
client.withAuthDefaultTestAccount()     // auto-login, stores token in ConfigManager
      .withHeaders(customHeaders);

// Overloaded HTTP methods cover all API shapes
client.get(endpoint);
client.post(endpoint, requestPojo);
client.post(endpoint, formParams, file, "productImage");   // multipart file upload
client.delete(endpoint, pathParam);
```

**Key design choices in `ApiClient`:**
- **`RequestSpecBuilder`** builds a base spec once — avoids repeating `baseUri`, `contentType`, `filters` in every call.
- **Request & Response logging filters** write every request/response to `logFile.txt` for debugging without polluting console output.
- **Overloaded methods** handle the variety of API shapes (body only, body + headers, multipart, query params) cleanly.
- **`withAuth()`** adds a stored JWT token; **`withAuthDefaultTestAccount()`** performs a full login and stores the token dynamically.

---

### 10. Domain Clients (AuthClient, ProductClient, CartClient)

Domain clients sit **between step definitions and ApiClient**. Each client owns one domain and exposes typed methods that return deserialized POJOs — step definitions never touch raw JSON.

```java
// AuthClient.java
public LoginResponse getLoginResponse(LoginRequest loginRequest) {
    Response response = new ApiClient()
                            .post("/api/ecom/auth/login", loginRequest);
    return response.as(LoginResponse.class);   // Jackson deserialization
}

// ProductClient.java — multipart file upload
public Response addProduct(Map<String, String> formParams, File productImage) {
    return new ApiClient()
               .withAuthDefaultTestAccount()
               .post("/api/ecom/product/add-product", formParams, productImage, "productImage");
}
```

> **Why this layer?** If an endpoint or response shape changes, you update **one client class**, not every step definition.

---

### 11. Payload POJOs (Request/Response)

POJOs (Plain Old Java Objects) model the **JSON body** of requests and responses. Jackson (`ObjectMapper`) handles serialization (POJO → JSON) and deserialization (JSON → POJO) automatically via getters/setters.

```java
// LoginRequest.java — sent as the POST body
public class LoginRequest {
    private String userEmail;
    private String userPassword;
    // getters + setters
}

// LoginResponse.java — response.as(LoginResponse.class)
public class LoginResponse {
    private String token;
    private String userId;
    private String message;
    // getters + setters
}
```

**Request POJOs:** `LoginRequest`, `RegisterRequest`, `AddProductRequest`, `AddToCartRequest`  
**Response POJOs:** `LoginResponse`, `RegisterResponse`, `AddProductResponse`, `DeleteProductResponse`, `GetProductsResponse`, `Product`

> `response.as(LoginResponse.class)` — REST Assured uses Jackson to map the JSON response body fields to the matching POJO fields by name automatically.

---

### 12. ConfigManager

`ConfigManager` is a **thread-safe Singleton** that loads environment-specific `.properties` files at runtime. It supports multi-environment setups and CI/CD overrides without code changes.

```java
// Resolution priority:
// 1. JVM system property  (-Denv=dev passed via Maven or command line)
// 2. OS environment variable (ENV=prod)
// 3. Default: "svt"
ConfigManager config = ConfigManager.getInstance();
String baseUrl = config.getBaseUrl();          // reads base.url from {env}.properties
String token   = config.getAuthToken();        // reads auth.token (set at runtime after login)
config.setProperty("auth.token", token);       // stores token in-memory for the test session
```

**Properties files (`src/test/resources/config/`):**

```properties
# svt.properties (default)
base.url=https://rahulshettyacademy.com
test.email=testuser@example.com
test.password=Test@1234
```

Run against a different environment:
```bash
mvn verify -Denv=dev
mvn verify -Denv=prod
```

---

### 13. ScenarioContext & PicoContainer Dependency Injection

**The problem:** Cucumber instantiates a **new instance** of each step definition class per scenario. Data captured in one step class (e.g., the login `Response`) is not accessible in another class (e.g., the status code assertion in `CommonStepDefinitions`).

**The solution — PicoContainer DI:**

`ScenarioContext` is a shared POJO injected into every step definition class via constructor injection. PicoContainer creates **one shared instance per scenario** and automatically injects it wherever it appears in a constructor.

```java
// ScenarioContext.java
public class ScenarioContext {
    private Response rawResponse;
    // getter + setter
}

// AuthStepDefinitions.java
public class AuthStepDefinitions {
    private ScenarioContext context;

    public AuthStepDefinitions(ScenarioContext context) {  // PicoContainer injects this
        this.context = context;
    }

    @When("the user sends a {string} request to the endpoint {string}")
    public void sendRequest(String method, String endpoint) {
        Response response = new AuthClient().login(loginRequest);
        context.setRawResponse(response);   // Store in shared context
    }
}

// CommonStepDefinitions.java
public class CommonStepDefinitions {
    private ScenarioContext context;

    public CommonStepDefinitions(ScenarioContext context) {  // Same instance injected
        this.context = context;
    }

    @Then("the API should respond with status code {int}")
    public void verifyStatusCode(int expected) {
        Assert.assertEquals(expected, context.getRawResponse().getStatusCode());  // Read from shared context
    }
}
```

> **No static variables. No thread-local hacks.** PicoContainer manages the lifecycle cleanly and safely supports parallel execution.

---

### 14. TestDataGenerator

`TestDataGenerator` uses **JavaFaker** to generate realistic, unique test data at runtime. This prevents collisions across parallel runs and removes the need for hardcoded test data.

```java
public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomPhoneNumber() {
        return faker.numerify("##########");   // 10-digit number
    }
}
```

In the feature file, `dynamic` is used as a signal:

```gherkin
Examples:
  | firstName | userEmail | userPassword |
  | Aakash    | dynamic   | Nova@258     |
```

The step definition detects `"dynamic"` and replaces it with a generated value at runtime — keeping the feature file readable while ensuring unique data every run.

---

### 15. Cucumber Reports

After each test run, **Masterthought** generates a rich HTML report from the JSON output produced by the `json` plugin.

```java
// CucumberReports.java — called in @AfterClass of the Runner
public void generateReports() {
    Configuration config = new Configuration(
        new File("target"),      // output dir
        "BDD_APIFramework"       // project name
    );
    ReportBuilder reportBuilder = new ReportBuilder(
        Collections.singletonList("target/jsonReports/cucumber-report.json"),
        config
    );
    reportBuilder.generateReports();
}
```

The HTML report at `target/cucumber-html-reports/overview-features.html` shows:
- Pass/Fail per feature, scenario, and step
- Full request/response logs (via the `pretty` plugin)
- Charts and trend summaries

---

## End-to-End Flow Example

Here is how a **complete Add Product test** flows through the framework:

```
AddProduct.feature
  └─ @AddProduct @Regression
     └─ Scenario: Admin user should be able to add a new product

           ↓ Step: "Given the admin user is authorized with a valid token"
           └─ ProductStepDefinitions → ApiClient.withAuthDefaultTestAccount()
                                     → AuthClient.login() → LoginResponse
                                     → token stored in ConfigManager

           ↓ Step: "When the admin sends a POST request with product details and image"
           └─ ProductStepDefinitions → ProductClient.addProduct(formParams, iphone.jpg)
                                     → ApiClient.post(endpoint, formParams, file, "productImage")
                                     → REST Assured multipart POST → raw Response stored in ScenarioContext

           ↓ Step: "Then the API should respond with status code 200"
           └─ CommonStepDefinitions → context.getRawResponse().getStatusCode() == 200

           ↓ @After("@AddProduct") Hook
           └─ Hooks.deleteProduct() → ProductClient.deleteProductResponse(productId)
                                    → Cleanup — no test pollution left behind
```

---

## Running the Tests

**Run the full regression suite:**
```bash
mvn verify
```

**Run smoke tests only:**
```bash
mvn verify -Dgroups=@Smoke
```

**Run against a specific environment:**
```bash
mvn verify -Denv=dev
mvn verify -Denv=prod
```

**Run a specific tag from the command line:**
```bash
mvn verify -Dcucumber.filter.tags="@Smoke"
```

> Maven Surefire is configured to pick up all `*Runner.java` files and run them in **parallel with 2 threads** (`<threadCount>2</threadCount>`).

---

## Environment Configuration

| File | Environment |
|---|---|
| `src/test/resources/config/dev.properties` | Development |
| `src/test/resources/config/svt.properties` | System Validation Testing *(default)* |
| `src/test/resources/config/prod.properties` | Production |

Each file holds:
```properties
base.url=https://rahulshettyacademy.com
test.email=yourtestuser@example.com
test.password=YourPassword@1
```

`ConfigManager` resolves the active environment from `-Denv` JVM arg → `ENV` OS variable → `svt` default.

---

## Key Design Decisions

| Decision | Reason |
|---|---|
| **`src/main` for clients & payloads** | Keeps reusable framework code separate from test-specific code in `src/test` |
| **ApiClient as a central wrapper** | Single place to change base URL, auth headers, logging — DRY principle |
| **PicoContainer DI** | Clean, stateless way to share `Response` between step classes — no statics |
| **Domain clients (Auth/Product/Cart)** | One class per domain — Single Responsibility Principle |
| **Typed POJOs over raw JSON** | Compile-time safety, IDE autocomplete, easier refactoring |
| **Tag-based runners** | Run smoke vs. regression independently — CI/CD friendly |
| **Scoped `@After` hooks** | Cleanup only runs where needed — keeps tests independent |
| **JavaFaker for dynamic data** | No hardcoded emails that cause duplicate-user failures in parallel runs |
| **`logFile.txt` logging** | Full request/response audit trail without flooding the console |
| **Masterthought HTML reports** | Shareable, visual test results for stakeholders post-run |
