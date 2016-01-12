package org.apache.atlas.seleniumtests;

import junit.framework.Assert;

import org.apache.atlas.objectwrapper.WebDriverWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.ui.pages.LineagePage;
import org.atlas.ui.pages.SearchPage;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LineagePageTest extends WebDriverWrapper {

	private static final Logger LOGGER = Logger.getLogger(LineagePageTest.class);
	private LineagePage lineagePage = null;
	private SearchPage searchPage = null;
	long testExecutionStartTime;

	@BeforeClass
	public void loadTagsTest() {
		lineagePage = new LineagePage();
		searchPage = new SearchPage();
		lineagePage.launchApp();
	}

	@AfterMethod
	public void getMethodName(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			AtlasDriverUtility.getScreenshot(result.getMethod().getMethodName());
		}
	}
	
	@BeforeMethod
	public void beforeMethod() {
		AtlasConstants.START_TIME = System.currentTimeMillis();
	}
	
	@Test
	public void testPageElementsFromSearchPage() {
		LOGGER.info("STARTED: Test testPageElements from Lineage Page");
		homePage.verifyPageLoadSuccessfully();
		LOGGER.info("ENDED: Test testPageElements from Lineage Page");
	}
	@Test
	public void validateBackToPageFunctionality() {
		LOGGER.info("ENTERED: validateBackToPageLink");
		searchPage.searchQuery("Metric");
		int resultCount = searchPage.getSearchResultCount();
		lineagePage.clickOnSearchData("134a83e1-68fc-4614-a0bb-8c24634dab29");
		lineagePage.clickOnBackToPageLink();
		Assert.assertTrue("Search result persist ",
				resultCount == searchPage.getSearchResultCount());
		LOGGER.info("EXITED: validateBackToPageLink");
	}
	
	@Test
	public void validateLineagePage(){
		LOGGER.info("ENTERED: validateLineagePage");
		 lineagePage.validateImage();
		 Assert.assertEquals("BackToPage link enabled", lineagePage.validateBackToPageLink(), true);
		 Assert.assertEquals("Details section displayed", true, lineagePage.isPageDataDisplayed());
		 Assert.assertEquals("Graphs Section loaded", true, lineagePage.validateGraphSection());
		LOGGER.info("EXITED: validateLineagePage");
	}
	
}
