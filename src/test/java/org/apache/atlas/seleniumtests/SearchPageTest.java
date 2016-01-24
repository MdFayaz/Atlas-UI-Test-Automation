package org.apache.atlas.seleniumtests;

import java.util.Arrays;

import org.apache.atlas.objectwrapper.WebDriverWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.ui.pages.SearchPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

public class SearchPageTest extends WebDriverWrapper {

	private static final Logger LOGGER = Logger.getLogger(SearchPageTest.class);

	private SearchPage searchPage = null;
	
	@BeforeClass(description = "SearchPage Test Setup")
	public void loadSearchTest(XmlTest config){
		searchPage = new SearchPage();
		searchPage.launchApp();
	}

	@Test
	public void testPageElementsFromSearchPage() {
		LOGGER.info("STARTED: Test testPageElements from SearchPage");
		homePage.verifyPageLoadSuccessfully();
		LOGGER.info("ENDED: Test testPageElements from SearchPage");
	}

	@Test
	public void validateSearchFunctionalty() {
		LOGGER.info("STARTED: Test searchTestMethod");
		searchPage.searchQuery("Fact");
		LOGGER.info("ENDED: Test searchTestMethod");
	}

	@Test(dataProvider = AtlasConstants.INVALID_SEARCH_STRING, dataProviderClass = SearchPage.class)
	public void invalidSearchResult(String invalidQuery) {
		LOGGER.info("STARTED: Test verifySearchResultNegative");
		searchPage.searchQuery(invalidQuery);

		Assert.assertTrue(searchPage.searchPageElements.noResultFound.isDisplayed(),
				"An Alert Banner displayed");

		String expectedMessage = "No Result found";
		String actualMessage = searchPage.searchPageElements.noResultFound.findElement(
				By.cssSelector(".ng-binding")).getText();

		Assert.assertEquals(actualMessage, expectedMessage,
				"No Result found message displayed");
		AtlasDriverUtility.waitUntilElementVisible(
				searchPage.searchPageElements.noResultFound, 10);
		WebElement close = searchPage.searchPageElements.noResultFound.findElement(By
				.tagName("button"));
		Assert.assertTrue(close.isDisplayed(), "Alert message displayed");
		close.click();
		LOGGER.info("ENDED: Test verifySearchResultNegative");
	}

	@Test
	public void validateSearchResultCount() {
		LOGGER.info("STARTED: Test validateSearchResult");
		String SEARCH_QUERY = "Table";
		searchPage.searchQuery(SEARCH_QUERY);
		String expectedMessage = searchPage.searchPageElements.resultCount.getText();
		if(searchPage.getSearchResultCount() > 0){
			Assert.assertTrue(searchPage.getSearchResultCount() > 0, expectedMessage);
		}
		LOGGER.info("ENDED: Test validateSearchResult");
	}

	@Test(dataProvider = AtlasConstants.SEARCH_STRING, dataProviderClass = SearchPage.class)
	public void validatePagination(String token) {
		LOGGER.info("STARTED: Test validatePagination");
			searchPage.searchQuery(token.toString());
			int searchCount = searchPage.getSearchResultCount();
			if (searchCount == 0) {
				LOGGER.info("No results found");
				Assert.assertTrue(!webElement.isElementExists(searchPage.searchPageElements.resultTable), 
						"Table not displayed for no result");
			}
			LOGGER.info("Searching with query : " + token
					+ " to assert with row count as " + searchCount);
			
			Assert.assertTrue(
					webElement.isElementExists(searchPage.searchPageElements.paginationPrevious),
					"Previous button displayed");

			String prevButtonState = searchPage.searchPageElements.paginationPrevious
					.getAttribute("class").toString();
			Assert.assertTrue(prevButtonState
					.contains(AtlasConstants.BUTTON_ATTRIBUTE_AS_DISBALED),
					"Previous button disabled");

			Assert.assertTrue(
					webElement.isElementExists(searchPage.searchPageElements.paginationNext),
					"Next button displayed");
			String nextButtonState = searchPage.searchPageElements.paginationNext.getAttribute(
					"class").toString();
			if (searchCount < 10) {
				Assert.assertTrue(nextButtonState
						.contains(AtlasConstants.BUTTON_ATTRIBUTE_AS_DISBALED),
						"Next button disabled");
			} else if (searchCount > 10) {
				Assert.assertTrue(!nextButtonState
						.contains(AtlasConstants.BUTTON_ATTRIBUTE_AS_DISBALED),
						"Next button enabled");
			}
		LOGGER.info("ENDED: Test validatePagination");
	}

