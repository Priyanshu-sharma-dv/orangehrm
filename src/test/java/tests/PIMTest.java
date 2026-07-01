package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class PIMTest extends BaseTest {

    @Test(description = "TC04 - PIM menu should open the Employee Information page")
    public void testPimPageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("PIM", "Employee Information");
    }
}
