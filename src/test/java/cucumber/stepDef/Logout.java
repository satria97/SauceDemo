package cucumber.stepDef;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Logout {
    WebDriver driver;
    String baseUrl = "https://www.saucedemo.com/";

    @Given("User is already logged in application")
    public void user_is_already_logged_in_application() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opt = new ChromeOptions();
        opt.setHeadless(false);

        String fileDir = System.getProperty("user.dir") + "/src/test/data/test-data.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileDir))) { //try read csv data
            String[] nextline;
            while ((nextline = reader.readNext()) != null) { // loop for all row data in csv
                String username = nextline[0]; // read column 1 for email
                String password = nextline[1]; // read column 2 for password
                String status = nextline[2]; // read column 3 for expected login status

                driver = new ChromeDriver(opt);
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
                driver.manage().window().maximize();
                driver.get(baseUrl);

                driver.findElement(By.id("user-name")).sendKeys(username);
                driver.findElement(By.id("password")).sendKeys(password);
                driver.findElement(By.xpath("//input[@type='submit']")).click();

                if (status.equals("success")) {
                    String dashboard = driver.findElement(By.xpath("//div[contains(text(), 'Swag Labs')]")).getText();
                    Assert.assertEquals(dashboard, "Swag Labs");
                } else {
                    String errorLogin = driver.findElement(By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3")).getText();
                    Assert.assertEquals(errorLogin, "Epic sadface: Username and password do not match any user in this service");
                }
            }
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @When("User click burger menu")
    public void user_click_burger_menu() {
        driver.findElement(By.id("react-burger-menu-btn")).click();
    }

    @Then("User click logout menu")
    public void user_click_logout_menu() {
        driver.findElement(By.id("logout_sidebar_link")).click();
    }

    @Then("User verify status logout")
    public void user_verify_status_logout() {
        String status = "success";
        if (status.equals("failed")) {
            String dashboard = driver.findElement(By.xpath("//span[contains(text(), 'Products')]")).getText();
            Assert.assertEquals(dashboard, "Products");
        } else {
            WebElement loginForm = driver.findElement(By.id("login_button_container"));
            Assert.assertTrue(loginForm.isDisplayed());
        }
        driver.close();
    }
}
