package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class IndexPage {
    private final WebDriver driver;
    private final String startURL = "http://rocketmiles.com";

    @FindBy(linkText = "Contact Us")
    private WebElement contactUs;

    @FindBy(name = "locationSearch")
    private WebElement locationSearch;

    @FindBy(name = "programAutosuggest")
    private WebElement programList;

    @FindBy(name = "username")
    private WebElement emailInputBox;

    private WebElement signInOrJoin;
    private WebElement checkInDate;
    private WebElement checkOutDate;
    private static List<WebElement> dropdownBoxes;
    private WebElement searchButton;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
        driver.get(startURL);
        PageFactory.initElements(driver, this);

        searchButton = driver.findElement(By.cssSelector("*[translate='index_page.searchForm.submit.label']"));
        dropdownBoxes = driver.findElements(By.cssSelector("*[class='btn dropdown-toggle form-control']"));
        signInOrJoin = driver.findElement(By.cssSelector("*[translate='index_page.signIn.label']"));
        checkInDate = driver.findElement(By.cssSelector("*[placeholder='Check in']"));
        checkOutDate = driver.findElement(By.cssSelector("*[placeholder='Check out']"));
    }

    public IndexPage clickSignInOrJoin() {
        signInOrJoin.click();
        return this;
    }

    public IndexPage changeNumGuests(int newNumGuests) {
        dropdownBoxes.get(0).click();
        WebElement numGuestsDropdown = driver.findElement(By.partialLinkText(Integer.toString(newNumGuests) + " Guest"));
        numGuestsDropdown.click();
        return this;
    }

    public IndexPage changeNumRooms(int newNumRooms) {
        dropdownBoxes.get(1).click();
        WebElement numRoomsDropdown = driver.findElement(By.partialLinkText(Integer.toString(newNumRooms) + " Room"));
        numRoomsDropdown.click();
        return this;
    }

    public SearchResultsPage clickSearchButton() {
        searchButton.click();
        return new SearchResultsPage(driver);
    }

    public IndexPage clickSearchButtonStayOnIndex() {
        searchButton.click();
        return this;
    }

    public IndexPage inputLocation(String location) {
        locationSearch.click();
        locationSearch.sendKeys(location);

        WebElement resultListOption = driver.findElement(By.partialLinkText(location));
        resultListOption.click();
        return this;
    }

    public IndexPage inputReward(String reward) {
        programList.click();
        programList.sendKeys(reward);

        WebElement rewardProgramListOption = driver.findElement(By.partialLinkText(reward));
        rewardProgramListOption.click();
        return this;
    }

    public boolean doesUnknownLocationPopupDisplay() {
        WebElement popup = driver.findElement(By.cssSelector("*[class = 'popover-content ng-binding']"));
        return popup.isDisplayed();
    }

    public boolean doesRewardWarningPopupDisplay() {
        WebElement popup = driver.findElement(By.cssSelector("*[class='popover-content']"));
        return popup.isDisplayed();
    }

    public IndexPage signInInfo(String username, String password) {
        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement signInButton = driver.findElement(By.cssSelector("*[value='Sign In']"));

        usernameInput.click();
        usernameInput.sendKeys(username);
        passwordInput.click();
        passwordInput.sendKeys(password);
        signInButton.click();
        return this;
    }

    public Boolean doesAccountOptionDisplay() {
        try {
            WebElement account = driver.findElement(By.cssSelector("*[class='hidden-xs']"));
            return account.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean doesAccountAlreadyExistsErrorDisplay() {
        WebElement error = driver.findElement(By.cssSelector("*[class = 'clearfix alert alert-error ng-scope']"));
        return error.isDisplayed();
    }

    public IndexPage clickSignUpButton() {
        WebElement signUpButton = driver.findElement(By.cssSelector("*[class='btn btn-default ng-binding']"));
        signUpButton.click();
        return this;
    }

    public IndexPage enterNewUserInfo(String username, String password, String firstName, String lastName) {
        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement confirmPassword = driver.findElement(By.name("confirmPassword"));
        WebElement firstNameInput = driver.findElement(By.name("firstName"));
        WebElement lastNameInput = driver.findElement(By.name("lastName"));
        WebElement signUpButton = driver.findElement(By.cssSelector("*[class='col-xs-12 rm-btn-orange login-button rm-animate-fade']"));

        usernameInput.click();
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        confirmPassword.sendKeys(password);
        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
        signUpButton.click();
        return this;
    }

    public IndexPage changeCheckInDate(int monthOffset, int checkInDay) {
        checkInDate.click();
        WebElement nextMonthButton = driver.findElement(By.cssSelector("*[title='Next']"));
        for(int i = 0; i < monthOffset; i++) {
            nextMonthButton.click();
        }
        List<WebElement> daysOfMonth = driver.findElements(By.cssSelector("*[href='#'"));
        daysOfMonth.get(checkInDay-1).click();
        return this;
    }

    public IndexPage changeCheckOutDate(int monthOffset, int checkOutDay) {
        checkOutDate.click();
        if(monthOffset != 0) {
            WebElement nextMonthButton = driver.findElement(By.cssSelector("*[title='Next']"));
            for(int i = 0; i < monthOffset; i++) {
                nextMonthButton.click();
            }
        }
        List<WebElement> daysOfMonth = driver.findElements(By.cssSelector("*[href='#']"));
        daysOfMonth.get(checkOutDay).click();
        return this;
    }

    public String getCheckInDate() {
        return checkInDate.getText();
    }

    public String getCheckOutDate() {
        return checkOutDate.getText();
    }
}
