package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSender;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class RoomService {
    private final String BASE = "http://localhost:3001/";

    public RoomService() {
        RestAssured.baseURI = BASE;
    }

    public Workbook getData (String file) {
        File excelFile;
        FileInputStream input;
        Workbook workbook = null;
        try {
            excelFile = new File(file);
            input = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(input);
        }
        catch (IOException e) {
            System.out.println("File " + " not found.");
        }
        
        return workbook;
    }

    public void postRoom(Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheet("Sheet1");
        Map<String, Object> request;
        List<String> features;
        ObjectMapper mapper = new ObjectMapper();
        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "admin", "password", "password"))
                .post("http://localhost:3004/auth/login");

        String token = loginResponse.getCookie("token");
        for (Row row: sheet) {
            request = new HashMap<>();
            if (row.getRowNum() == 0)
                continue;
            for (Cell cell: row) {
                switch (cell.getColumnIndex()) {
                    case 0:
                        request.put("roomid", (int) cell.getNumericCellValue());
                        break;
                    case 1:
//                        System.out.println(cell.getStringCellValue());
                        request.put("roomName", cell.getStringCellValue());
                        break;
                    case 2:
//                        System.out.println(cell.getStringCellValue());
                        request.put("type", cell.getStringCellValue());
                        break;
                    case 3:
//                        System.out.println(cell.getBooleanCellValue());
                        request.put("accessible", cell.getBooleanCellValue());
                        break;
                    case 4:
//                        System.out.println(cell.getStringCellValue());
                        request.put("image", cell.getStringCellValue());
                        break;
                    case 5:
//                        System.out.println(cell.getStringCellValue());
                        request.put("description", cell.getStringCellValue());
                        break;
                    case 6:
                        features = Arrays.asList(cell.getStringCellValue().split("\\|"));
//                        System.out.println(features);
                        request.put("features", features);
                        break;
                    case 7:
//                        System.out.println(cell.getNumericCellValue());
                        request.put("roomPrice", (int) cell.getNumericCellValue());
                        break;
                }
            }
            String jsonBody = mapper.writeValueAsString(request);

            Response post = given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .cookie("token", token)
                    .log().all()
                    .body(jsonBody)
                    .post("room/").then().statusCode(201).extract().response();
            System.out.println("Row " + row.getRowNum() + " response: " + post.asString());
        }

    }

    public void checkRoom() {

    }

}
