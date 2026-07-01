package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class TimeTest extends BaseTest {

    @Test(description = "TC05 - Time menu should open the Time page")
    public void testTimePageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("Time", "Time");
    }
}
