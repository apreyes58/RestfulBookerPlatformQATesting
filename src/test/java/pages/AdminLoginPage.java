package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URISyntaxException;
import java.time.Duration;

public class AdminLoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By user = By.xpath("//input[@id = 'username']");
    private By pass = By.xpath("//input[@id = 'password']");
    private By login = By.xpath("//button[@id = 'doLogin']");

    public AdminLoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void loginUser(String username, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(login));
        driver.findElement(user).sendKeys(username);
        driver.findElement(pass).sendKeys(password);
    }

    public AdminRoomsPage submitUser() throws URISyntaxException {
        driver.findElement(login).click();
        return new AdminRoomsPage(driver);
    }

}
