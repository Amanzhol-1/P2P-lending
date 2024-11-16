<img width="939" alt="image" src="https://github.com/user-attachments/assets/939f919f-9bce-4340-b76c-50e9b8b1bc6e"># P2P Lending Platform

## Description
The P2P Lending Platform is a service developed using Spring Boot, designed to facilitate peer-to-peer lending by allowing users to act as both borrowers and lenders. Users can create loan requests with specified amounts, terms, and interest rates. Borrowers can submit requests for funding, while lenders can browse, filter, and select loans to invest in. Each loan agreement is processed as a transaction, and the system automatically calculates repayment schedules and notifies borrowers of upcoming payments. The platform provides reports to both lenders and borrowers on the current status of loans and earnings, making the lending process transparent and secure.

## Technologies

- **Spring Boot** — framework for creating and managing RESTful APIs.
- **Spring Security & JWT** — for secure user authentication and authorization, supporting role-based access control.
- **Hibernate/JPA** — for object-relational mapping and interacting with a relational database.
- **Lombok** — a library to reduce boilerplate code.
- **Redis** — for session management, enhancing application scalability and performance.
- **MongoDB** — used for asynchronous logging, capturing detailed application logs in a NoSQL database.

## Features

- **User Authentication and Authorization**: Secure registration and login functionalities with role-based access control for borrowers and lenders.
- **Loan Requests**: Borrowers can create loan requests specifying desired amounts, interest rates, and loan terms.
- **Lender Offers**: Lenders can create offers to fund loans, specifying the amount, interest rate, and term.
- **Loan Acceptance**: Borrowers can accept lender offers, initiating the loan agreement process.
- **Transaction Management**: Automatic handling of transactions when loans are issued or repayments are made.
- **Payment Scheduling**: Automatic calculation and generation of repayment schedules for loans.
- **Balance Management**: Users can top up their balances and view their current balance.
- **Asynchronous Logging**: Detailed logging of application events and errors, stored in MongoDB for auditing and debugging purposes.
- **Session Management**: Efficient session handling using Redis to improve performance and scalability.
- **Reports**: Provision of reports to lenders and borrowers on loan statuses and earnings.

# API Endpoints Documentation

## 1. Authentication API

| Method | HTTP Request          | Description                              |
|--------|-----------------------|------------------------------------------|
| POST   | `/api/auth/register`  | Registers a new user                     |
| POST   | `/api/auth/login`     | Authenticates a user and returns a JWT   |

---

## 2. User Management API

| Method | HTTP Request                  | Description                     |
|--------|-------------------------------|---------------------------------|
| POST   | `/api/user/balance/top-up`    | Tops up the user's balance      |
| GET    | `/api/user/balance`           | Retrieves the user's balance    |

---

## 3. Lender Offers API

| Method | HTTP Request                  | Description                                     |
|--------|-------------------------------|-------------------------------------------------|
| POST   | `/api/lender/offers`          | Creates a new lending offer (lenders only)     |
| GET    | `/api/lender/offers/active`   | Retrieves all active lender offers             |

## 4. Loan Management API

| Method | HTTP Request                              | Description                                          |
|--------|-------------------------------------------|------------------------------------------------------|
| POST   | `/api/loans/accept-offer/{offerId}`       | Borrower accepts a lender's offer to create a loan   |
| GET    | `/api/loans/{loanId}`                     | Retrieves details of a specific loan                |
| GET    | `/api/loans/borrower`                     | Retrieves all loans for the authenticated borrower  |
| GET    | `/api/loans/lender`                       | Retrieves all loans for the authenticated lender    |

---

## 5. Payment API

| Method | HTTP Request                              | Description                                          |
|--------|-------------------------------------------|------------------------------------------------------|
| POST   | `/api/payments/pay/{paymentId}`           | Processes a payment for a scheduled installment     |
| GET    | `/api/payments/loan/{loanId}`             | Retrieves payment schedule for a specific loan      |

---

## 6. Transaction API

| Method | HTTP Request                              | Description                                          |
|--------|-------------------------------------------|------------------------------------------------------|
| GET    | `/api/transactions`                       | Retrieves all transactions for the user             |

