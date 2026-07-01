package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class MyInfoTest extends BaseTest {

    @Test(description = "TC08 - My Info menu should open the Personal Details page")
    public void testMyInfoPageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("My Info", "Personal Details");
    }
}
