# CTF Learn Automation Framework

A comprehensive test automation framework for CTF Learn platform and Exness mobile trading application. This framework supports both web and mobile testing using Selenium WebDriver and Appium.

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Features](#features)
- [Technologies](#technologies)
- [Setup and Installation](#setup-and-installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [CI/CD Integration](#cicd-integration)
- [Project Components](#project-components)
- [Contributing](#contributing)

## Overview

This framework provides a robust solution for automated testing of the CTF Learn platform (web application) and Exness mobile trading application. It follows the Page Object Model design pattern for maintainable and reusable test automation.

## Project Structure

```
cbtw_automation/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── automation/
│   │           ├── ctflearn/
│   │           │   ├── dataObject/
│   │           │   │   └── web/
│   │           │   │       └── CTFLearnChallenge.java
│   │           │   ├── mobile/
│   │           │   │   ├── LoginScreen.java
│   │           │   │   ├── ProfileScreen.java
│   │           │   │   └── TradeScreen.java
│   │           │   └── web/
│   │           │       ├── ChallengeHomePage.java
│   │           │       ├── ChallengesPage.java
│   │           │       ├── CreateChallengePage.java
│   │           │       ├── HomePage.java
│   │           │       └── LoginPage.java
│   │           └── utils/
│   │               ├── mobile/
│   │               │   ├── AppiumDriverManager.java
│   │               │   ├── AppLauncher.java
│   │               │   ├── BaseMobilePage.java
│   │               │   └── Mobile.java
│   │               ├── web/
│   │               │   ├── BaseWebPage.java
│   │               │   └── WebDriverFactory.java
│   │               ├── AssertHelper.java
│   │               ├── ConfigLoader.java
│   │               ├── Element.java
│   │               ├── JsonLoader.java
│   │               ├── LogUtil.java
│   │               ├── PathManager.java
│   │               ├── TestBase.java
│   │               └── WaitUtil.java
│   └── test/
│       └── java/
│           └── automation/
│               └── ctflearn/
│                   ├── mobile/
│                   │   └── TradingAppPortfolioTest.java
│                   └── web/
│                       └── CTFLearnChallengeCreationTest.java
├── ci-scripts/
│   └── Jenkinsfile
├── parameters/
│   ├── config.properties
│   ├── dev.properties
│   ├── qa.properties
│   └── staging.properties
├── testdata/
│   ├── jsonfile/
│   │   └── CTFLearn.json
│   └── pdffile/
│       └── ex.pdf
├── pom.xml
├── mobile-tests.xml
├── testing.xml
└── README.md
```

## Features

- **Multi-platform Testing**: Support for both web and mobile testing
- **Page Object Model**: Modular and maintainable test structure
- **Cross-browser Testing**: Support for Chrome, Firefox, Edge, and Safari
- **Mobile Device Testing**: Android and iOS support via Appium
- **Configuration Management**: Environment-specific configuration files
- **CI/CD Integration**: Jenkins pipeline for automated test execution
- **Logging**: Comprehensive logging with different log levels
- **Reporting**: TestNG and custom HTML reports
- **Screenshot Capture**: Automatic screenshot capture on test failure
- **Data-driven Testing**: Support for JSON and other data formats

## Technologies

- Java 17
- Selenium WebDriver 4.x
- Appium 9.x
- TestNG
- Maven
- Jenkins
- Log4j
- JSON Simple
- WebDriverManager

## Setup and Installation

### Prerequisites

- Java JDK 17 or higher
- Maven 3.8.x or higher
- Node.js and npm (for Appium)
- Appium Server
- Android SDK (for mobile testing)
- Xcode (for iOS testing, macOS only)

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/cbtw_automation.git
   cd cbtw_automation
   ```

2. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

3. Install and configure Appium (for mobile testing):
   ```bash
   npm install -g appium
   appium driver install uiautomator2
   appium driver install xcuitest
   ```

## Configuration

The framework uses multiple configuration files:

- `config.properties`: Default configuration
- `dev.properties`: Development environment configuration
- `qa.properties`: QA environment configuration
- `staging.properties`: Staging environment configuration

### Web Configuration

```properties
web.url=https://ctflearn.com
web.username=your-username
web.password=your-password
web.browser=chrome
web.headless=false
web.timeout=30
```

### Mobile Configuration

```properties
appium.platform=android
appium.remote=false
appium.remote.url=http://localhost:4723/wd/hub
appium.android.device=device-id
appium.android.version=13
appium.android.app.package=com.exness.android.pa
```

## Running Tests

### Web Tests

```bash
mvn test -Dsuite=web-tests -Denv=qa
```

### Mobile Tests

```bash
mvn test -Dsuite=mobile-tests -Denv=qa
```

### Specific Test Class

```bash
mvn test -Dtest=CTFLearnChallengeCreationTest -Denv=qa
```

## CI/CD Integration

The project includes a Jenkinsfile for CI/CD integration. The pipeline includes the following stages:

1. Checkout
2. Build
3. Test
4. Generate Allure Report

### Jenkins Parameters

- `ENVIRONMENT`: Test environment (dev, qa, staging)
- `TEST_SUITE`: Test suite to run (web-tests, mobile-tests, regression)
- `HEADLESS`: Run tests in headless mode

## Project Components

### Core Utilities

- **ConfigLoader**: Loads configuration properties
- **LogUtil**: Handles logging across the framework
- **Element**: Provides wrapper methods for WebElement interactions
- **WaitUtil**: Contains wait utilities for synchronization
- **AssertHelper**: Custom assertion methods
- **TestBase**: Base class for all test classes

### Web Components

- **WebDriverFactory**: Creates and manages WebDriver instances
- **BaseWebPage**: Base class for all web page objects

### Mobile Components

- **AppiumDriverManager**: Creates and manages Appium driver instances
- **AppLauncher**: Launches mobile applications
- **BaseMobilePage**: Base class for all mobile page objects
- **Mobile**: Annotation for marking mobile tests

### Page Objects

#### Web Pages

- **LoginPage**: Handles login functionality
- **HomePage**: Represents the home page
- **ChallengesPage**: Lists available challenges
- **CreateChallengePage**: Creates new challenges
- **ChallengeHomePage**: Displays challenge details

#### Mobile Screens

- **LoginScreen**: Handles mobile app login
- **TradeScreen**: Represents trading functionality
- **ProfileScreen**: User profile management

### Data Objects

- **CTFLearnChallenge**: Represents a CTF challenge

## Contributing

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature/my-new-feature`
5. Submit a pull request
