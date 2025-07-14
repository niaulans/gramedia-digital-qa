package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BasePage;
import io.appium.java_client.AppiumDriver;

public class PrivacyAndTermsPage extends BasePage {

    private By agreeAndContinueBtn = By.id("com.appsfoundry.scoop:id/tv_agree");
    private By agreeCheckBox = By.id("com.appsfoundry.scoop:id/checkbox");

    public PrivacyAndTermsPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isAgreeAndContinueButtonVisible() {
        return isDisplayed(agreeAndContinueBtn);
    }

    public void checkAgreeTerms() {
        WebElement checkbox = find(agreeCheckBox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void tapAgreeAndContinue() {
        click(agreeAndContinueBtn);
    }
}