	@Test(dataProvider = AtlasConstants.SEARCH_TABLE_HEADERS, dataProviderClass = SearchPage.class)
	public void validateTableHeaders(String... expectedTableHeaders) {
		LOGGER.info("STARTED: Test validateTableHeader");
		String[] actualHeaders = searchPage.getSearchResultTableHeaders();
		String[] expectedHeaders = expectedTableHeaders;
		Arrays.asList(actualHeaders);
		Assert.assertEquals(actualHeaders, expectedHeaders,
				"Table Header Validation");
		LOGGER.info("ENDED: Test validateTableHeader");
	}

	@Test
	public void validateTagSectionInSearchPage() {
		LOGGER.info("STARTED: Test validateTagSection");
		Assert.assertTrue(webElement.isElementExists(searchPage.searchPageElements.tagsSection),
				"Tags Section displayed");
		LOGGER.info("ENDED: Test validateTagSection");
	}
	
	@Test
	public void validateTagInSearchResult(){
		LOGGER.info("STARTED: Test validateTagInSearchResult");
		String SEARCH_QUERY = "Table";
		searchPage.searchQuery(SEARCH_QUERY);
		if(searchPage.getSearchResultCount() > 0){
			Assert.assertFalse(SearchPage.isPreviousButtonDisabled, "Previous Button disabled");
			Assert.assertTrue(SearchPage.isNextButtonEnabled, "Next Button enabled");
			searchPage.clickOnTag("Employee:Fact");
			Assert.assertTrue(SearchPage.isPreviousButtonEnabled, "Previous Button enabled");
			Assert.assertFalse(!SearchPage.isNextButtonDisabled, "Next Button disabled");
		}
		LOGGER.info("ENDED: Test validateTagInSearchResult");
	}
	
	@Test
	public void validateTagSearchResult(){
		LOGGER.info("STARTED: Test validateTagSearchResult");
		boolean resultCount = searchPage.searchFromTags("Dimension", true);
		Assert.assertTrue(resultCount,
				"Search result displayed after selecting tags from Tags section");
		LOGGER.info("ENDED: Test validateTagSearchResult");
	}
	
	@Test
	public void validateFunctionalTestTag(){
		LOGGER.info("STARTED: Test validateFunctionalTestTag");
		searchPage.navigateToSearchTab();
		Assert.assertTrue(searchPage.validateSearchTagsTag("FunctionalTestTag"), "Validating tag in search page tags");
		LOGGER.info("ENDED: Test validateFunctionalTestTag");
	}
	
	//TODO: Bug [#HDPDGI-320] in application so commented the following test
	/*@Test
	public void validateToolsInSearchResult(){
		LOGGER.info("STARTED: Test validateTagInSearchResult");
		String SEARCH_QUERY = "Table";
		searchPage.searchQuery(SEARCH_QUERY);
		if(searchPage.getSearchResultCount() > 0){
			Assert.assertFalse(SearchPage.isPreviousButtonDisabled, "Button 'Previous' disabled");
			Assert.assertTrue(SearchPage.isNextButtonEnabled, "Button 'Next' enabled");
			searchPage.clickOnTool("BA1");
			Assert.assertTrue(SearchPage.isPreviousButtonEnabled, "Button 'Previous'  enabled");
			Assert.assertFalse(!SearchPage.isNextButtonDisabled, "Button 'Next' disabled");
		}
		LOGGER.info("STARTED: Test validateToolsInSearchResult");
	}*/

}
