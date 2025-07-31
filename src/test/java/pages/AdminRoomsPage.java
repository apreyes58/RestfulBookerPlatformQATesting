package pages;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import pages.components.CreateRoomComponent;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class AdminRoomsPage {

    private final CreateRoomComponent createRoom;
    private final String BASE = "http://localhost:3001/room/";
    private Response response;
    private JsonPath json;

    public AdminRoomsPage(WebDriver driver) throws URISyntaxException {
        createRoom = new CreateRoomComponent(driver);
        json = RestAssured.given().when().get(BASE).then().extract().jsonPath();
    }

    public void createRoom(String number, String type, String accessible, int price, List<String> features) {
        createRoom.enterInfo(number, type, accessible, price, String.valueOf(features));
        createRoom.clickSubmit();
    }

    public String verifyRoom(String number, String type, String accessible, int price, List<String> features) {
        List<Map<String, Object>> rooms = json.getList("rooms");
        for (Map<String, Object> room : rooms) {
            String actualNumber = room.get("roomName").toString();
            String actualType = room.get("type").toString();
            String actualAccessible = room.get("accessible").toString();
            int actualPrice = (int) room.get("roomPrice");
            List<String> actualFeatures = (List<String>) room.get("features");

            if (actualNumber.equals(number) &&
            actualType.equals(type) &&
            actualAccessible.equals(accessible) &&
            actualPrice == price &&
            actualFeatures.containsAll(features) &&
            features.containsAll(actualFeatures)) {
                return "pass";
            }
        }
        return "fail";
    }
}
