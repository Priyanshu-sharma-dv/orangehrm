package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Common setup/teardown + reusable helpers for all OrangeHRM UI tests.
 * Centralizing this removes the duplicated boilerplate (and duplicate
 * class names / Thread.sleep calls) that existed in the original scripts.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/";
    protected static final String VALID_USERNAME = "Admin";
    protected static final String VALID_PASSWORD = "admin123";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Logs in with the given credentials. Does not assert success. */
    protected void login(String username, String password) {
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.clear();
        usernameField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    /** Logs in with the standard valid admin credentials and asserts the dashboard loaded. */
    protected void loginAsAdmin() {
        login(VALID_USERNAME, VALID_PASSWORD);
        WebElement dashboardHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));
        Assert.assertTrue(dashboardHeading.isDisplayed(), "Dashboard heading was not displayed after login");
    }

    /**
     * Clicks a left-nav top-level menu item (e.g. "Admin", "PIM", "Leave", "Time",
     * "Recruitment", "My Info", "Performance") and asserts the resulting page
     * header matches what's expected.
     */
    protected void navigateToMenuAndVerify(String menuName, String expectedHeading) {
        WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[normalize-space()='" + menuName + "']")));
        menuItem.click();

        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[normalize-space()='" + expectedHeading + "']")));
        Assert.assertTrue(heading.isDisplayed(),
                "Expected heading '" + expectedHeading + "' not shown after clicking '" + menuName + "'");
    }
}
