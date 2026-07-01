package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class PerformanceTest extends BaseTest {

    @Test(description = "TC09 - Performance menu should open the Performance page")
    public void testPerformancePageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("Performance", "Performance");
    }
}
