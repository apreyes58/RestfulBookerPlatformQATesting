package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateRoomSteps {
    WebDriver driver;
    HomePage home;
    AdminLoginPage adminLogin;
    AdminRoomsPage adminRooms;

    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        home = new HomePage(driver);
    }

    public void tearDown() {
        driver.quit();
    }

    @Given("User goes to {string}")
    public void userGoesToUrl(String url) {
        setUp();
        home.getToHomePage(url);
        System.out.println("User enters website.");
    }

    @And("User clicks to go to Admin Page")
    public void userClicksToGoToAdminPage() {
        adminLogin = home.getToAdmin();
        System.out.println("User goes to admin page.");
    }

    @When("User logs in as an Admin using {string} and {string}")
    public void userLogsInAsAnAdminUsingUsernameAndPassword(String username, String password) throws URISyntaxException {
        adminLogin.loginUser(username, password);
        adminRooms = adminLogin.submitUser();
        System.out.println("User logs in as admin with given username and password");
    }

    @Then("User creates a room with {string}, {string}, {string}, {int}, {string}, {string}")
    public void userCreatesARoomWithIdTypeAccessiblePriceDetails(String name, String type, String accessible, int price, String feat, String expected) {
        List<String> features = Arrays.asList(feat.split("\\s*,\\s*"));
        adminRooms.createRoom(name, type, accessible, price, features);
        String result = adminRooms.verifyRoom(name, type, accessible, price, features);
        if (expected.equals(result))
            System.out.println("Test passed!");
        else
            System.out.println("Test failed!");
        Assert.assertEquals(expected, result);
        tearDown();
    }
}
