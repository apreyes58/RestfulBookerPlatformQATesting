package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private By AdminButton = By.xpath("//a[@class='nav-link' and @href='/admin']");
    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void getToHomePage(String url) {
        driver.get(url);
    }

    public AdminLoginPage getToAdmin() {
        wait.until(ExpectedConditions.textToBe(AdminButton, "Admin"));
        driver.findElement(AdminButton).click();
        return new AdminLoginPage(driver);
    }
}
