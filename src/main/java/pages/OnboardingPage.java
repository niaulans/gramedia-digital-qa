package pages;

import org.openqa.selenium.By;

import base.BasePage;
import io.appium.java_client.AppiumDriver;

public class OnboardingPage extends BasePage {

    private By actionButton = By.id("com.appsfoundry.scoop:id/btnAction");

    public OnboardingPage(AppiumDriver driver) {
        super(driver);
    }

    public void tapActionButton() {
        click(actionButton);
    }

    public boolean isActionButtonVisible() {
        return isDisplayed(actionButton);
    }

    public String getActionButtonText() {
        return find(actionButton).getText();
    }

    public void waitUntilActionButtonVisible(int seconds) {
        waitUntilVisible(actionButton, seconds);
    }

}
