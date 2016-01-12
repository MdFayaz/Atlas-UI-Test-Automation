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
	public HashMap<String, WebElement> nameToTagElements = null;
	public HashMap<String, WebElement> nameToElement = new HashMap<String, WebElement>();

	
	private void getAllTagsFromSearchResultTable() {
		List<WebElement> tableRows = searchPageElements.resultTable
				.findElements(By.tagName("tr"));
		LOGGER.info("Search result count: " + tableRows.size());
		
		for (int index = 1; index < tableRows.size(); index++) {
			List<WebElement> tableCellData = tableRows.get(index).findElements(
					By.tagName("td"));
			WebElement firstCol = tableCellData.get(0).findElement(
					By.tagName("a"));
			String key = firstCol.getText();
			if (tableCellData.size() > 2) {
				List<WebElement> tagsCol = tableCellData.get(3).findElements(
						By.tagName("a"));
				if (tagsCol.size() > 0) {
					nameToTagElements = new HashMap<String, WebElement>();
					for (WebElement tagsColDetails : tagsCol) {
						String tagName = tagsColDetails.getText();
						nameToTagElements.put(tagName, tagsColDetails);
					}
				}
				nameToTagsMap.put(key, nameToTagElements);
			} else {
				nameToElement.put(key, firstCol);
			}
			
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

	boolean isTagFound = false;
	/*private boolean searchTableForTag(String tagName){
		if(tagName.contains(":")) {
			String name = tagName.substring(0, tagName.indexOf(":"));
			String tagNameToClick = tagName.substring(tagName.indexOf(":") + 1,
					tagName.length());
			if (nameToTagsMap != null && nameToTagsMap.containsKey(name)) {
				HashMap<String, WebElement> tagsMap = nameToTagsMap.get(name);
				tagsMap.get(tagNameToClick).click();
				isTagFound = true;
				waitForPageLoad(driver, 120);
			} else {
				LOGGER.error("Given tagName not found " + tagName);
			}
		} else {
//			System.out.println("in else: " + nameToElement);
//			System.out.println("is true? "+nameToElement.containsKey(tagName));
			if(nameToElement != null && nameToElement.containsKey(tagName)){
				nameToElement.get(tagName).click();
				isTagFound = true;
				waitForPageLoad(driver, 120);
			} else {
				System.out.println("in else tagName: " + tagName);
				System.out.println("isTagFound: "+isTagFound);
			}
		}
		return isTagFound;
	}*/
	
	private void searchTableForTag(String tagName){
		String name = tagName.substring(0, tagName.indexOf(":"));
		String tagNameToClick = tagName.substring(tagName.indexOf(":") + 1,
				tagName.length());
		if (nameToTagsMap != null && nameToTagsMap.containsKey(name)) {
			HashMap<String, WebElement> tagsMap = nameToTagsMap.get(name);
			WebElement element = tagsMap.get(tagNameToClick);
			element.click();
			isTagFound = true;
			waitForPageLoad(driver, 120);
			waitUntilPageRefresh(driver);
		}
	}
	
	public static boolean isPreviousButtonDisabled = false;
	public static boolean isNextButtonDisabled = false;

	public static boolean isPreviousButtonEnabled = true;
	public static boolean isNextButtonEnabled = true;

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
					if(!isTagFound) {
						if (paginationFields.get(size - 1).getText().equals("Next")) {
							isPreviousButtonEnabled = searchPageElements.paginationPrevious
									.isEnabled();
							boolean nextLinkEnabled = searchPageElements.paginationNext.isEnabled();
							isNextButtonEnabled = isTagFound ? nextLinkEnabled : !nextLinkEnabled;
						}
					}
				}
				getAllTagsFromSearchResultTable();
				searchTableForTag(tagName);
				if(isTagFound){
					System.out.println("Expected link " + tagName + " found");
					WebElement nextPage = listItem.findElement(By.tagName("a"));
					nextPage.click();
					break;
				}
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
