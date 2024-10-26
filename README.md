# Customer Rewards Calculator API

This project is a Java based REST API built using spring boot to manage a retailers rewards program. This calculates reward points for customers based on their purchases. The rewards are computed based on the amount spent in each transaction and are aggregated monthly.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Endpoints](#endpoints)
  - [Calculating Monthly rewards for Customer](#1-calculating-monthly-rewards-for-customer)
  - [Get Rewards Summary for Latest Three-month period](#2-get-rewards-summary-for-latest-three-month-period)
  - [Calculate Reward for amount](#3-calculate-reward-for-amount)
- [Testing](#testing)
  - [Asynchronous API Call](#asynchronous-api-call)
- [Setup and Running](#setup-and-running)
- [Database (H2)](#database-h2)
- [Technology stack](#technology-stack)
- [GitHub Actions](#github-actions)

---

## Overview

The rewards program calculates based on following rules:
- **2 points** for every dollar spent over $100 in each trnsaction
- **1 point** for every dollar spent between $50 and $100 in each transaction.

For Example:
- A $120 purchase = (2 x 20) + (1 x 50) = 90 points

## Features

- **Calculating Monthly rewards for Customer**: Retrieves the monthly reward points for a specified customer.
- **Get Rewards Summary for Latest Three-month period**: Provides a summary of rewards for each customer over the latest three month period specified by a start month.
- **Calculate Reward for amount**: This provides the calculation of reward for the amount given.

---

## Endpoints

### 1. Calculating Monthly rewards for Customer

**Endpoint**: `GET /api/rewards/{customerId}`

**Description**: Retrieves the monthly reward points for a specified customer.

**Path Parameters**: 
- `customerId`: ID of the customer whose rewards need to be calculated.

**Response**:
- Returns a JSON object with month-wise rewards for the customer.

**Example**:
```
GET /api/rewards/1
```
**Sample Response**:
```
{
    "OCTOBER": 90,
    "AUGUST": 250,
    "SEPTEMBER": 25,
    "NOVEMBER": 90
}
```


### 2. Get Rewards Summary for Latest Three-month period

**Endpoint**: `GET /api/rewards/summary?year=[year]&month=[month]`

**Description**: Provides a summary of rewards for each customer over the latest three month period specified by a start month.

**Query Parameters**:
- `year`: Starting year of the three-month period.
- `month`: Starting month of the three-month period.

**Response**:
- Returns a JSON object where each key is a customer ID and the value is an object containing the customers name, monthly rewards and total rewards.

**Example**:
```
GET /api/rewards/summary?year=2024&month=10
```
**Sample Response**:
```
{
    "1": {
        "name": "Dinakar",
        "monthlyRewards": {
            "SEPTEMBER": 25,
            "AUGUST": 250,
            "OCTOBER": 90
        },
        "totalRewards": 365
    },
    "2": {
        "name": "Akku",
        "monthlyRewards": {
            "SEPTEMBER": 70,
            "AUGUST": 10,
            "OCTOBER": 45
        },
        "totalRewards": 125
    }
}
```



### 3. Calculate Reward for amount

**Endpoint**: `GET /api/rewards/calculate/{amount}`

**Description**: This provides the calculation of reward for the amount given.

**Path Parameters**:
- `amount`: Amount you want to calculate the reward.

**Response**:
- Returns a String with the calculation.
**Example**:
```
GET /api/rewards/calculate/:amount
```
**Sample Response**:
```
The calculated reward for amount $90.0 is: 40
```
---

## Testing

Unit tests are written to ensure each functionality of API works as expected.
-  Calculating Monthly rewards for Customer: Tests the monthly reward for the customer.
-  Get Rewards Summary for Latest Three-month period: Tests rewards for each customer over the latest three month period specified by a start month
-  Calculate Reward for amount: Tests the calculation of reward for the amount given.

### Asynchronous API Call

A test added to simulate the asynchronous api call using `CompletableFuture`. This test validates the ability to handle async class within the service layer.

```java
    @Test
    public void testFetchAsync() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> future = apiService.fetchDataSync();

        String result = future.get(2, TimeUnit.SECONDS);
        assertEquals("Fetched data", result, "The asynchronous fetched data");

        assertTrue(future.isDone());
    }
```

Tests are located in the `src/test/java` directory. They can be run using  Maven:
```bash
mvn test
```
The test results are output to `target/surefire-reports`.

---

## Setup and Running

**1. Clone the Repository**:
```bash
git clone https://github.com/DinakarAk/CustomerRewardCalculator.git
cd CustomerRewardCalculator
```
**2. Build the project if maven installed in your local**:
```bash
mvn clean install
```
**3. Run the application**:
```bash
mvn spring-boot:run
```
**4. Access the API**: The API will be accessible at `http://localhost:8080`

---
## Database (H2)
The application uses in memory H2 database for testing and development process.
- schema.sql: This defines database schema, including tables for `Customer` and `Transaction`.
- data.sql: This populates the database data by using INSERT commands to stimulate the real time transactions.

**Sample `schema.sql`**
```sql
CREATE TABLE IF NOT EXISTS Customer (id INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(255) NOT NULL);

CREATE TABLE IF NOT EXISTS Transaction (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT NOT NULL, amount DECIMAL(10, 2) NOT NULL, transaction_date DATE NOT NULL, FOREIGN KEY (customer_id) REFERENCES Customer(id));
```

**Sample `data.sql`**

```sql
INSERT INTO Customer (name) VALUES ('Dinakar');
INSERT INTO Customer (name) VALUES ('Akku');

INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (1, 120.00, '2024-11-25');
INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (1, 120.00, '2024-10-25');
INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (1, 75.00, '2024-09-25');
INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (1, 200.00, '2024-08-25');

INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (2, 95.00, '2024-10-13');
INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (2, 110.00, '2024-09-12');
INSERT INTO Transaction (customer_id, amount, transaction_date) VALUES (2, 60.00, '2024-08-17');


```

---
## Technology Stack

- **Java 8**
- **Spring Boot**
- **Maven**: For dependency management and build.
- **JUnit**: Testing
- **GitHub Actions**: For Continuous Integration.

---

## GitHub Actions

- Checks out the code
- Setup JDK 8
- Caches Maven dependencies
- Builds the project and runs all the test cases
- Archives test results for easy access.
---
