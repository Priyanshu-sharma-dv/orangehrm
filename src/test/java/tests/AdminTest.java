package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class AdminTest extends BaseTest {

    @Test(description = "TC02 - Admin menu should open the User Management page")
    public void testAdminPageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("Admin", "User Management");
    }
}
