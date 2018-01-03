package DEMO.Selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class AOSWebTest {
    private static RemoteWebDriver driver;
    static DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    @BeforeClass
    public static void openBrowser() throws MalformedURLException {

        capabilities.setVersion("latest");
        capabilities.setCapability("platform", "Windows 10");
        capabilities.setCapability("testName", "Selenium Java - Online Shopping");
        capabilities.setCapability("SRF_CLIENT_ID", "<REPLACE THIS WITH SRF_CLIENT_ID>");
        capabilities.setCapability("SRF_CLIENT_SECRET", "<REPLACE THIS WITH SRF_CLIENT_SECRET>");
        capabilities.setCapability("resolution", "1366x768");

        driver = new RemoteWebDriver(
                new URL("http://ftaas.saas.hpe.com/wd/hub/"), capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void OnlineShoppingE2E() throws InterruptedException {
        Actions builder = new Actions(driver);
        driver.get("http://advantageonlineshopping.com/#");

        driver.findElementByXPath("//*[@id=\"menuSearch\"]").click();
        driver.findElementByXPath("//*[@id=\"autoComplete\"]").sendKeys("Speakers");     // search speakers
        driver.findElementByCssSelector("a.product:nth-child(3) > img:nth-child(1)").click();           // select a speaker

        WebElement addToCart = driver.findElementByCssSelector(".fixedBtn > button:nth-child(1)");      // add to cart
        builder.click(addToCart).build().perform();
        driver.findElementByXPath("//*[@id=\"checkOutPopUp\"]").click();                                // Check out

        driver.findElementByXPath("/html/body/div[3]/section/article/div/div[1]/div/div[1]/sec-form/sec-view[1]/div/input").sendKeys("Shahar");
        driver.findElementByXPath("/html/body/div[3]/section/article/div/div[1]/div/div[1]/sec-form/sec-view[2]/div/input").sendKeys("Password1");
        WebElement clickLogin = driver.findElementByXPath("//*[@id=\"login_btnundefined\"]");
        builder.click(clickLogin).build().perform();                                                  // Click log in
        driver.findElementByXPath("//*[@id=\"next_btn\"]").click();                             // Click Next
        driver.findElementByXPath("//*[@id=\"pay_now_btn_MasterCredit\"]").click();             // Click pay now
    }

    @AfterClass
    public static void closeBrowser() {
        driver.quit();
    }
}

