package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementHandler {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ElementHandler(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Waits for page to be fully loaded
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Clicks on an element using different strategies if the regular click fails
     * @param element The web element to click
     */
    public void clickElement(WebElement element) {
        try {
            // First try regular click
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            try {
                // Try scrolling to element and clicking
                js.executeScript("arguments[0].scrollIntoView(true);", element);
                Thread.sleep(500); // Small pause after scroll
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            } catch (Exception e2) {
                try {
                    // Force click using JavaScript
                    js.executeScript("arguments[0].click();", element);
                } catch (Exception e3) {
                    throw new ElementClickInterceptedException("Failed to click element after multiple attempts");
                }
            }
        }
    }

    /**
     * Clicks on an element using a CSS selector
     * @param cssSelector The CSS selector to find the element
     */
    public void clickElementBySelector(String cssSelector) {
        try {
            waitForPageLoad();
            // First wait for presence
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
            // Then wait for visibility
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
            clickElement(element);
        } catch (TimeoutException e) {
            // Try to find element in any frame
            boolean found = false;
            try {
                driver.switchTo().defaultContent();
                java.util.List<WebElement> frames = driver.findElements(By.tagName("iframe"));
                for (WebElement frame : frames) {
                    try {
                        driver.switchTo().frame(frame);
                        WebElement element = driver.findElement(By.cssSelector(cssSelector));
                        if (element.isDisplayed()) {
                            clickElement(element);
                            found = true;
                            break;
                        }
                    } catch (Exception frameEx) {
                        driver.switchTo().defaultContent();
                    }
                }
            } finally {
                driver.switchTo().defaultContent();
            }
            if (!found) {
                throw new NoSuchElementException("Element not found with selector: " + cssSelector);
            }
        }
    }

    /**
     * Makes an element visible by removing 'display: none' style if present
     * @param element The web element to make visible
     */
    public void makeElementVisible(WebElement element) {
        js.executeScript("arguments[0].style.display='block';", element);
    }
}
