package website;

import driver.ConfigureWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.IndexPage;
import pages.SearchResultsPage;
import utils.TestConstants;

import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

public class SearchingTests {
    private WebDriver driver;
    private IndexPage indexPage;
    private String location = "San Diego, CA";
    private String reward = "Amazon.com Gift Card";

    @Before
    public void setUp() {
        driver = ConfigureWebDriver.returnChromeWebdriver();
        indexPage = new IndexPage(driver);
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void searchWithDefaults() {
        //Search with the default values of check in/out dates, number of guests, and number of rooms
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        assertTrue(searchResultsPage.getSearchParameters().contains(location));
        assertTrue(searchResultsPage.getSearchParameters().contains(reward));
    }

    @Test
    public void searchWithDifferentDates() {
        //Change the dates from the default on the index page and verify they are in the parameters on the results page
        indexPage.changeCheckInDate(1, 1);
        indexPage.changeCheckOutDate(0, 2);
        String checkIn = indexPage.getCheckInDate();
        String checkOut = indexPage.getCheckOutDate();
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        HashMap<String, String> searchParameters = searchResultsPage.getSearchParametersFromDropdown();
        assertTrue(searchParameters.get(TestConstants.CHECK_IN_DATE).contains(checkIn));
        assertTrue(searchParameters.get(TestConstants.CHECK_OUT_DATE).contains(checkOut));
    }

    @Test
    public void searchWithDifferentRooms() {
        //Change the number of rooms needed and verify search parameters on the results page
        indexPage.changeNumRooms(3);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        HashMap<String, String> searchParameters = searchResultsPage.getSearchParametersFromDropdown();
        assertTrue(searchParameters.get(TestConstants.SEARCH_ROOMS).contains(Integer.toString(3)));
    }

    @Test
    public void searchWithDifferentGuests() {
        //Change the number of guests and verify search parameters on the results page
        indexPage.changeNumGuests(3);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        HashMap<String, String> searchParameters = searchResultsPage.getSearchParametersFromDropdown();
        assertTrue(searchParameters.get(TestConstants.SEARCH_GUESTS).contains(Integer.toString(3)));
    }
}
