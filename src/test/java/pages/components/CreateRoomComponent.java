package pages.components;

import org.apache.commons.lang3.EnumUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreateRoomComponent {

    WebDriverWait wait;
    @FindBy(id = "roomName")
    WebElement roomNumberEl;

    @FindBy(xpath = "//select[@class='form-control' and @id='type']")
    WebElement typeEl;

    @FindBy(xpath = "//select[@class='form-control' and @id='accessible']")
    WebElement accessibleEl;

    @FindBy(id = "roomPrice")
    WebElement roomPriceEl;

    @FindBy(id = "wifiCheckbox")
    WebElement wifiEl;

    @FindBy(id = "tvCheckbox")
    WebElement tvEl;

    @FindBy(id = "radioCheckbox")
    WebElement radioEl;

    @FindBy(id = "refeshCheckbox")
    WebElement refreshmentsEl;

    @FindBy(id = "safeCheckbox")
    WebElement safeEl;

    @FindBy(id = "viewsCheckbox")
    WebElement viewsEl;

    @FindBy(id = "createRoom")
    WebElement createButton;

    @FindBy(xpath = "//p[text()='must be greater than or equal to 1']")
    WebElement alertPrice;

    @FindBy(xpath = "//p[text()='Room name must be set']")
    WebElement alertName;

    private enum types {
        Single, Twin, Double, Family, Suite
    }

    public CreateRoomComponent(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void enterInfo(String number, String type, String accessible, int price, String features) {
        wait.until(ExpectedConditions.elementToBeClickable(createButton));
        if (!number.isEmpty() && !type.isEmpty() && !accessible.isEmpty() && price != 0 && !features.isEmpty()) {
            roomNumberEl.sendKeys(number);
            Select typeSel = new Select(typeEl);
            Select accessibleSel = new Select(accessibleEl);
            if (EnumUtils.isValidEnum(types.class, type))
                typeSel.selectByValue(type);
            else
                throw new IllegalArgumentException("Invalid room type: " + type);
            if (accessible.equals("true") || accessible.equals("false"))
                accessibleSel.selectByValue(accessible);
            else
                throw new IllegalArgumentException("Invalid accessible: " + accessible);
            if (price >= 1 && price <= 999)
                roomPriceEl.sendKeys(Integer.toString(price));
            else
                throw new IllegalArgumentException("Invalid price: " + price);
            if (features.contains("WiFi"))
                wifiEl.click();
            if (features.contains("TV"))
                tvEl.click();
            if (features.contains("Radio"))
                radioEl.click();
            if (features.contains("Refreshments"))
                refreshmentsEl.click();
            if (features.contains("Safe"))
                safeEl.click();
            if (features.contains("Views"))
                viewsEl.click();
        }
    }

    public void clickSubmit() {
        createButton.click();

        try {
            if (!alertName.getText().isEmpty() && !alertPrice.getText().isEmpty())
                throw new IllegalArgumentException("No name and price given");
            else if (!alertPrice.getText().isEmpty())
                throw new IllegalArgumentException("No price given");
            else if (!alertName.getText().isEmpty())
                throw new IllegalArgumentException("No name given");
        } catch (NoSuchElementException ignored) {

        }
    }
}
