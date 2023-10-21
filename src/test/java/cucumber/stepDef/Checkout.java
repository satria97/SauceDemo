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

public class Checkout {
    WebDriver driver;
    String baseUrl = "https://www.saucedemo.com/";

    @Given("Product is already in the cart")
    public void product_is_already_in_the_cart() {
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

    @When("User click Checkout button")
    public void user_click_checkout_button() {
        driver.findElement(By.id("checkout")).click();
    }

    @Then("user input (.*) as firstname$")
    public void user_input_jhon_as_first_name(String firstname) {
        driver.findElement(By.id("first-name")).sendKeys(firstname);
    }

    @And("user input (.*) as lastname$")
    public void user_input_doe_as_last_name(String lastname) {
        driver.findElement(By.id("last-name")).sendKeys(lastname);
    }

    @And("user input (.*) as postal code$")
    public void user_input_12345_as_portal_code(String postalcode) {
        driver.findElement(By.id("postal-code")).sendKeys(postalcode);
    }

    @Then("user click Continue button")
    public void user_click_continue_button() {
        driver.findElement(By.id("continue")).click();
    }

    @Then("user verify (.*) as information$")
    public void user_verify_success_as_information(String status) {
        if (status.equals("invalid_firstname")) {
            String errorFirst = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
            Assert.assertEquals(errorFirst, "Error: First Name is required");
            driver.close();
        } else if (status.equals("invalid_lastname")) {
            String errorLast = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
            Assert.assertEquals(errorLast, "Error: Last Name is required");
            driver.close();
        } else if (status.equals("invalid_postal")) {
            String errorPostal = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
            Assert.assertEquals(errorPostal, "Error: Postal Code is required");
            driver.close();
        } else {
            String successCheckout = driver.findElement(By.xpath("//span[contains(text(), 'Checkout: Overview')]")).getText();
            Assert.assertEquals(successCheckout, "Checkout: Overview");
        }
    }

    @Then("user click Finish button")
    public void user_click_finish_button() {
        driver.findElement(By.id("finish")).click();
    }

    @Then("user verify (.*) checkout result$")
    public void user_verify_success_checkout(String status) {
        if (status.equals("success")) {
            String successCheckout = driver.findElement(By.xpath("//h2[contains(text(), 'Thank you for your order!')]")).getText();
            Assert.assertEquals(successCheckout, "Thank you for your order!");
        } else {
            String successCheckout = driver.findElement(By.xpath("//h2[contains(text(), 'Thank you for your order!')]")).getText();
            Assert.assertNotEquals(successCheckout, "Thank you for your order!");
        }
        driver.close();
    }
}
