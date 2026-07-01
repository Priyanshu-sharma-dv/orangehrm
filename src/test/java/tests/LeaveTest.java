package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class LeaveTest extends BaseTest {

    @Test(description = "TC06 - Leave menu should open the Leave List page")
    public void testLeavePageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("Leave", "Leave List");
    }
}
