package services;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class RoomService {
    private final String BASE = "http://localhost:3001/";
    private Response response;

    public RoomService() {
        RestAssured.baseURI = BASE;
    }

    public Workbook getExcelData(String file) throws IOException {
        File excelFile;
        FileInputStream input = null;
        Workbook workbook = null;
        try {
            excelFile = new File(file);
            input = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(input);
        } catch (IOException e) {
            System.out.println("File " + " not found.");
            throw new RuntimeException(e);
        }
        input.close();

        return workbook;
    }

    public List<String> getTextData(String textFile) {
        List<String> items = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(textFile));
            String line;
            while ((line = br.readLine()) != null)
                items.add(line.trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public void postRoom(Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheet("Sheet1");
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/reports/PostRoomReport.html");
        extent.attachReporter(spark);

        int expected = 0;
        Map<String, Object> request;
        List<String> features;
        ObjectMapper mapper = new ObjectMapper();
        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "admin", "password", "password"))
                .post("http://localhost:3004/auth/login");

        String token = loginResponse.getCookie("token");
        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;
            request = new HashMap<>();
            ExtentTest test = extent.createTest("Test " + row).assignCategory("Creating rooms using POST.");
            for (Cell cell : row) {
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
                    case 8:
                        expected = (int) cell.getNumericCellValue();
                        break;
                }
            }

            String jsonBody = mapper.writeValueAsString(request);

            response = given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .cookie("token", token)
                    .log().all()
                    .body(jsonBody)
                    .post("room/").then().extract().response();
            System.out.println("Row " + row.getRowNum() + " response: " + response.asString());
            if (response.getStatusCode() == expected && response.getStatusCode() == 201) {
                test.pass("Success! Updated database.");
            } else if (response.getStatusCode() == expected && response.getStatusCode() == 400) {
                test.pass("Success! Rejected node values.").
                        log(Status.PASS, "Reason: " + response.jsonPath().getString("fieldErrors[0]"));
            } else {
                Assert.fail("Test case " + row.getRowNum() + " failed.");
            }
        }
        workbook.close();
        extent.flush();
    }

    public void getRoom(Workbook workbook) throws IOException, URISyntaxException {
        Sheet sheet = workbook.getSheet("Sheet1");
        RequestSpecification rs = given();
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/reports/GetRoomReport.html");
        extent.attachReporter(spark);

        int cellNum;
        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;
            ExtentTest test = extent.createTest("Test " + row).assignCategory("GET room service.");
            boolean pass = true;
            boolean expected = true;
            for (Cell cell : row) {
                cellNum = cell.getColumnIndex();
                if (cellNum == 0) {
                    response = rs.get(new URI("room/" + (int) cell.getNumericCellValue()));
                    if ((int) response.then().extract().path("roomid") == (int) cell.getNumericCellValue())
                        test.log(Status.PASS, "ID found");
                    else {
                        test.log(Status.FAIL, "Id not found");
                        pass = false;
                    }
                } else if (cellNum == 1) {
                    if (response.then().extract().path("roomName").equals(cell.getStringCellValue()))
                        test.log(Status.PASS, "Name found");
                    else {
                        test.log(Status.FAIL, "Name not found");
                        pass = false;
                    }
                } else if (cellNum == 2) {
                    if (response.then().extract().path("type").equals(cell.getStringCellValue()))
                        test.log(Status.PASS, "Type found");
                    else {
                        test.log(Status.FAIL, "Type not found");
                        pass = false;
                    }
                } else if (cellNum == 3) {
                    if ((int) response.then().extract().path("roomPrice") == (int) cell.getNumericCellValue())
                        test.log(Status.PASS, "Price found");
                    else {
                        test.log(Status.FAIL, "Price not found");
                        pass = false;
                    }
                } else if (cellNum == 4)
                    expected = cell.getBooleanCellValue();
            }
            if (pass == expected)
                test.pass("Test " + row.getRowNum() + " passed");
            else
                test.fail("Test " + row.getRowNum() + " failed");
        }
        workbook.close();
        extent.flush();
    }
}
