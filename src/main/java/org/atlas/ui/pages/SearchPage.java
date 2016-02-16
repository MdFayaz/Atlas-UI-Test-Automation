package org.atlas.ui.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.atlas.objectwrapper.AtlasTableObjectWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.atlas.utilities.AtlasFileUtils;
import org.apache.log4j.Logger;
import org.apcahe.atlas.pageobject.SearchPageElements;
import org.atlas.testHelper.AtlasConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class SearchPage extends HomePage {

	private static final Logger LOGGER = Logger.getLogger(SearchPage.class);
	static WebDriver driver = getDriver();

	public SearchPageElements searchPageElements;

	public SearchPage() {
		searchPageElements = PageFactory.initElements(driver,
				SearchPageElements.class);
	}

	public void navigateToSearchTab() {
		customWait(10);
		searchPageElements.searchTab.click();
		waitForPageLoad(driver, 10);
	}

	public void searchQuery(String text) {
		navigateToSearchTab();
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

	public boolean isTableDisplayed() {
		return webElement.isElementExists(searchPageElements.resultTable);
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
	}

	boolean isTagFound = false;

	public boolean searchFromTags(String expectedTag, boolean isResultExpected) {
		List<WebElement> listOfTags = searchPageElements.tagsSection
				.findElements(By.cssSelector(".list-group a"));
		boolean isExpectedTagFound = false;
		for (WebElement tag : listOfTags) {
			if (tag.getText().equals(expectedTag)) {
				tag.click();
				AtlasDriverUtility.waitUntilPageRefresh(driver);
				isExpectedTagFound = true;
				LOGGER.info("Expected tag found in tags section");
				break;
			}
		}
		if (!isExpectedTagFound) {
			LOGGER.error("Expected tag " + expectedTag
					+ " not found in tags section");
		}
		int resultantData = getSearchResultCount();
		if (resultantData > 0 && isResultExpected) {
			return true;
		}
		return isExpectedTagFound;
	}

	private void searchTableForTag(String tagName) {
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

	public void validateSearchResultData(){
		if (webElement.isElementExists(searchPageElements.paginationBoard)) {
			List<WebElement> paginationFields = searchPageElements.paginationBoard
					.findElements(By.tagName("li"));
			int size = paginationFields.size();
			for (int anchorTagIndex = 1; anchorTagIndex < size - 1; anchorTagIndex++) {
				
			}
		}
	}
	
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
					if (!isTagFound) {
						if (paginationFields.get(size - 1).getText()
								.equals("Next")) {
							isPreviousButtonEnabled = searchPageElements.paginationPrevious
									.isEnabled();
							boolean nextLinkEnabled = searchPageElements.paginationNext
									.isEnabled();
							if ((paginationFields.size()) == anchorTagIndex) {
								isNextButtonEnabled = !nextLinkEnabled;
							}
							isNextButtonEnabled = nextLinkEnabled;
						}
					}
				}
				getAllTagsFromSearchResultTable();
				searchTableForTag(tagName);
				if (isTagFound) {
					WebElement nextPage = listItem.findElement(By.tagName("a"));
					nextPage.click();
					break;
				}
				AtlasDriverUtility.waitUntilPageRefresh(driver);
			}
		}
	}
	
	String tagNameContainsGuid ;
	
	private boolean getAllNamColData(String tagName) {
		boolean isCellDataGuid = false;
		List<WebElement> tableRows = searchPageElements.resultTable
				.findElements(By.tagName("tr"));
		LOGGER.info("Search result count: " + tableRows.size() + " for tag Name " + tagName);
		int colIndex = getColIndex(tableRows.get(0), "name");
		for (int index = 1; index < tableRows.size(); index++) {
			List<WebElement> tableCellData = tableRows.get(index).findElements(
					By.tagName("td"));
			if(tableCellData.size() > 0) {
				WebElement firstCol = tableCellData.get(colIndex).findElement(
						By.tagName("a"));
				String key = firstCol.getText();
				if(key.contains("-")) {
					if(getHyperCharCount(key) > 3) {
						isCellDataGuid = true;
						tagNameContainsGuid = key;
						break;
					}
				}
			}
		}
		return isCellDataGuid;
	}
	
	private int getColIndex(WebElement tableRow, String colNameToIndex){
		List<WebElement> tableHeaders = tableRow.findElements(By.tagName("th"));
		for(int colIndex = 0; colIndex <  tableHeaders.size(); colIndex++ ) {
			if(tableHeaders.get(colIndex).getText().equals(colNameToIndex)){
				return colIndex;
			}
		}
		LOGGER.error("No column with name as 'name' found");
		return 0;
	}
	
	private int getHyperCharCount(String colName){
		int counter = 0;
		for(int index = 0; index < colName.length(); index++){
			if(colName.charAt(index) == '-') {
				counter++;
			}
		}
		return counter;
	}
	
	public boolean validateNameCol(String tagName) {
		AtlasDriverUtility.customWait(5);
		boolean isNameColContainsGuid = false;
		if (webElement.isElementExists(searchPageElements.paginationBoard)) {
			List<WebElement> paginationFields = searchPageElements.paginationBoard
					.findElements(By.tagName("li"));
			int size = paginationFields.size();
			for (int anchorTagIndex = 1; anchorTagIndex < size - 1; anchorTagIndex++) {
				WebElement listItem = paginationFields.get(anchorTagIndex);
				if(getAllNamColData(tagName)) {
					isNameColContainsGuid = true;
					break;
				}
				WebElement nextPage = listItem.findElement(By.tagName("a"));
				nextPage.click();
				AtlasDriverUtility.waitUntilPageRefresh(driver);
			}
		} else {
			LOGGER.error("Pagination is not displayed");
		}
		return isNameColContainsGuid;
	}

	public boolean validateSearchTagsTag(String tagsTagName) {
		boolean isTagDisplayed = false;
		for (WebElement we : searchPageElements.tagsSection.findElements(By
				.tagName("a"))) {
			if (we.getAttribute("title").equals(tagsTagName))
				isTagDisplayed = true;
		}
		return isTagDisplayed;
	}

	public void clickOnTool(String tagName) {
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
						boolean nextLinkEnabled = searchPageElements.paginationNext
								.isEnabled();
						isNextButtonEnabled = isTagFound ? nextLinkEnabled
								: !nextLinkEnabled;
					}
				}
				if(!getAllToolsFromSearchResultTable(tagName)) {
					WebElement anchorTag = listItem.findElement(By.tagName("a"));
					anchorTag.click();
					break;
				}
				AtlasDriverUtility.waitUntilPageRefresh(driver);
			}
		}
	}

	private boolean getAllToolsFromSearchResultTable(String tagName) {
		boolean isToolsClicked = false;
		List<WebElement> tableRows = searchPageElements.resultTable
				.findElements(By.tagName("tr"));
		int colIndex = getColIndex(tableRows.get(0), "Tools");
		for (int index = 1; index < tableRows.size(); index++) {
			List<WebElement> tableCellData = tableRows.get(index).findElements(
					By.tagName("td"));
			WebElement firstCol = tableCellData.get(0).findElement(
					By.tagName("a"));
			String key = firstCol.getText();
			if (key.equalsIgnoreCase(tagName)) {
				if (tableCellData.size() > 2) {
					WebElement fifthCol = tableCellData.get(colIndex).findElement(
							By.tagName("img"));
					fifthCol.click();
					isToolsClicked = true;
					break;
				}
			}
		}
		AtlasDriverUtility.customWait(5);
		return isToolsClicked;
	}

	public void addTagFromTools(String tagDefinitionName) {
		WebElement parentDiv1 = driver.findElement(By.xpath("//div[@role='dialog']"));
		parentDiv1.click();
		if (webElement.isElementExists(parentDiv1)) {
			WebElement selectTag = driver.findElement(By.id("tagDefinition"));
			Select slt = new Select(selectTag);
			selectTag.click();
			List<WebElement> options = slt.getOptions();
			for(int index = 0 ; index < options.size(); index++){
				String optionText = options.get(index).getText();
				if(optionText.equals(tagDefinitionName)){
					options.get(index).click();
				}
			}
			slt.selectByValue(tagDefinitionName);
			AtlasDriverUtility.customWait(5);
		} else {
			LOGGER.error("Add Tags dialogs didnt display after clicking from Tools column");
		}
	}
	
	public boolean validateAddedTagFromTools(String tagDefinitionName) {
		boolean isTagAdded = false;
		getAllTagsFromSearchResultTable();
		int tagsCountBeforeAdding = nameToTagsMap.size();
		WebElement saveBtn = driver.findElement(By.xpath("//*[@class='btn btn-success']"));
		saveBtn.click();
		AtlasDriverUtility.customWait(10);
		getAllTagsFromSearchResultTable();
		int tagsCountAfterAdding = nameToTagsMap.size();
		if(webElement.isElementExists(driver.findElement(By.xpath("//div[@ng-show='isError']")))){
			String errorMsg = driver.findElement(By.xpath("//div[@ng-show='isError']")).getText();
			if(errorMsg.contains(tagDefinitionName)){
				isTagAdded = true;
				WebElement cancelBtn = driver.findElement(By
						.xpath("//*[@class='btn btn-warning']"));
				cancelBtn.click();
			}
		} else {
			isTagAdded = (tagsCountBeforeAdding != tagsCountAfterAdding);
		}
		return isTagAdded;
	}
	
	@DataProvider(name = AtlasConstants.INVALID_SEARCH_STRING)
	public static String[][] invalidSearchDataProvider(ITestContext context) {
		Map<String, String> testParams = context.getCurrentXmlTest()
				.getLocalParameters();
		return new String[][] { {testParams.get("invalidSearchData")} };
	}
	
	@DataProvider(name = AtlasConstants.LINEAGE_DATA)
	public static String[][] backToResultLink(ITestContext context) {
		Map<String, String> testParams = context.getCurrentXmlTest()
				.getLocalParameters();
		return new String[][] { {testParams.get("validateLineage")} };
	}

	@DataProvider(name = AtlasConstants.SEARCH_STRING)
	public static Iterator<Object[]> fileDataProvider(ITestContext context) {
		// Get the input file path from the ITestContext
		String inputFile = context.getCurrentXmlTest().getParameter(
				"searchQueries");
		return AtlasFileUtils.getData(inputFile);
	}
	
	@DataProvider(name = AtlasConstants.GUID)
	public static Iterator<Object[]> guidDataProvider(ITestContext context) {
		// Get the input file path from the ITestContext
		String inputFile = context.getCurrentXmlTest().getParameter("guid");
		return AtlasFileUtils.getData(inputFile);
	}
	
	@DataProvider(name = AtlasConstants.TAG_NAME)
	public static Iterator<Object[]> nameDataProvider(ITestContext context) {
		// Get the input file path from the ITestContext
		String inputFile = context.getCurrentXmlTest().getParameter("searchQueries");
		return AtlasFileUtils.getData(inputFile);
	}

	@DataProvider(name = AtlasConstants.SEARCH_TABLE_HEADERS)
	public static String[][] tableHeaders() {
		String[][] object = new String[][] { { "Name", "Description", "Owner",
				"Tags", "Tools" } };
		return object;
	}
}
