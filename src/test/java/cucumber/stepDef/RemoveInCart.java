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

public class RemoveInCart {
    WebDriver driver;
    String baseUrl = "https://www.saucedemo.com/";

    @Given("Products is already in the Cart")
    public void products_is_already_in_the_cart() {
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

        driver.findElement(By.xpath("//*[@id='add-to-cart-sauce-labs-backpack']")).click();
        driver.findElement(By.xpath("//*[@id='shopping_cart_container']/a")).click();
        String qty = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[1]")).getText();
        if (qty.equals("1")) {
            String removeButton = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
            Assert.assertEquals(removeButton, "Remove");
        } else {
            String removeButton = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
            Assert.assertNotEquals(removeButton, "Remove");
        }
    }

    @When("User click icon Cart")
    public void user_click_icon_cart() {
        driver.findElement(By.xpath("//*[@id='shopping_cart_container']/a")).click();
    }
    @Then("User click Remove button")
    public void user_click_remove_button() {
        driver.findElement(By.xpath("//*[@id='remove-sauce-labs-backpack']")).click();
    }

    @Then("User verify status remove")
    public void user_verify_status_remove() {
        String status = "1";
        if (status.equals("1")) {
            String qty = driver.findElement(By.xpath("//*[@id='shopping_cart_container']/a")).getText();
            Assert.assertEquals(qty,"");
        } else {
            String qty = driver.findElement(By.xpath("//*[@id='shopping_cart_container']/a/span")).getText();
            Assert.assertEquals(qty, "1");
        }
        driver.close();
    }
}
