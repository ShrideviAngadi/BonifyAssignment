/*

Task 2 (Level 2)

Write an automated test script for our login page my.bonify.de that tests all functions and
features on this one page and performs login.
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
import org.openqa.selenium.io.*;
import javax.annotation.Nonnull;
import java.sql.Array;
import java.util.List;
import java.util.Set;

public class BonifyLoginTest {

    public static final String XPATH_EMAILADDRESS="//div[contains(@class, 'text-field form-group')]//input[contains(@type, 'email')]";
    public static final String XPATH_PASSWORD="//div[contains(@class, 'text-field form-group')]//input[contains(@type, 'password')]";
    public static final String XPATH_LOGINBTN="//button[contains(@type, 'submit')]/div[1]";
    public static final String XPATH_ARROWICON="//header[contains(@id, 'main-header')]/div[1]/div[1]/div[2]//div[contains(@class, 'dropdown-label')]";
    public static final String XPATH_LOGOUTBUTTON="//ul[contains(@class, 'dropdown-layer open')]/li[4]/button/span";
    public static final String XPATH_REGISTRATIONBUTTON="//button[contains(@type, 'button')]/div[1]";
    public static final String XPATH_REGISTERFORFREEBUTTON="//button[contains(@type, 'submit')]/div";
    public static final String XPATH_FORGOTPASSWORD="//div[contains(@class, 'form-group remember-pass')]/a/span";
    public static final String XPATH_FORGOTPASSWRDPAGE="//div[contains(@class, 'substep')]/form/h1";
    public static final String XPATH_ERRORTEXT="//form[contains(@class, 'short-form')]/p[1]";
    public static final String XPATH_WELCOMEMESSAGE="//div[contains(@class, 'welcome')]//div[contains(@class, 'message')]/div[1]";
    public static final String XPATH_EMAILERROR="//form[contains(@class, 'short-form')]/div[1]/div[2]/span";
    public static final String XPATH_PASSWORDERROR="//form[contains(@class, 'short-form')]/div[2]/div[2]/span";
    public static final String XPATH_IMPRESSUMLINK="//form[contains(@class, 'short-form')]//li[1]/a[1]/span";
    public static final String XPATH_IMPRESSUMPAGE="//div[contains(@class, 'l-titlebar-content')]/h1";
    public static final String XPATH_AGBLINK="//form[contains(@class, 'short-form')]//li[2]/a[1]/span";
    public static final String XPATH_AGBPAGE="//div[contains(@class, 'l-titlebar-content')]/h1";
    public static final String XPATH_DATENSCHUTZLINK="//form[contains(@class, 'short-form')]//li[3]/a[1]/span";
    public static final String XPATH_DATENSCHUTZPAGE="//div[contains(@class, 'vc_column-inner')]/div[1]/h2[1]";
    public static final String XPATH_FOOTERIMAGE="//footer[contains(@class, 'security-logos paper')]/img";

    public WebDriver driver;

    @Test
    public void testSuccessLogin() {

        // Setup chrome driver
        driver = setupChromeDriver();

        // login
        fillEmailField("testbonify@bonify.com");
        fillPasswordField("testing123!");

        clickLoginButton();
        waitSeconds(1);

        // get welcome message string
        String welcomeText = driver.findElement(By.xpath(XPATH_WELCOMEMESSAGE)).getText();
        String expectedWelcomeMessage = "Hey testbonify@bonify.com, schön Dich zu sehen!";

        Assert.assertTrue("Welcome message not displayed",welcomeText.contains(expectedWelcomeMessage));

        waitSeconds(1);
        clickLogoutButton();
        driver.quit();
    }

    @Test
    public void testInvalidLogin() {

        // Setup chrome driver
        driver = setupChromeDriver();

        // login
        fillEmailField("wrongemailid@some.com");
        fillPasswordField("somepassword");
        clickLoginButton();
        waitSeconds(1);

        // get error string
        String errorText = driver.findElement(By.xpath(XPATH_ERRORTEXT)).getText();
        String expectedErrorText = "Leider passen Benutzername und Passwort nicht zusammen.";

        Assert.assertTrue("Error text not found", errorText.contains(expectedErrorText));
        driver.quit();
    }

    @Test
    public void testBlankFieldsError() {

        // Setup chrome driver
        driver = setupChromeDriver();
        clickLoginButton();
        waitSeconds(1);

        // Email error string
        String EmailerrorText = driver.findElement(By.xpath(XPATH_EMAILERROR)).getText();
        String expectedEmailErrorText = "Trage bitte Deine Email-Adresse ein";
        Assert.assertTrue("Email error text not found", EmailerrorText.contains(expectedEmailErrorText));

        String PassworderrorText = driver.findElement(By.xpath(XPATH_PASSWORDERROR)).getText();
        String expectedPasswordErrorText = "Trage bitte Dein Passwort ein";

        Assert.assertTrue("Password error text not found", PassworderrorText.contains(expectedPasswordErrorText));
        driver.quit();
    }

    @Test
    public void testResgistrationButtonAction() {

        // Sertup chrome driver
        driver = setupChromeDriver();
        clickRegistrationButton();
        waitSeconds(1);

        Assert.assertNotNull("Register for free button not found", driver.findElement(By.xpath(XPATH_REGISTERFORFREEBUTTON)));
        driver.quit();
    }

    @Test
    public void testForgotPasswordLink() {

        // Setup chrome driver
        driver = setupChromeDriver();
        clickForgotPassword();
        waitSeconds(1);

        String ForgotPasswordText = driver.findElement(By.xpath(XPATH_FORGOTPASSWRDPAGE)).getText();
        String expectedForgotPasswordPageText = "Email senden";

        Assert.assertTrue("Forgot page text not found", ForgotPasswordText.contains(expectedForgotPasswordPageText));
        driver.quit();
    }

    @Test
    public void testImpressumLink() {

        // Setup chrome driver
        driver = setupChromeDriver();
        clickImpressumLink();
        waitSeconds(2);

        // get all open tabs
        Object[] browserTabs = driver.getWindowHandles().toArray();

        // switch to tab 2
        driver.switchTo().window((String) browserTabs[1]);

        waitSeconds(2);

        String ImpressumText = driver.findElement(By.xpath(XPATH_IMPRESSUMPAGE)).getText();
        String expectedImpressumPageText = "Impressum";

        Assert.assertTrue("Impressum page text not found", ImpressumText.contains(expectedImpressumPageText));
        driver.quit();
    }

    @Test
    public void testAGBLink() {

        // Setup chrome driver
        driver = setupChromeDriver();
        clickAGBLink();
        waitSeconds(2);

        // get all open tabs
        Object[] browserTabs = driver.getWindowHandles().toArray();

        // switch to tab 2
        driver.switchTo().window((String) browserTabs[1]);

        waitSeconds(1);

        String AGBText = driver.findElement(By.xpath(XPATH_AGBPAGE)).getText();
        String expectedAGBPageText = "AGB";

        Assert.assertTrue("AGB page text not found", AGBText.contains(expectedAGBPageText));
        driver.quit();
    }

    @Test
    public void testDatenschutzLink() {
        driver = setupChromeDriver();
        clickDatenschutzLink();
        waitSeconds(2);

        // get all open tabs
        Object[] browserTabs = driver.getWindowHandles().toArray();

        // switch to tab 2
        driver.switchTo().window((String) browserTabs[1]);

        waitSeconds(1);

        String DatenschutzText = driver.findElement(By.xpath(XPATH_DATENSCHUTZPAGE)).getText();
        String expectedDatenschutzPageText = "Datenschutz";

        Assert.assertTrue("Datenschutz page text not found", DatenschutzText.contains(expectedDatenschutzPageText));
        driver.quit();
    }

    @Test
    public void testFooterImage() {
        driver = setupChromeDriver();
        waitSeconds(1);

        WebElement imageElement = driver.findElement(By.xpath(XPATH_FOOTERIMAGE));

        Assert.assertNotNull("Footer image not found", imageElement);
        driver.quit();
    }

    private void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException ingnored) {

        }
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

    private void clickLogoutButton() {

        driver.findElement(By.xpath(XPATH_ARROWICON)).click();
        waitSeconds(2);

        mouseOver(By.xpath(XPATH_LOGOUTBUTTON));
        driver.findElement(By.xpath(XPATH_LOGOUTBUTTON)).click();
        waitSeconds(2);
    }

    protected void mouseOver(@Nonnull By by) {
        Actions mouseOverAction = new Actions(driver);
        mouseOverAction.moveToElement(driver.findElement(by)).perform();
        waitSeconds(2);
    }

    private void clickRegistrationButton() {

        driver.findElement(By.xpath(XPATH_REGISTRATIONBUTTON)).click();
    }

    private void clickForgotPassword() {

        driver.findElement(By.xpath(XPATH_FORGOTPASSWORD)).click();
    }

    private void clickImpressumLink() {

        driver.findElement(By.xpath(XPATH_IMPRESSUMLINK)).click();
    }

    private void clickAGBLink() {

        driver.findElement(By.xpath(XPATH_AGBLINK)).click();
    }

    private void clickDatenschutzLink() {

        driver.findElement(By.xpath(XPATH_DATENSCHUTZLINK)).click();
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
}
