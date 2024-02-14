# Banking Service

This Kotlin Banking Service simulates basic banking operations, such as account creation, deposits, withdrawals, and transfers between accounts. This project is designed to showcase basic Kotlin programming and object-oriented design principles.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them:

- JDK 11 or newer 
- IntelliJ IDEA or any preferred IDE with Kotlin support

### Installing

A step by step series of examples that tell you how to get a development environment running:

1. Clone the repository to your local machine:
   
    `git clone https://github.com/josephpickering9/First-Circle-Code-Test-Kotlin.git`

2. Open the project in IntelliJ IDEA
    - Open IntelliJ IDEA and select Open.
    - Navigate to the project directory and select the project.

3. Build the project:
    - Open the terminal in IntelliJ IDEA and run: `./gradlew build`
    - Alternatively, use the Build menu in IntelliJ IDEA and select Rebuild Project.

## Usage

Run the application through IntelliJ IDEA by right-clicking on the Main.kt file and selecting Run.

Alternatively, execute the following command in the terminal:

`./gradlew run`

## Running the tests

This project includes unit tests for validating the banking operations. To run the tests, execute:

`./gradlew test`

---

*Here is the brief for the code test:*

# Engineering Code Test

Develop a service that simulates basic banking operations in a programming language of your choice. This service will
manage accounts, process deposits, withdrawals, and transfers between accounts. The system should be designed reflecting
real-world constraints of a bank.

Requirements:

1. A class or set of functions that allow:
    - Account creation: Allow users to create an account with an initial deposit.
    - Deposit: Enable users to deposit money into their account.
    - Withdrawal: Allow users to withdraw money from their account, ensuring that overdrafts are not allowed.
    - Transfer: Enable transferring funds between accounts.
    - Account balance: Provide the ability to check the account balance.
2. Database:
    - In-memory data storage will suffice, no need to have a database alongside the project, but you can add one at your
      discretion

The word “service” here is used in a “software component/module” rather “deployable unit with an API” sense, no need to
provide API for it.

Don’t spend more than a couple of hours on the exercise, this will be used as a base for a follow-up pair programming
session.