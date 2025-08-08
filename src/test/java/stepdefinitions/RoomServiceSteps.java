package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.poi.ss.usermodel.Workbook;
import services.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class RoomServiceSteps {
    RoomService service = new RoomService();
    Workbook workbook;

    @Given("Data is given through text file {string}")
    public void dataIsGivenThroughTextFileTxt(String file) {
        throw new PendingException();
    }

    @Given("Data is given through excel file {string}")
    public void dataIsGivenThroughExcelFileExcel(String file) throws IOException {
        workbook = service.getExcelData(file);
    }

    @And("Data is read and room posted")
    public void dataIsReadAndRoomPosted() throws IOException {
        service.postRoom(workbook);
    }

    @Then("Data is processed and room given")
    public void dataIsProcessedAndRoomGiven() throws URISyntaxException, IOException {
        service.getRoom(workbook);
    }
}
