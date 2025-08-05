package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.poi.ss.usermodel.Workbook;
import services.RoomService;

import java.io.IOException;

public class PostCreateRoomSteps {
    RoomService service = new RoomService();
    Workbook workbook;
    @Given("Data is given through excel file {string}")
    public void dataIsGivenThroughExcelFileExcel(String file) {
        workbook = service.getData(file);
    }

    @And("Data is read")
    public void dataIsRead() throws IOException {
        service.postRoom(workbook);
    }

    @Then("Now check tests if valid")
    public void useDataSetToTestEndpoint() {
    }
}
