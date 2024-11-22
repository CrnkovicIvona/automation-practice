package part1;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import part1.utils.CsvReader;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class FirstSeleniumTest {
    WebDriver driver;
    private static List<TestResult> testResults = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static TestResult currentTest;
    
    private static class TestResult {
        String testName;
        String status;
        String timestamp;
        String duration;
        String errorMessage;
        // Test data fields
        String firstName;
        String lastName;
        String email;
        String password;
        String dateOfBirth;
        String orderNumber;
        String orderDate;
        String orderPrice;
        
        TestResult(String testName, String status, String duration, String errorMessage) {
            this.testName = testName;
            this.status = status;
            this.timestamp = dateFormat.format(new Date());
            this.duration = duration;
            this.errorMessage = errorMessage;
            // Initialize test data fields
            this.firstName = "";
            this.lastName = "";
            this.email = "";
            this.password = "";
            this.dateOfBirth = "";
            this.orderNumber = "";
            this.orderDate = "";
            this.orderPrice = "";
        }
        
        void setTestData(String firstName, String lastName, String email, String password, 
                        String dateOfBirth, String orderNumber, String orderDate, String orderPrice) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.dateOfBirth = dateOfBirth;
            this.orderNumber = orderNumber;
            this.orderDate = orderDate;
            this.orderPrice = orderPrice;
        }
    }
    
    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://www.automationpractice.pl/index.php");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        currentTest = new TestResult(
            result.getMethod().getMethodName(),
            "RUNNING",
            "0.0",
            ""
        );
    }
    
    public void updateTestData(String firstName, String lastName, String email, String password, 
                             String dateOfBirth, String orderNumber, String orderDate, String orderPrice) {
        if (currentTest != null) {
            currentTest.setTestData(firstName, lastName, email, password, dateOfBirth, 
                                  orderNumber, orderDate, orderPrice);
        }
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String durationStr = String.format("%.2f seconds", duration / 1000.0);
        
        String status;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "PASSED";
                break;
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED";
                break;
            default:
                status = "UNKNOWN";
                break;
        }
        
        String errorMsg = "";
        if (result.getStatus() == ITestResult.FAILURE && result.getThrowable() != null) {
            errorMsg = result.getThrowable().getMessage();
        }
        
        currentTest.status = status;
        currentTest.duration = durationStr;
        currentTest.errorMessage = errorMsg;
        testResults.add(currentTest);
    }
    
    @AfterSuite
    public void generateTestReport() {
        try {
            File reportsDir = new File("test-reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdir();
            }
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = "test-reports/test_report_" + timestamp + ".html";
            
            try (PrintWriter writer = new PrintWriter(reportPath)) {
                // Write HTML header with enhanced styling
                writer.println("<!DOCTYPE html>");
                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>Test Execution Report</title>");
                writer.println("<style>");
                writer.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
                writer.println("h1 { color: #333; text-align: center; }");
                writer.println("table { border-collapse: collapse; width: 100%; background-color: white; box-shadow: 0 1px 3px rgba(0,0,0,0.2); }");
                writer.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
                writer.println("th { background-color: #4CAF50; color: white; }");
                writer.println(".passed { color: #4CAF50; font-weight: bold; }");
                writer.println(".failed { color: #f44336; font-weight: bold; }");
                writer.println(".skipped { color: #ff9800; font-weight: bold; }");
                writer.println(".summary { margin: 20px 0; padding: 20px; background-color: white; border-radius: 4px; box-shadow: 0 1px 3px rgba(0,0,0,0.2); }");
                writer.println(".test-data { background-color: #f8f9fa; }");
                writer.println("</style>");
                writer.println("</head>");
                writer.println("<body>");
                
                // Report header
                writer.println("<h1>Test Execution Report</h1>");
                writer.println("<div class='summary'>");
                writer.println("<p><strong>Execution Date:</strong> " + dateFormat.format(new Date()) + "</p>");
                writer.println("<p><strong>Total Tests:</strong> " + testResults.size() + "</p>");
                
                // Calculate summary
                long passed = testResults.stream().filter(r -> r.status.equals("PASSED")).count();
                long failed = testResults.stream().filter(r -> r.status.equals("FAILED")).count();
                long skipped = testResults.stream().filter(r -> r.status.equals("SKIPPED")).count();
                
                writer.println("<p><strong>Passed:</strong> <span class='passed'>" + passed + "</span></p>");
                writer.println("<p><strong>Failed:</strong> <span class='failed'>" + failed + "</span></p>");
                writer.println("<p><strong>Skipped:</strong> <span class='skipped'>" + skipped + "</span></p>");
                writer.println("</div>");
                
                // Enhanced test results table with test data
                writer.println("<table>");
                writer.println("<tr>");
                writer.println("<th>Test Name</th>");
                writer.println("<th>Status</th>");
                writer.println("<th>Timestamp</th>");
                writer.println("<th>Duration</th>");
                writer.println("<th>Test Data</th>");
                writer.println("<th>Error Message</th>");
                writer.println("</tr>");
                
                for (TestResult result : testResults) {
                    writer.println("<tr>");
                    writer.println("<td>" + result.testName + "</td>");
                    writer.println("<td class='" + result.status.toLowerCase() + "'>" + result.status + "</td>");
                    writer.println("<td>" + result.timestamp + "</td>");
                    writer.println("<td>" + result.duration + "</td>");
                    
                    // Test Data column with detailed information
                    writer.println("<td class='test-data'>");
                    if (!result.firstName.isEmpty() || !result.email.isEmpty() || !result.orderNumber.isEmpty()) {
                        writer.println("<strong>User Details:</strong><br>");
                        if (!result.firstName.isEmpty()) writer.println("First Name: " + result.firstName + "<br>");
                        if (!result.lastName.isEmpty()) writer.println("Last Name: " + result.lastName + "<br>");
                        if (!result.email.isEmpty()) writer.println("Email: " + result.email + "<br>");
                        if (!result.dateOfBirth.isEmpty()) writer.println("Date of Birth: " + result.dateOfBirth + "<br>");
                        
                        if (!result.orderNumber.isEmpty()) {
                            writer.println("<br><strong>Order Details:</strong><br>");
                            writer.println("Order Number: " + result.orderNumber + "<br>");
                            writer.println("Order Date: " + result.orderDate + "<br>");
                            writer.println("Order Price: " + result.orderPrice);
                        }
                    } else {
                        writer.println("-");
                    }
                    writer.println("</td>");
                    
                    writer.println("<td>" + (result.errorMessage.isEmpty() ? "-" : result.errorMessage) + "</td>");
                    writer.println("</tr>");
                }
                
                writer.println("</table>");
                writer.println("</body>");
                writer.println("</html>");
            }
            
            System.out.println("Test report generated: " + reportPath);
            
        } catch (Exception e) {
            System.err.println("Error generating test report: " + e.getMessage());
        }
    }

    @Test
    public void testRegistration() throws InterruptedException, IOException {
        driver.findElement(By.cssSelector("#header > div.nav > div > div > nav > div.header_user_info")).click();
        String randomEmail = "user" + new Random().nextInt(10000) + "@example.com";
        WebElement emailInputField = driver.findElement(By.id("email_create"));
        emailInputField.sendKeys(randomEmail);
        driver.findElement(By.cssSelector("#SubmitCreate > span")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#id_gender2")).click();
        String[] firstNames = {"Ana", "Marko", "Ivana", "Petar", "Katarina"};
        String randomFirstName = firstNames[new Random().nextInt(firstNames.length)];
        WebElement firstNameInputField = driver.findElement(By.cssSelector("#customer_firstname"));
        firstNameInputField.sendKeys(randomFirstName);
        String[] lastNames = {"Horvat", "Novak", "Kovačić", "Babić", "Sertić"};
        String randomLastName = lastNames[new Random().nextInt(lastNames.length)];
        WebElement lastNameInputField = driver.findElement(By.cssSelector("#customer_lastname"));
        lastNameInputField.sendKeys(randomLastName);
        String randomPassword = "Pass" + new Random().nextInt(10000) + "!@#";
        WebElement passwordInputField = driver.findElement(By.cssSelector("#passwd"));
        passwordInputField.sendKeys(randomPassword);
        int randomDay = new Random().nextInt(31) + 1;
        int randomMonth = new Random().nextInt(12) + 1; //trebalo bi biti riječi!!!!
        int randomYear = 1900 + new Random().nextInt(2025 - 1900);
        try {
            WebElement dropdownDays = driver.findElement(By.cssSelector("#days"));
            Select days = new Select(dropdownDays);
            days.selectByValue(String.valueOf(randomDay));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        try {
            WebElement dropdownMonths = driver.findElement(By.cssSelector("#months"));
            Select months = new Select(dropdownMonths);
            months.selectByValue(String.valueOf(randomMonth));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        try {
            WebElement dropdownYears = driver.findElement(By.cssSelector("#years"));
            Select years = new Select(dropdownYears);
            years.selectByValue(String.valueOf(randomYear));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        File resourcesDir = new File("resources");
        if (!resourcesDir.exists()) {
            resourcesDir.mkdir();
        }
        try (FileWriter writer = new FileWriter("resources/registration_data.csv", true)) {
            writer.append(randomFirstName).append(',')
                 .append(randomLastName).append(',')
                 .append(randomEmail).append(',')
                 .append(randomPassword).append(',')
                 .append(String.valueOf(randomDay)).append(',')
                 .append(String.valueOf(randomMonth)).append(',')
                 .append(String.valueOf(randomYear)).append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#submitAccount > span")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#block_top_menu > ul > li:nth-child(1) > a")).click();
        Thread.sleep(2000);
        WebElement dropdownSort = driver.findElement(By.cssSelector("#selectProductSort"));
        Select sort = new Select(dropdownSort);
        sort.selectByVisibleText("Product Name: A to Z");
        Thread.sleep(2000);

        // 7. Filtriraj po boji: Crna
        driver.findElement(By.cssSelector("#ul_layered_id_attribute_group_3 > li:nth-child(3) > label > a")).click();

        // 8. Namjesti Price range $20 - $30
        Thread.sleep(2000);
        WebElement leftSlider = driver.findElement(By.cssSelector("#layered_price_slider > a:nth-child(2)"));
        Thread.sleep(2000);
        WebElement rightSlider = driver.findElement(By.cssSelector("#layered_price_slider > a:nth-child(3)"));
        Actions actions = new Actions(driver);
        actions.clickAndHold(leftSlider).moveByOffset(30, 0).release().perform();
        actions.clickAndHold(rightSlider).moveByOffset(-153, 0).release().perform();

        // 9. Otvori proizvod Blouse
        WebElement productImage = driver.findElement(By.cssSelector("li.ajax_block_product .product-image-container"));
        Actions action = new Actions(driver);
        actions.moveToElement(productImage).perform();
        Thread.sleep(1000);
        WebElement quickViewButton = driver.findElement(By.cssSelector("a.quick-view"));
        quickViewButton.click();

        Thread.sleep(3000);

        // 10. Promjeni Size na L
        try {
            // Switch to iframe if quick view is in iframe
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".fancybox-iframe")));
            driver.switchTo().frame(iframe);

            // Wait for size dropdown to be present
            WebElement sizeDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("group_1")));
            Select sizeSelect = new Select(sizeDropdown);
            sizeSelect.selectByValue("3"); // Value "3" corresponds to size L

            // Switch back to default content
            driver.switchTo().defaultContent();

            System.out.println("Successfully selected size L");

        } catch (NoSuchElementException e) {
            System.out.println("Size dropdown element not found");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error occurred while selecting size L: " + e.getMessage());
            e.printStackTrace();
        }


        // 11. Add to cart
        try {
            // Wait for Add to Cart button and click it
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Make sure we're still in the iframe
            driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".fancybox-iframe"))));

            // Wait for Add to Cart button and click it
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[name='Submit']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
            System.out.println("11. Clicked Add to Cart button");

            // Switch back to default content to see the success message
            driver.switchTo().defaultContent();

            // 12. Verify success message
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.layer_cart_product h2")));

            String expectedMessage = "Product successfully added to your shopping cart";
            String actualMessage = successMessage.getText().trim();
            Assert.assertTrue(actualMessage.contains(expectedMessage),
                    "Expected success message not found. Actual message: " + actualMessage);
            System.out.println("12. Verified success message: \"" + actualMessage + "\"");

            // 13. Click Continue Shopping button
            WebElement continueShoppingButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("span.continue.btn")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueShoppingButton);
            System.out.println("13. Clicked Continue Shopping button");

            // 14. Wait for the cart overlay to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("#layer_cart")));
            System.out.println("14. Cart overlay closed successfully");

        } catch (Exception e) {
            System.out.println("Error occurred while adding product to cart: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        updateTestData(randomFirstName, randomLastName, randomEmail, randomPassword, 
                       String.valueOf(randomDay) + "," + String.valueOf(randomMonth) + "," + String.valueOf(randomYear), "", "", "");
    }

    @Test(description = "Complete Contact Us form submission", priority = 2)
    public void testContactUsSubmission() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            // 1. Navigate to Contact Us page
            WebElement contactUsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contact-link a")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contactUsLink);
            Thread.sleep(500);

            // Try clicking with JavaScript if normal click fails
            try {
                contactUsLink.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", contactUsLink);
            }

            // Wait for page load and verify we're on the Contact Us page
            wait.until(ExpectedConditions.urlContains("controller=contact"));
            WebElement pageHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Customer service - Contact us')]")));
            Assert.assertTrue(pageHeading.isDisplayed(), "Contact us page heading should be visible");
            System.out.println("1. Successfully opened Contact Us page");

            // 2. Get last registration data from CSV
            String csvPath = Paths.get(System.getProperty("user.dir"), "resources", "registration_data.csv").toString();
            String[] lastRecord = CsvReader.getLastRecord(csvPath);
            String firstName = lastRecord[0];
            String lastName = lastRecord[1];
            String email = lastRecord[2];
            String dateOfBirth = lastRecord[4] + "," + lastRecord[5] + "," + lastRecord[6];
            System.out.println("2. Retrieved user data from CSV:");
            System.out.println("   - First Name: " + firstName);
            System.out.println("   - Last Name: " + lastName);
            System.out.println("   - Email: " + email);
            System.out.println("   - Date of Birth: " + dateOfBirth);

            // 3. Fill out the contact form
            System.out.println("3. Filling out the contact form:");
            
            // Wait for form to be fully loaded
            Thread.sleep(2000);

            // Select Subject Heading using JavaScript
            WebElement subjectSelect = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("id_contact")));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '2'; arguments[0].dispatchEvent(new Event('change')); arguments[0].dispatchEvent(new Event('click'));", 
                subjectSelect
            );
            System.out.println("   - Selected subject heading");

            // Enter email
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", emailField);
            emailField.sendKeys(email);
            System.out.println("   - Entered email");

            // Handle Order Reference
            WebElement orderSelect = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("id_order")));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '0'; arguments[0].dispatchEvent(new Event('change'));", 
                orderSelect
            );
            System.out.println("   - Set order reference");

            // Upload file
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fileUpload")));
            String imagePath = Paths.get(System.getProperty("user.dir"), "resources", "test_image.png").toString();
            // Create a test image file if it doesn't exist
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                try (FileWriter writer = new FileWriter(imageFile)) {
                    writer.write("Test image content");
                }
            }
            fileInput.sendKeys(imagePath);
            System.out.println("4. Attached file: " + new File(imagePath).getName());

            // Enter message
            WebElement messageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("message")));
            String messageText = String.format("User Information:\nFirst Name: %s\nLast Name: %s\nEmail: %s\nDate of Birth: %s\nTest Reference: TEST123", 
                firstName, lastName, email, dateOfBirth);
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", messageField, messageText);
            System.out.println("5. Entered message with user details");

            // Ensure form is ready for submission
            Thread.sleep(1000);

            // Click Send button using JavaScript
            WebElement sendButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submitMessage")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendButton);
            System.out.println("6. Clicked Send button");

            // Wait for and verify success message
            WebElement alertSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert.alert-success")));
            String expectedMessage = "Your message has been successfully sent to our team.";
            String actualMessage = alertSuccess.getText().trim();
            Assert.assertTrue(alertSuccess.isDisplayed(), "Success message should be visible");
            Assert.assertEquals(actualMessage, expectedMessage, "Success message text is incorrect");
            System.out.println("7. Verified success message: \"" + actualMessage + "\"");

        } catch (Exception e) {
            System.err.println("Error in Contact Us test: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Contact Us test failed", e);
        }
    }

    @Test(description = "Navigate to personal information page and verify data", priority = 3)
    public void testNavigateToPersonalInfo() {
        try {
            // 1. Wait for any overlays to disappear
            Thread.sleep(1000);

            // 2. Click on the account link in the navbar
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement accountLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".header_user_info .account")));

            try {
                accountLink.click();
                System.out.println("1. Clicked on the account link");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountLink);
                System.out.println("1. Clicked on the account link using JavaScript");
            }

            // 3. Wait for the my account page to load and click personal info
            wait.until(ExpectedConditions.urlContains("controller=my-account"));
            WebElement personalInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[title='Information']")));

            try {
                personalInfoLink.click();
                System.out.println("2. Clicked on My personal information link");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", personalInfoLink);
                System.out.println("2. Clicked on My personal information link using JavaScript");
            }

            // 4. Wait for personal information page to load
            wait.until(ExpectedConditions.urlContains("controller=identity"));

            // 5. Get the last registration data from CSV
            String csvPath = System.getProperty("user.dir") + "/resources/registration_data.csv";
            String[] lastRegistrationData = CsvReader.getLastRecord(csvPath);
            String expectedFirstName = lastRegistrationData[0];
            String expectedLastName = lastRegistrationData[1];
            String expectedEmail = lastRegistrationData[2];
            String expectedDay = lastRegistrationData[4];
            String expectedMonth = lastRegistrationData[5];
            String expectedYear = lastRegistrationData[6];

            // 6. Get actual values from the form
            String actualFirstName = driver.findElement(By.id("firstname")).getAttribute("value");
            String actualLastName = driver.findElement(By.id("lastname")).getAttribute("value");
            String actualEmail = driver.findElement(By.id("email")).getAttribute("value");
            String actualDay = driver.findElement(By.id("days")).getAttribute("value");
            String actualMonth = driver.findElement(By.id("months")).getAttribute("value");
            String actualYear = driver.findElement(By.id("years")).getAttribute("value");

            // 7. Verify each field matches
            Assert.assertEquals(actualFirstName, expectedFirstName,
                "First name should match registration data");
            System.out.println("3. Verified first name: " + actualFirstName);

            Assert.assertEquals(actualLastName, expectedLastName,
                "Last name should match registration data");
            System.out.println("4. Verified last name: " + actualLastName);

            Assert.assertEquals(actualEmail, expectedEmail,
                "Email should match registration data");
            System.out.println("5. Verified email: " + actualEmail);

            Assert.assertEquals(actualDay, expectedDay,
                "Birth day should match registration data");
            Assert.assertEquals(actualMonth, expectedMonth,
                "Birth month should match registration data");
            Assert.assertEquals(actualYear, expectedYear,
                "Birth year should match registration data");
            System.out.println("6. Verified date of birth: " + actualDay + "/" + actualMonth + "/" + actualYear);

            System.out.println("7. Successfully verified all personal information matches registration data");

        } catch (Exception e) {
            System.err.println("Error in Navigate to Personal Info test: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Navigate to Personal Info test failed", e);
        }
    }

    @Test(description = "Navigate to cart and proceed to checkout", priority = 4)
    public void testCartAndCheckout() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 1. Click on the Cart button
            WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div.shopping_cart > a")));

            try {
                cartButton.click();
                System.out.println("1. Clicked on Cart button");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);
                System.out.println("1. Clicked on Cart button using JavaScript");
            }

            // 2. Click on Proceed to Checkout button
            WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".button.btn.btn-default.standard-checkout")));

            try {
                checkoutButton.click();
                System.out.println("2. Clicked on Proceed to Checkout button");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutButton);
                System.out.println("2. Clicked on Proceed to Checkout button using JavaScript");
            }

            // 3. Fill out the address form
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("address1")))
                .sendKeys("123 Test Street");
            System.out.println("3. Filled out address line 1");

            driver.findElement(By.id("city")).sendKeys("Test City");
            System.out.println("4. Filled out city");

            // Select state (e.g., California)
            Select stateSelect = new Select(driver.findElement(By.id("id_state")));
            stateSelect.selectByVisibleText("California");
            System.out.println("5. Selected state: California");

            driver.findElement(By.id("postcode")).sendKeys("12345");
            System.out.println("6. Filled out zip code");

            // Fill out required phone number
            driver.findElement(By.id("phone_mobile")).sendKeys("1234567890");
            System.out.println("7. Filled out mobile phone");

            // Save the address
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("submitAddress")));
            saveButton.click();
            System.out.println("8. Clicked Save button");

            // Wait for address save and proceed to next step
            wait.until(ExpectedConditions.urlContains("controller=order"));

            // Click the final checkout button
            WebElement finalCheckoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit'][name='processAddress']")));
            finalCheckoutButton.click();
            System.out.println("9. Proceeded to shipping step");

            // Wait for shipping page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cgv")));

            // Check the Terms of Service checkbox
            WebElement termsCheckbox = driver.findElement(By.id("cgv"));
            if (!termsCheckbox.isSelected()) {
                termsCheckbox.click();
                System.out.println("10. Accepted terms of service");
            }

            // Click Proceed to Checkout on shipping page
            WebElement proceedFromShippingButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[name='processCarrier']")));
            proceedFromShippingButton.click();
            System.out.println("11. Proceeded to payment step");

            // Select payment method (bank wire)
            WebElement bankWirePayment = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".bankwire")));
            bankWirePayment.click();
            System.out.println("12. Selected bank wire payment method");

            // Confirm order
            WebElement confirmOrderButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#cart_navigation button[type='submit']")));
            confirmOrderButton.click();
            System.out.println("13. Confirmed order");

            // Verify order confirmation message
            WebElement confirmationMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".alert.alert-success")));
            String actualMessage = confirmationMessage.getText();
            Assert.assertEquals(actualMessage, "Your order on My Shop is complete.");
            System.out.println("14. Verified order completion message: " + actualMessage);

            // Click on View your order history
            WebElement orderHistoryButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.button-exclusive[href*='controller=history']")));
            orderHistoryButton.click();
            System.out.println("15. Navigated to order history");

            // Wait for the order history table to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order-list")));

            // Get the first (most recent) order details
            WebElement firstOrderRow = driver.findElement(By.cssSelector("#order-list tbody tr:first-child"));
            String orderReference = firstOrderRow.findElement(By.cssSelector(".history_link")).getText().trim();
            String orderDate = firstOrderRow.findElement(By.cssSelector(".history_date")).getText().trim();
            String totalAmount = firstOrderRow.findElement(By.cssSelector(".history_price .price")).getText().trim();
            String orderStatus = firstOrderRow.findElement(By.cssSelector(".history_state span")).getText().trim();

            // Click on details button to get product information
            firstOrderRow.findElement(By.cssSelector("a.color-myaccount")).click();
            
            // Wait for details to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order-detail-content")));

            // Get product details
            WebElement productRow = driver.findElement(By.cssSelector("#order-detail-content tbody tr"));
            String productReference = productRow.findElement(By.cssSelector("td:nth-child(1)")).getText().trim();
            String productDetails = productRow.findElement(By.cssSelector("td:nth-child(2)")).getText().trim();
            String quantity = productRow.findElement(By.cssSelector("td:nth-child(3) .order_qte_span")).getText().trim();
            String unitPrice = productRow.findElement(By.cssSelector("td:nth-child(4)")).getText().trim();

            // Get shipping address
            WebElement addressBlock = driver.findElement(By.cssSelector(".address.alternate_item"));
            String shippingAddress = addressBlock.getText().replace("Delivery address (My address)", "").trim();
            
            System.out.println("Debug - Extracted details:");
            System.out.println("Order Reference: " + orderReference);
            System.out.println("Order Date: " + orderDate);
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Status: " + orderStatus);
            System.out.println("Product: " + productDetails);
            System.out.println("Quantity: " + quantity);
            System.out.println("Unit Price: " + unitPrice);
            System.out.println("Shipping Address: " + shippingAddress);
            
            // Create PDF with order details
            Document document = new Document();
            try {
                String fileName = "invoice/order_" + orderReference + ".pdf";
                PdfWriter.getInstance(document, new FileOutputStream(fileName));
                document.open();
                
                // Add content to PDF
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                
                // Title
                document.add(new Paragraph("Order Confirmation", titleFont));
                document.add(new Paragraph("\n"));
                
                // Order Details
                document.add(new Paragraph("Order Details", headerFont));
                document.add(new Paragraph("Order Reference: " + orderReference, normalFont));
                document.add(new Paragraph("Order Date: " + orderDate, normalFont));
                document.add(new Paragraph("Total Amount: " + totalAmount, normalFont));
                document.add(new Paragraph("Status: " + orderStatus, normalFont));
                document.add(new Paragraph("\n"));
                
                // Product Details
                document.add(new Paragraph("Product Information", headerFont));
                document.add(new Paragraph("Reference: " + productReference, normalFont));
                document.add(new Paragraph("Product: " + productDetails, normalFont));
                document.add(new Paragraph("Quantity: " + quantity, normalFont));
                document.add(new Paragraph("Unit Price: " + unitPrice, normalFont));
                document.add(new Paragraph("\n"));
                
                // Shipping Address
                document.add(new Paragraph("Shipping Address", headerFont));
                document.add(new Paragraph(shippingAddress, normalFont));
                
                document.close();
                System.out.println("16. Generated order confirmation PDF: " + fileName);
            } catch (Exception e) {
                System.out.println("Error generating PDF: " + e.getMessage());
                throw new RuntimeException("Failed to generate PDF", e);
            }

            updateTestData("", "", "", "", "", orderReference, orderDate, totalAmount);

        } catch (Exception e) {
            System.err.println("Error in Cart and Checkout test: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Cart and Checkout test failed", e);
        }
    }

}
