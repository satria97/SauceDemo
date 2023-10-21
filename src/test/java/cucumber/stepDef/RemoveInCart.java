package cucumber.stepDef;

import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class RemoveInCart {
    WebDriver driver;
    @When("User click Remove button")
    public void user_click_remove_button() {
        driver.findElement(By.xpath("//*[@id='remove-sauce-labs-backpack']")).click();
    }
}
