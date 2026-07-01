package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddUserTest extends BaseTest {

    @Test(description = "TC03 - Admin should be able to add a new system user")
    public void testAddUserSuccess() {
        loginAsAdmin();

        // Navigate to Admin > Add User
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[normalize-space()='Admin']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='User Management']")));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Add']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Add User']")));

        // User Role dropdown
        driver.findElement(By.xpath("//label[text()='User Role']/following::div[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='ESS']"))).click();

        // Employee Name typeahead - unique-ish, pick first suggestion
        WebElement employeeNameField = driver.findElement(
                By.xpath("//input[@placeholder='Type for hints...']"));
        employeeNameField.sendKeys("Sanjay");
        WebElement firstOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option'][1]")));
        firstOption.click();

        // Status dropdown
        driver.findElement(By.xpath("//label[text()='Status']/following::div[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Enabled']"))).click();

        // Unique username so repeated test runs don't collide
        String uniqueUsername = "sanjay_" + System.currentTimeMillis();

        driver.findElement(By.xpath("//label[text()='Username']/following::input[1]"))
                .sendKeys(uniqueUsername);
        driver.findElement(By.xpath("//label[text()='Password']/following::input[1]"))
                .sendKeys("Test@1234");
        driver.findElement(By.xpath("//label[text()='Confirm Password']/following::input[1]"))
                .sendKeys("Test@1234");

        driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();

        // Successful save redirects back to the System Users list
        WebElement usersHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='System Users']")));
        Assert.assertTrue(usersHeading.isDisplayed(), "Did not return to System Users list after save");

        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]")));
        Assert.assertTrue(toast.isDisplayed(), "Success toast was not shown after adding the user");
    }
}
