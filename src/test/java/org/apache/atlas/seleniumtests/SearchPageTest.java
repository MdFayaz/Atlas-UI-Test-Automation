package org.apache.atlas.seleniumtests;

import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.testHelper.BaseUITestClass;
import org.atlas.ui.search.SearchPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class SearchPageTest extends BaseUITestClass {

	private static final Logger LOGGER = Logger.getLogger(SearchPageTest.class);
	private SearchPage searchPage = null;

	@BeforeClass
	public void setup() {
		openBrowser();
		searchPage = SearchPage.open();
	}

	@AfterClass
	public void tearDown() {
		closeBrowser();
	}

	@Test
	public void testPageElements() {
		baseTestClass.verifyPageLoadSuccessfully();
	}

	@Test(dataProvider = AtlasConstants.SEARCH_STRING, dataProviderClass = SearchPage.class)
	public void searchTestMethod(String astrikSearch,String tableSearch) {
		LOGGER.info("Search Test Method started");
		searchPage.searchQuery(astrikSearch);
		searchPage.searchQuery(tableSearch);
		LOGGER.info("Search Test Method Ended");
	}

}
