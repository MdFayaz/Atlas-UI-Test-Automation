package org.apache.atlas.seleniumtests;

import junit.framework.Assert;

import org.apache.atlas.objectwrapper.WebDriverWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.ui.pages.LineagePage;
import org.atlas.ui.pages.SearchPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LineagePageTest extends WebDriverWrapper {

	private static final Logger LOGGER = Logger.getLogger(LineagePageTest.class);
	private LineagePage lineagePage = null;
	private SearchPage searchPage = null;
	long testExecutionStartTime;

	@BeforeClass
	public void loadTagsTest() {
		AtlasConstants.START_TIME = System.currentTimeMillis();
		lineagePage = new LineagePage();
		searchPage = new SearchPage();
		lineagePage.launchApp();
	}
	
	@AfterClass
	public void tearDown() {
		closeBrowser();
		AtlasDriverUtility.testSuiteExecutionTime(testExecutionStartTime,
				" execute entire test suite");
	}
	
	@Test
	public void testPageElementsFromSearchPage() {
		LOGGER.info("STARTED: Test testPageElements from Lineage Page");
		homePage.verifyPageLoadSuccessfully();
		LOGGER.info("ENDED: Test testPageElements from Lineage Page");
	}	
	
	@Test
	public void validateBackToPageFunctionality() {
		LOGGER.info("STARTED: validateBackToPageLink");
		searchPage.searchQuery("Metric");
		int resultCount = searchPage.getSearchResultCount();
		lineagePage.clickOnSearchData("134a83e1-68fc-4614-a0bb-8c24634dab29");
		Assert.assertEquals("BackToPage link enabled", lineagePage.validateBackToPageLink(), true);
		lineagePage.clickOnBackToPageLink();
		Assert.assertTrue("Search result persist ",
				resultCount == searchPage.getSearchResultCount());
		LOGGER.info("ENDED: validateBackToPageLink");
	}
	
	@Test
	public void validateLineagePage(){
		LOGGER.info("STARTED: validateLineagePage");
		 lineagePage.goToLineagePageFor("Fact:0de1ae11-b498-484f-965f-1019501d3870");
		 Assert.assertEquals("Graphs Section loaded", true, lineagePage.validateGraphSection());
		 Assert.assertEquals("Details section displayed", true, lineagePage.isPageDataDisplayed());
		LOGGER.info("ENDED: validateLineagePage");
	}
	
	@Test
	public void validateLineagePageTabs() {
		LOGGER.info("STARTED: validateLineagePageTabs");
		lineagePage
				.goToLineagePageFor("Fact:0de1ae11-b498-484f-965f-1019501d3870");
		boolean allTabsClicked = lineagePage.clickOnAllTabs();
		Assert.assertTrue("All tabs clicked in lineage page", allTabsClicked);
		LOGGER.info("ENDED: validateLineagePageTabs");
	}
	
}
