package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestConstants;

import java.util.HashMap;
import java.util.List;

public class SearchResultsPage {
    private final WebDriver driver;

    private static WebElement sortByBox;

    @FindBy(name = "locationSearch")
    private WebElement locationSearchBox;

    @FindBy(name = "programAutosuggest")
    private WebElement dropdownProgramSuggest;

    @FindBy(className = ".value.filter-option")
    private List<WebElement> searchDropdowns;

    private WebElement searchParameters;
    private WebElement propertyName;
    private WebElement searchCheckInDate;
    private WebElement searchCheckOutDate;
    private WebElement searchButton;
    private WebElement starFilterDropdown;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        //Wait until the searching message is no longer visible
        new WebDriverWait(driver, 20).until(ExpectedConditions.invisibilityOfElementLocated(By.className("search-transition")));

        searchParameters = driver.findElement(By.cssSelector("*[translate = 'search_page.search_summary.n_hotels']"));
        propertyName = driver.findElement(By.cssSelector("*[placeholder = 'Property name']"));
        starFilterDropdown = driver.findElement(By.cssSelector("*[class='rating-dropdown ng-binding dropdown-toggle']"));
        sortByBox = driver.findElement(By.cssSelector("*[class='sort-dropdown dropdown-toggle']"));
    }

    public SearchResultsPage selectSortBy(String sortByOption) {
        sortByBox.click();

        WebElement sortByList = driver.findElement(By.linkText(sortByOption));
        sortByList.click();
        return this;
    }

    public String getSearchParameters() {
        return searchParameters.getText();
    }

    public SearchResultsPage enterPropertyName(String property) {
        propertyName.click();
        propertyName.sendKeys(property);
        return this;
    }

    public HashMap<String, String> getSearchParametersFromDropdown() {
        locationSearchBox.click();
        HashMap<String, String> parameters = new HashMap<>();

        searchParameters = driver.findElement(By.cssSelector("*[translate = 'search_page.search_summary.n_hotels']"));
        propertyName = driver.findElement(By.cssSelector("*[placeholder = 'Property name']"));
        searchDropdowns = driver.findElements(By.cssSelector("*[class = 'value filter-option']"));
        searchCheckInDate = driver.findElement(By.xpath("//div[contains(@class, 'checkin booking-date')]/input"));
        searchCheckOutDate = driver.findElement(By.xpath("//div[contains(@class, 'checkout booking-date')]/input"));

        parameters.put(TestConstants.SEARCH_LOCATION, locationSearchBox.getAttribute("value"));
        parameters.put(TestConstants.CHECK_IN_DATE, searchCheckInDate.getText());
        parameters.put(TestConstants.CHECK_OUT_DATE, searchCheckOutDate.getText());
        parameters.put(TestConstants.SEARCH_REWARD, dropdownProgramSuggest.getAttribute("value"));
        parameters.put(TestConstants.SEARCH_GUESTS, searchDropdowns.get(0).getText());
        parameters.put(TestConstants.SEARCH_ROOMS, searchDropdowns.get(1).getText());
        return parameters;
    }

    public SearchResultsPage expandSearchFields() {
        locationSearchBox.click();
        return this;
    }

    public SearchResultsPage inputNewLocation(String location) {
        locationSearchBox.click();
        locationSearchBox.clear();
        locationSearchBox.sendKeys(location);

        WebElement resultListOption = driver.findElement(By.partialLinkText(location));
        resultListOption.click();
        return this;
    }

    public SearchResultsPage inputNewRewards(String reward) {
        dropdownProgramSuggest.click();
        dropdownProgramSuggest.clear();
        dropdownProgramSuggest.sendKeys(reward);

        WebElement rewardProgramListOption = driver.findElement(By.partialLinkText(reward));
        rewardProgramListOption.click();
        return this;
    }

    public SearchResultsPage clickSearchButton() {
        searchButton = driver.findElement(By.cssSelector("*[type='submit']"));
        searchButton.click();
        return new SearchResultsPage(driver);
    }

    public int getNumberOfHotelsDisplaying() {
        List<WebElement> hotelsDisplaying = driver.findElements(By.cssSelector("*[class='hotel-name ng-binding']"));
        return hotelsDisplaying.size();
    }

    public boolean doesHotelDisplay(String hotelName) {
        List<WebElement> hotelsDisplaying = driver.findElements(By.cssSelector("*[class='hotel-name ng-binding']"));
        for(int i = 0; i < hotelsDisplaying.size(); i++) {
            if(hotelsDisplaying.get(i).getText().contains(hotelName)) {
                return true;
            }
        }
        return false;
    }

    public List<WebElement> getsearchResultWebElements() {
        return driver.findElements(By.cssSelector("*[class='mf-search-result-container']"));
    }

    public SearchResultsPage changeHotelStarRatingFilter(int numStars) {
        starFilterDropdown.click();
        WebElement starRating = driver.findElement(By.partialLinkText(Integer.toString(numStars)));
        starRating.click();
        return this;
    }

    public boolean confirmHotelsHaveProperStarRating(int minimumStars) {
        List<WebElement> hotelListings = driver.findElements(By.cssSelector("*[class='hotel-reviews ng-binding']"));
        for(int i=0; i < hotelListings.size(); i++) {
            switch (minimumStars) {
                case 1:
                    //1, 2, 3, 4, 5 stars
                    if(!hotelListings.get(i).getAttribute("innerHTML").contains("1 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("2 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("3 ") ||
                            !hotelListings.get(i).getAttribute("innerHTML").contains("4 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("5 ")) {
                        return false;
                    }
                    break;
                case 2:
                    //2, 3, 4, 5 stars
                    if(!hotelListings.get(i).getAttribute("innerHTML").contains("2 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("3 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("4 ") ||
                            !hotelListings.get(i).getAttribute("innerHTML").contains("5 ")) {
                        return false;
                    }
                    break;
                case 3:
                    //3, 4, 5 stars
                    if(!hotelListings.get(i).getAttribute("innerHTML").contains("3 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("4 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("5 ")) {
                        return false;
                    }
                    break;
                case 4:
                    //4, 5 stars
                    if(!hotelListings.get(i).getAttribute("innerHTML").contains("4 ") || !hotelListings.get(i).getAttribute("innerHTML").contains("5 ")) {
                        return false;
                    }
                    break;
                case 5:
                    //5 stars
                    if(!hotelListings.get(i).getAttribute("innerHTML").contains("5 ")) {
                        //fail
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public List<WebElement> getHotelRatings() {
        return driver.findElements(By.cssSelector("*[translate='search_page.search_result.rating_from_number_reviews']"));
    }
}
