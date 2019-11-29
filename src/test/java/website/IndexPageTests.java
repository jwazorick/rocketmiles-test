package website;

import driver.ConfigureWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.IndexPage;

import static junit.framework.TestCase.assertTrue;

public class IndexPageTests {
    private WebDriver driver;
    private IndexPage indexPage;
    private String email = "jwazorick@gmail.com";
    private String password = "RocketMilesTest1";

    @Before
    public void setUp() {
        driver = ConfigureWebDriver.returnChromeWebdriver();
        indexPage = new IndexPage(driver);
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void signInUser() {
        //Sign in a user and verify it shows the account option at the top
        indexPage.clickSignInOrJoin();
        indexPage.signInInfo(email, password);
        assertTrue(indexPage.doesAccountOptionDisplay());
    }

    @Test
    public void signUpWithExistingUser() {
        //Attempt to sign up with an existing user's email address and verify the error popup displays
        indexPage.clickSignInOrJoin();
        indexPage.clickSignUpButton();
        indexPage.enterNewUserInfo(email, password, "Jim", "Wazorick");
        assertTrue(indexPage.doesAccountAlreadyExistsErrorDisplay());
    }

    @Test
    public void searchWithNoCityOrReward() {
        //Without entering in a city or rewards program, click on Search and verify a popup warning displays
        indexPage.clickSearchButtonStayOnIndex();
        assertTrue(indexPage.doesUnknownLocationPopupDisplay());
    }

    @Test
    public void searchWithNoReward() {
        //Enter in a city, but leave the rewards program blank and click search. A popup warning message should display
        indexPage.inputLocation("San Diego, CA");
        indexPage.clickSearchButtonStayOnIndex();
        assertTrue(indexPage.doesRewardWarningPopupDisplay());
    }

    @Test
    public void partialCitySearch() {
        String city = "San Diego, CA";
        String partialCity = "San Di";
        indexPage.inputLocation(partialCity);
        indexPage.inputReward("Amazon.com Gift Card");
        assertTrue(indexPage.getLocation().contains(city));
    }
}
