package tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.time.Duration;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import pages.OnboardingPage;
import pages.PrivacyAndTermsPage;

public class OnboardingTests extends BaseTest {

    private OnboardingPage onboardingPage;

    @BeforeMethod(alwaysRun = true)
    public void resetAppConditionally() throws Exception {

        clearAppData();
        if (driver != null)
            driver.quit();
        setUp();

        onboardingPage = new OnboardingPage(driver);
    }

    @Test(description = "TC_OB_001 - Validate onboarding screen is displayed when user hasn't accepted terms")
    public void shouldDisplayOnboardingScreen_WhenUserHasNotAcceptedTerms() {

        boolean isVisible = onboardingPage.isActionButtonVisible();
        String buttonText = onboardingPage.getActionButtonText();

        assertTrue(isVisible, "Onboarding action button should be visible.");
        assertEquals(buttonText, "Selanjutnya");
        System.out.println("Onboarding screen is shown with \"" + buttonText + "\" button visible");
    }

    @Test(description = "TC_OB_002 - Validate user can swipe between onboarding slides")
    public void shouldSwipeBetweenOnboardingSlides() {

        String textSlide1 = onboardingPage.getActionButtonText();
        assertEquals(textSlide1, "Selanjutnya");

        onboardingPage.swipeLeft();
        String textSlide2 = onboardingPage.getActionButtonText();
        assertEquals(textSlide2, "Selanjutnya");

        onboardingPage.swipeLeft();
        String textSlide3 = onboardingPage.getActionButtonText();
        assertEquals(textSlide3, "Mulai");

        onboardingPage.swipeRight();
        assertEquals(textSlide2, "Selanjutnya");

        onboardingPage.swipeRight();
        assertEquals(textSlide1, "Selanjutnya");
    }

    @Test(description = "TC_OB_005 - Validate 'Selanjutnya' button navigates to the next slide")
    public void shouldNavigateToNextSlide_WhenSelanjutnyaButtonClicked() {

        String button1Text = onboardingPage.getActionButtonText();
        assertEquals(button1Text, "Selanjutnya");

        onboardingPage.tapActionButton();
        String button2Text = onboardingPage.getActionButtonText();
        assertEquals(button2Text, "Selanjutnya");

        onboardingPage.tapActionButton();
        String button3Text = onboardingPage.getActionButtonText();
        assertEquals(button3Text, "Mulai", "Should be on slide 3 after two times tap");
    }

    @Test(description = "TC_OB_006 - Validate tapping 'Mulai' navigates to Privacy Policy & Terms screen")
    public void shouldShowMulaiButton_OnLastSlide() {

        onboardingPage.tapActionButton();
        onboardingPage.tapActionButton();

        String buttonText = onboardingPage.getActionButtonText();
        assertEquals(buttonText, "Mulai", "On last slide, the button should show text 'Mulai'");

        onboardingPage.tapActionButton();

        PrivacyAndTermsPage privacyAndTermsPage = new PrivacyAndTermsPage(driver);
        assertTrue(privacyAndTermsPage.isAgreeAndContinueButtonVisible(),
                "Button 'Agree and Continue' should be visible");
    }

    @Test(description = "TC_OB_007 - Validate app continues to show onboarding screen if terms remain unaccepted")
    public void shouldKeepShowingOnBoardingScreen_IfTermsNotAccepted() throws InterruptedException {

        onboardingPage.tapActionButton();
        onboardingPage.tapActionButton();

        ((AndroidDriver) driver).terminateApp(PACKAGE_NAME);
        ((AndroidDriver) driver).activateApp(PACKAGE_NAME);

        onboardingPage = new OnboardingPage(driver);
        onboardingPage.waitUntilActionButtonVisible(5);

        boolean isVisible = onboardingPage.isActionButtonVisible();
        String buttonText = onboardingPage.getActionButtonText();

        assertTrue(isVisible, "Onboarding should still visible.");
        assertEquals(buttonText, "Selanjutnya", "Should return to the first onboarding slide");
    }

    @Test(description = "TC_OB_009 - Validate onboarding state persists after app is backgrounded and resumed")
    public void shouldPersistOnboardingState_WhenAppBackgroundedAndResumed() {

        onboardingPage.tapActionButton();
        String textBefore = onboardingPage.getActionButtonText();
        assertEquals(textBefore, "Selanjutnya", "Should be on slide 2");

        ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(5));

        onboardingPage.waitUntilActionButtonVisible(5);
        String textAfter = onboardingPage.getActionButtonText();
        assertEquals(textAfter, "Selanjutnya", "Should remain on slide 2 after resume");
    }

    @Test(description = "TC_OB_010 - Validate onboarding is skipped is user already accepted terms")
    public void shouldSkipOnboarding_WhenUserHasAcceptedTerms() {

        onboardingPage.tapActionButton();
        onboardingPage.tapActionButton();
        onboardingPage.tapActionButton();

        PrivacyAndTermsPage privacyAndTermsPage = new PrivacyAndTermsPage(driver);
        privacyAndTermsPage.checkAgreeTerms();
        privacyAndTermsPage.tapAgreeAndContinue();

        ((AndroidDriver) driver).terminateApp(PACKAGE_NAME);
        ((AndroidDriver) driver).activateApp(PACKAGE_NAME);

        onboardingPage = new OnboardingPage(driver);
        boolean isOnboardingVisible = onboardingPage.isActionButtonVisible();

        assertFalse(isOnboardingVisible, "Onboarding should be skipped after user terms.");
    }

}
