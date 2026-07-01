package tests;

import base.BaseTest;
import org.testng.annotations.Test;

public class RecruitmentTest extends BaseTest {

    @Test(description = "TC07 - Recruitment menu should open the Candidates page")
    public void testRecruitmentPageOpens() {
        loginAsAdmin();
        navigateToMenuAndVerify("Recruitment", "Candidates");
    }
}
