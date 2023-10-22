package cucumber.stepDef;

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

import java.util.concurrent.TimeUnit;

public class Login {
    WebDriver driver;
    String baseUrl = "https://www.saucedemo.com/";

    @Given("user is on SauceDemo login page")
    public void user_is_on_sauce_demo_login_page() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opt = new ChromeOptions();
        opt.setHeadless(false);

        driver = new ChromeDriver(opt);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @When("user input (.*) as username$")
    public void user_input_standard_user_as_username(String username) {
        driver.findElement(By.id("user-name")).sendKeys(username);
    }

    @And("user input (.*) as password$")
    public void user_input_secret_sauce_as_password(String password) {
        driver.findElement(By.id("password")).sendKeys(password);
    }

    @And("user click login button")
    public void user_click_login_button() {
        driver.findElement(By.xpath("//input[@type='submit']")).click();
    }

    @Then("user verify (.*) login result$")
    public void user_verify_success_login_result(String status) {
        if (status.equals("failed")) {
            String errorLogin = driver.findElement(By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3")).getText();
            Assert.assertEquals(errorLogin, "Epic sadface: Username and password do not match any user in this service");
        } else if (status.equals("user_empty")) {
            String errorUser = driver.findElement(By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3")).getText();
            Assert.assertEquals(errorUser, "Epic sadface: Username is required");
        } else if (status.equals("password_empty")) {
            String errorPass = driver.findElement(By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3")).getText();
            Assert.assertEquals(errorPass, "Epic sadface: Password is required");
        } else {
            String dashboard = driver.findElement(By.xpath("//span[contains(text(), 'Products')]")).getText();
            Assert.assertEquals(dashboard, "Products");
        }
        driver.close();
    }
}
