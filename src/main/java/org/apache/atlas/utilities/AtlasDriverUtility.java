package org.apache.atlas.utilities;

import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.testHelper.BaseUITestClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AtlasDriverUtility extends BaseUITestClass{

	private static final Logger LOGGER = Logger.getLogger(AtlasDriverUtility.class);
	
	public static void waitForAngularToFinish() {
        final String javaScript = "return (window.angular != null) && "
            + "(angular.element(document).injector() != null) && "
            + "(angular.element(document).injector().get('$http').pendingRequests.length === 0)";
        boolean isLoaded = false;
        for (int i = 0; i < AtlasConstants.PAGELOAD_TIMEOUT_THRESHOLD && !isLoaded; i++) {
            final Object output = ((JavascriptExecutor) getDriver()).executeScript(javaScript);
            isLoaded = Boolean.valueOf(output.toString());
            LOGGER.info(i+1 + ". waiting on angular to finish.");
        }
        LOGGER.info("angular is done continuing...");
    }
	
	public static WebElement waitUntilElementVisible(WebElement element, int timeInSec){
		WebDriverWait wait = new WebDriverWait(getDriver(), timeInSec);
		return wait.until(
		        ExpectedConditions.visibilityOf(element));
	}
	
	public static void customWait(int timeInSec){
		try {
			Thread.sleep(1000 * timeInSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
