package org.atlas.ui.pages;

import java.util.HashMap;
import java.util.List;

import org.apache.atlas.objectwrapper.AtlasTableObjectWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.apcahe.atlas.pageobject.SearchPageElements;
import org.atlas.testHelper.AtlasConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;

public class SearchPage extends AtlasDriverUtility {

	private static final Logger LOGGER = Logger.getLogger(SearchPage.class);
	static WebDriver driver = getDriver();

	public SearchPageElements searchPageElements;

	public SearchPage() {
		searchPageElements = PageFactory.initElements(driver,
				SearchPageElements.class);
	}

	public void searchQuery(String text) {
		customWait(10);
		searchPageElements.searchTab.click();
		waitForPageLoad(driver, 10);
		webElement.clearAndSendKeys(searchPageElements.searchBox, text);
		searchPageElements.searchBox.sendKeys(Keys.ENTER);
		long startTime = System.currentTimeMillis();
		waitUntilPageRefresh(driver);
		pageLoadedTime(startTime, text + " query");
	}

	public int getSearchResultCount() {
		return new AtlasTableObjectWrapper(webElement,
				searchPageElements.resultTable).getRowCount();
	}

	public String[] getSearchResultTableHeaders() {
		return new AtlasTableObjectWrapper(webElement,
				searchPageElements.resultTable).getTableHeaders();
	}

	public HashMap<String, HashMap<String, WebElement>> nameToTagsMap = new HashMap<String, HashMap<String, WebElement>>();
	public HashMap<String, WebElement> nameToElement = null;

	private void getAllTagsFromSearchResultTable() {
		List<WebElement> tableRows = searchPageElements.resultTable
				.findElements(By.tagName("tr"));
		LOGGER.info("Search result count: " + tableRows.size());
		for (int index = 1; index < tableRows.size(); index++) {
			List<WebElement> tableTagData = tableRows.get(index).findElements(
					By.tagName("td"));
			WebElement firstCol = tableTagData.get(0).findElement(
					By.tagName("a"));
			List<WebElement> tagsCol = tableTagData.get(3).findElements(
					By.tagName("a"));
			if (tagsCol.size() > 0) {
				nameToElement = new HashMap<String, WebElement>();
				for (WebElement tagsColDetails : tagsCol) {
					String key = tagsColDetails.getText();
					nameToElement.put(key, tagsColDetails);
				}
			}
			String key = firstCol.getText();
			nameToTagsMap.put(key, nameToElement);
		}
//		display();
	}

	/*private void display() {
		for (Entry<String, HashMap<String, WebElement>> tags : nameToTagsMap
				.entrySet()) {
			String key = tags.getKey();
			System.out.println("!!!!!!!!!!!");
			System.out.println(key);
			HashMap<String, WebElement> e = nameToTagsMap.get(key);
			for (Entry<String, WebElement> ee : e.entrySet()) {
				System.out.println(ee.getKey() + "!!!!!" + ee.getValue());
			}
			System.out.println("#######");
		}
	}*/

	private void searchTableForTag(String tagName){
		String name = tagName.substring(0, tagName.indexOf(":"));
		String tagNameToClick = tagName.substring(tagName.indexOf(":") + 1,
				tagName.length());
		if (nameToTagsMap != null && nameToTagsMap.containsKey(name)) {
			HashMap<String, WebElement> tagsMap = nameToTagsMap.get(name);
			WebElement element = tagsMap.get(tagNameToClick);
			element.click();
			waitForPageLoad(driver, 120);
		} else {
			LOGGER.error("Given tagName not found " + tagName);
		}
	}
	public static boolean isPreviousButtonDisabled = false;
	public static boolean isNextButtonDisabled = false;

	public static boolean isPreviousButtonEnabled = false;
	public static boolean isNextButtonEnabled = false;

	public void clickOnTag(String tagName) {
		if (webElement.isElementExists(searchPageElements.paginationBoard)) {
			List<WebElement> paginationFields = searchPageElements.paginationBoard
					.findElements(By.tagName("li"));
			int size = paginationFields.size();
			for (int anchorTagIndex = 1; anchorTagIndex < size - 1; anchorTagIndex++) {
				WebElement listItem = paginationFields.get(anchorTagIndex);
				if (listItem.getAttribute("class").contains("active")) {
					if (paginationFields.get(anchorTagIndex - 1).getText()
							.equals("Previous")) {
						isPreviousButtonDisabled = !searchPageElements.paginationPrevious
								.isEnabled();
						isNextButtonDisabled = searchPageElements.paginationNext
								.isEnabled();
					}
					if (paginationFields.get(size - 1).getText().equals("Next")) {
						isPreviousButtonEnabled = searchPageElements.paginationPrevious
								.isEnabled();
						isNextButtonEnabled = !searchPageElements.paginationNext
								.isEnabled();
					}
				}
				getAllTagsFromSearchResultTable();
				searchTableForTag(tagName);
				WebElement anchorTag = listItem
						.findElement(By.tagName("a"));
				anchorTag.click();
				AtlasDriverUtility.waitUntilPageRefresh(driver);
			}
		}
	}

	@DataProvider(name = AtlasConstants.SEARCH_STRING)
	public static String[][] searchData() {
		String[][] object = new String[][] { {
				// "*",
				"Table", "from Table select Table.name" } };
		return object;
	}

	@DataProvider(name = AtlasConstants.SEARCH_TABLE_HEADERS)
	public static String[][] tableHeaders() {
		String[][] object = new String[][] { { "Name", "Description", "Owner",
				"Tags", "Tools" } };
		return object;
	}

}
