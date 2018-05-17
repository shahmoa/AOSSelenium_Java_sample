package com.mf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.CommandInfo;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpHost;

public class AOSWebTest {
    private static RemoteWebDriver driver;
    private static DesiredCapabilities capabilities;

    @BeforeClass
    public static void openBrowser() throws MalformedURLException {

        boolean hasProxy = false;
        String clientID = "t511780658_oauth2-r59KGnAQQhMfzYTlnpar";
        String clientSecret = "pskKNTcojAyDEpMpw1gS";
        String SeleniumURL = "http://ftaas.saas.hpe.com/wd/hub";
        String testName = "Selenium/Java-AOS-remote-exec";

        String remoteDriverAddr = System.getenv("SELENIUM_ADDRESS");
        if (remoteDriverAddr != null) {
            SeleniumURL = remoteDriverAddr;
            clientID = System.getenv("SRF_CLIENT_ID");
            clientSecret = System.getenv("SRF_CLIENT_SECRET");
            testName = "Selenium/Java-AOS";
        }
        capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("build", "12.3.5");
        capabilities.setCapability("release", "969");

        capabilities.setCapability("version", "64");
        capabilities.setCapability("platform", "Windows 10");
        capabilities.setCapability("resolution", "1366x768");

        capabilities.setCapability("testName", testName);
        capabilities.setCapability("SRF_CLIENT_ID", clientID);
        capabilities.setCapability("SRF_CLIENT_SECRET", clientSecret);

        // this code was not tested //
        if (hasProxy && remoteDriverAddr==null) {
            URL srfGatewayUrl = new URL("https", "ftaas.saas.hpe.com", 443, "/wd/hub/");

            System.out.println("Creating remote web driver with address: " + srfGatewayUrl);

            String proxyHost = "http://pac.wellsfargo.net"; //use your org proxy
            int proxyPort = 80;

            HttpClientBuilder builder = HttpClientBuilder.create();
            HttpHost driverProxy = new HttpHost(proxyHost, proxyPort);

            builder.setProxy(driverProxy);

            HttpClient.Factory factory = new MyHttpClientFactory(builder);
            HttpCommandExecutor executor = new HttpCommandExecutor(new HashMap<String, CommandInfo>(), srfGatewayUrl, factory);

            driver = new RemoteWebDriver(executor, capabilities);
        } else {
            driver = new RemoteWebDriver(new URL(SeleniumURL), capabilities);
        }
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

