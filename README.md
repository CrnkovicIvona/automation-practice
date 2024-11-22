# E-commerce Test Automation Framework

## Overview
This project is a comprehensive test automation framework built for testing an e-commerce website (AutomationPractice). It demonstrates automated end-to-end testing of various e-commerce functionalities including user registration, product browsing, shopping cart management, and order processing.

## Technologies Used
- **Java**: Core programming language
- **JavaScript**: Used for dynamic web elements interaction and validation
- **Selenium WebDriver**: Web automation framework (version 4.26.0)
- **TestNG**: Testing framework for test organization and execution (version 7.10.2)
- **Maven**: Dependency management and build automation
- **iText**: PDF generation and manipulation (version 5.5.13.3)
- **Chrome WebDriver**: Browser automation driver
- **Java CSV**: CSV file handling for data-driven testing


## Features
1. **User Registration**
   - Automated user account creation
   - Random data generation for unique users
   - Form validation testing

2. **Product Management**
   - Product search and selection
   - Size and quantity selection
   - Add to cart functionality

3. **Shopping Cart**
   - Cart management
   - Product quantity updates
   - Cart total verification

4. **Checkout Process**
   - Complete order placement
   - Address verification
   - Payment method selection

5. **Order Management**
   - Order history tracking
   - Order details verification
   - PDF generation of order details

6. **Test Reporting**
   - Detailed HTML test reports
   - Test execution statistics
   - Failure screenshots
   - Test data logging

## Test Data Management
- Dynamic data generation for unique test runs
- CSV file support for data-driven testing
- Random user information generation

## Reporting Features
The framework generates detailed HTML reports including:
- Test execution summary
- Test case status (Pass/Fail/Skip)
- Execution time and duration
- Test data used
- Error messages for failed tests
- Order details and user information

## Prerequisites
- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome browser
- Chrome WebDriver matching your Chrome version

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

3. Update `testng.xml` if needed for specific test suite configuration

4. Run tests:
   ```bash
   mvn test
   ```

## Configuration
- Browser settings can be modified in `FirstSeleniumTest.java`
- Test suite configuration in `testng.xml`
- Maven dependencies in `pom.xml`

## Test Execution
### Running All Tests
```bash
mvn clean test
```

### Running Specific Test Classes
```bash
mvn test -Dtest=FirstSeleniumTest
```

## Reporting
- HTML reports are generated in `test-reports/` directory
- Order PDFs are generated in `invoice/` directory
- Console logs provide real-time execution status

## Best Practices Implemented
1. **Page Object Model**
   - Organized test structure
   - Reusable page elements
   - Maintainable code

2. **Wait Strategies**
   - Explicit waits
   - Dynamic element handling
   - Timeout management

3. **Error Handling**
   - Comprehensive exception management
   - Detailed error logging
   - Recovery mechanisms

4. **Test Data Management**
   - Dynamic data generation
   - Data-driven approach
   - Clean test data

## Demo Recording
The `video` folder contains .mp4 file showing demo recording of the first version of the automation framework.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Author
Ivona Crnković

## Acknowledgments
- Selenium WebDriver documentation
- TestNG documentation
- Maven documentation
- iText PDF library
