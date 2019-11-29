package website;

import driver.ConfigureWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.IndexPage;
import pages.SearchResultsPage;
import utils.TestConstants;

import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class SearchResultsTests {
    private WebDriver driver;
    private IndexPage indexPage;
    private String location = "San Diego, CA";
    private String reward = "Amazon.com Gift Card";

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
    public void resultsForCorrectParameters() {//
        int numGuests = 3;
        int numRooms = 3;
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        indexPage.changeNumGuests(numGuests);
        indexPage.changeNumRooms(numRooms);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();

        HashMap<String, String> searchParameters = searchResultsPage.getSearchParametersFromDropdown();
        assertTrue(searchParameters.get(TestConstants.SEARCH_LOCATION).contains(location));
        assertTrue(searchParameters.get(TestConstants.SEARCH_REWARD).contains(reward));
        assertTrue(searchParameters.get(TestConstants.SEARCH_GUESTS).contains(Integer.toString(numGuests)));
        assertTrue(searchParameters.get(TestConstants.SEARCH_ROOMS).contains(Integer.toString(numRooms)));
    }

    @Test
    public void changeParametersAndSearch() {
        //After Searching for one cit/rewards combo, search for another combination on the results page
        String newLocation = "Chicago, IL";
        String newReward = "MileagePlus";

        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        searchResultsPage.expandSearchFields();
        searchResultsPage.inputNewLocation(newLocation);
        searchResultsPage.inputNewRewards(newReward);
        SearchResultsPage updatedResultsPage = searchResultsPage.clickSearchButton();

        assertTrue(updatedResultsPage.getSearchParameters().contains(newLocation));
        assertTrue(updatedResultsPage.getSearchParameters().contains(newReward));
    }

    @Test
    public void topOfferDisplayedFirst() {
        //Verify the top offer is displayed as the first hotel after completing a search
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();

        List<WebElement> hotelResults = searchResultsPage.getsearchResultWebElements();
        WebElement topOffer = hotelResults.get(0).findElement(By.cssSelector("*[translate='search_page.top.offer']"));
        assertTrue(topOffer != null);
    }

    @Test
    public void searchSpecificProperty() {
        //Verify that searching for a specific hotel displays only that hotel
        String propertyName = "Westgate Hotel";
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        searchResultsPage.enterPropertyName(propertyName);
        assertTrue(searchResultsPage.getNumberOfHotelsDisplaying() == 1);
        assertTrue(searchResultsPage.doesHotelDisplay(propertyName));
    }

    @Test
    public void changeSortByMethod() {
        //On the results page, change the sort order and verify the sorting is correct
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        searchResultsPage.selectSortBy("Guest Rating");
        List<WebElement> hotelRatings = searchResultsPage.getHotelRatings();

        for(int i = 1; i < hotelRatings.size(); i++) {
            String[] current = hotelRatings.get(i).getText().split("/10");
            String[] previous = hotelRatings.get(i-1).getText().split("/10");
            assertTrue(Double.parseDouble(current[0]) <= Double.parseDouble(previous[0]));
        }
    }

    @Test
    public void searchParametersSameAsIndexPage() {//
        //Verify that the results page displays the search parameters in terms of city and rewards program
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        HashMap<String, String> searchParameters = searchResultsPage.getSearchParametersFromDropdown();
        System.out.print("Location: " + searchParameters.get(TestConstants.SEARCH_LOCATION));
        assertTrue(searchParameters.get(TestConstants.SEARCH_LOCATION).contains(location));
        assertTrue(searchParameters.get(TestConstants.SEARCH_REWARD).contains(reward));
    }

    @Test
    public void changStarRating() {
        //On the results page, set a filter for hotel star rating and verify it filters correctly
        indexPage.inputLocation(location);
        indexPage.inputReward(reward);
        SearchResultsPage searchResultsPage = indexPage.clickSearchButton();
        int numHotelsNoFilter = searchResultsPage.getNumberOfHotelsDisplaying();
        searchResultsPage.changeHotelStarRatingFilter(5);
        int numHotelsFiltered = searchResultsPage.getNumberOfHotelsDisplaying();

        assertTrue(numHotelsFiltered <= numHotelsNoFilter);
        assertTrue(searchResultsPage.confirmHotelsHaveProperStarRating(5));
    }
}
