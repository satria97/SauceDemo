package cucumber.stepDef;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AddToCart {
    WebDriver driver;
    String baseUrl = "https://www.saucedemo.com/";

    @Given("User is already login")
    public void user_is_already_login() {
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

    @When("User click Add to cart button")
    public void user_click_add_to_cart_button() {
        driver.findElement(By.xpath("//*[@id='add-to-cart-sauce-labs-backpack']")).click();
    }

    @And("User click Cart icon")
    public void user_click_cart_icon() {
        driver.findElement(By.xpath("//*[@id='shopping_cart_container']/a")).click();
    }

    @Then("User verify status the cart")
    public void user_verifies_status_the_cart() {
        String qty = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[1]")).getText();
        if (qty.equals("1")) {
            String removeButton = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
            Assert.assertEquals(removeButton, "Remove");
        } else {
            String removeButton = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
            Assert.assertNotEquals(removeButton, "Remove");
        }
    }
}
