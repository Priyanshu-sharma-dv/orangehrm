package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "TC01 - Valid credentials should log the user in and land on Dashboard")
    public void testLoginSuccess() {
        loginAsAdmin();
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"),
                "URL should contain 'dashboard' after a successful login");
    }

    @Test(description = "TC01b - Invalid credentials should show an error and stay on the login page")
    public void testLoginFailure() {
        login(VALID_USERNAME, "wrongPassword123");

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class,'alert-content-text') or contains(text(),'Invalid credentials')]")));

        Assert.assertTrue(error.isDisplayed(), "Invalid credentials error message was not shown");
        Assert.assertTrue(driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().contains("auth"),
                "User should remain on the login/auth page after a failed login");
    }
}
