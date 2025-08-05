package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/features/postcreateroom.feature"},
glue = {"stepdefinitions"})
public class PostCreateRoomRunner {
}
