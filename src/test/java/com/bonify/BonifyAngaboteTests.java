/*

Task 3 (Level 2)

Write 1 test case of your choice(not login) for my.bonify.de application and implement
automation for this use case. The test case preferrable should include the validation of data
selected on previous screen or/and work with iframes in the web application.
Expected result is the self-runnable test and step by step explanation on how to run it,
including the needed tools installations to be made.

 */

package com.bonify;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;



public class BonifyAngaboteTests {

    public static final String XPATH_EMAILADDRESS="//div[contains(@class, 'text-field form-group')]//input[contains(@type, 'email')]";
    public static final String XPATH_PASSWORD="//div[contains(@class, 'text-field form-group')]//input[contains(@type, 'password')]";
    public static final String XPATH_LOGINBTN="//button[contains(@type, 'submit')]/div[1]";
    public static final String XPATH_FIRSTFRAME="//iframe[contains(@src, '/dist/9ab29bbcb5ffa57e176b6422412b40c8.html?postcode=10117&campaignId=iframe&subPartnerId=346424')]";
    public static final String XPATH_SECONDFRAME="//iframe[contains(@src, 'https://tools.financeads.net/kreditkarterechner.php?tp=dif&h=1&wf=24150&ntpl=responsive&width=650&subid=346424&campaignId=iframe')]";
    public static final String XPATH_ANGEBOTE="//*[@id=\"sidebar\"]/div/nav/ul[1]/li[4]/a";
    public static final String XPATH_FINANZEN="//*[@id=\"/my-products/financial-products\"]/a/div";
    public static final String XPATH_FIRSTDROPDOWN="//div[contains(@class, 'col-lg-12 col-md-12 col-sm-12 col-xs-12')]/select[contains(@class, 'kreditkarterechner_kartengesellschaft')]";
    public static final String XPATH_SECONDDROPDOWN="//div[contains(@class, 'col-lg-12 col-md-12 col-sm-12 col-xs-12')]/select[contains(@class, 'kreditkarterechner_anzeige')]";
    public static final String XPATH_SUBMITBUTTON="//div[contains(@class, 'col-md-3 col-md-offset-9 col-sm-4 col-sm-offset-8 col-xs-12')]/input";
    public static final String XPATH_AMERICANEXPRESS="//body//div[contains(@class, 'content')]//span[contains(@class, 'fa_link')]";
    public static final String XPATH_AMERICANPLATINUMCARD="//body//div[contains(@class, 'content')]//span[contains(@class, 'fa_productlink')]";
    public static final String XPATH_ARROWICON="//header[contains(@id, 'main-header')]/div[1]//div[2]";
    public static final String XPATH_LOGOUTBUTTON="//div[contains(@id, 'main-body')]/ul[1]/li[4]/button/span";


    private static final int WAIT_AFTER_MOUSE_OVER = 10;

    public WebDriver driver;

    @Test
    public void testFinanzenFlow() {

        // Setup chrome driver
        driver = setupChromeDriver();

        // Login to my.bonify.de
        fillEmailField("bonify.test@trash-mail.com");
        fillPasswordField("testing123!");

        clickLoginButton();

        waitSeconds(2);

        // Switch to Angebote > Finanzen
        driver.findElement(By.xpath(XPATH_ANGEBOTE)).click();
        waitSeconds(2);
        driver.findElement(By.xpath(XPATH_FINANZEN)).click();
        waitSeconds(2);

        // Switch to iframe
        driver.switchTo().frame(driver.findElement(By.xpath(XPATH_FIRSTFRAME)));
        driver.switchTo().frame(driver.findElement(By.xpath(XPATH_SECONDFRAME)));

        waitSeconds(3);

        // Select from card type dropdown > Select American Express
        Select dropdown = new Select(driver.findElement(By.xpath(XPATH_FIRSTDROPDOWN)));
        dropdown.selectByIndex(1);
        waitSeconds(2);

        // Choose Platinum card
        Select StatusDropdown = new Select(driver.findElement(By.xpath(XPATH_SECONDDROPDOWN)));
        StatusDropdown.selectByIndex(3);
        waitSeconds(3);

        driver.findElement(By.xpath(XPATH_SUBMITBUTTON)).click();
        waitSeconds(5);

        // Verify results fetched are American Express
        List<WebElement> AllSearchResultsAE = driver.findElements(By.xpath(XPATH_AMERICANEXPRESS));

        for(WebElement eachResultAE:AllSearchResultsAE)
        {
            if(eachResultAE.isDisplayed()){
                System.out.println("All Fetched");
            }
            else {
                System.out.println("Not Fetched");
            }
        }
        waitSeconds(2);

        //Verify results fetched are American Express Platinum Card
        List<WebElement> AllSearchResultsAEPC = driver.findElements(By.xpath(XPATH_AMERICANPLATINUMCARD));
        for(WebElement eachResultAEPC:AllSearchResultsAEPC)
        {
            Assert.assertTrue(eachResultAEPC.isDisplayed());
            if(eachResultAEPC.isDisplayed()){
                System.out.println("All Fetched");
            }
            else {
                System.out.println("Not Fetched");
            }
        }

        // Switch back to main content
        driver.switchTo().parentFrame();
        waitSeconds(1);
        driver.switchTo().defaultContent();
        waitSeconds(1);

        // logout
        clickLogoutButton();
        waitSeconds(2);
        driver.quit();
    }

    private void fillEmailField(String email) {

        driver.findElement(By.xpath(XPATH_EMAILADDRESS)).sendKeys(email);
    }

    private void fillPasswordField(String password) {

        driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(password);
    }

    private void clickLoginButton() {

        driver.findElement(By.xpath(XPATH_LOGINBTN)).click();
    }

    private WebDriver setupChromeDriver() {

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("----Start maximized");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(capabilities);
        driver.get("https://my.bonify.de/");
        return driver;
    }


    protected void waitMillis(@Nonnegative int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ignored) {
        }
    }

    protected void waitSeconds(int seconds) {
        waitMillis(seconds * 1000);
    }

    protected void mouseOver(@Nonnull By by, WebDriver driver) {
        Actions mouseOverAction = new Actions(driver);
        mouseOverAction.moveToElement(driver.findElement(by)).perform();
        waitMillis(WAIT_AFTER_MOUSE_OVER);
    }

    protected void select(@Nonnull By by, @Nonnull String text, WebDriver driver) {
        mouseOver(by, driver);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(driver.findElement(by));
        select.selectByVisibleText(text);
    }

    protected void mouseOver(@Nonnull By by) {
        Actions mouseOverAction = new Actions(driver);
        mouseOverAction.moveToElement(driver.findElement(by)).perform();
        waitSeconds(2);
    }

    private void clickLogoutButton() {

        driver.findElement(By.xpath(XPATH_ARROWICON)).click();
        waitSeconds(1);

        List <WebElement> logoutButton = driver.findElements(By.xpath(XPATH_LOGOUTBUTTON));
        for (WebElement logoutbutton1 :logoutButton) {

            if(logoutbutton1.isDisplayed()) {
                logoutbutton1.click();
            }
        }
    }
}
