package org.atlas.ui.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apcahe.atlas.pageobject.LineagePageElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

public class LineagePage extends AtlasDriverUtility {

	private static Logger LOGGER = Logger.getLogger(LineagePage.class);

	LineagePageElements lineagePageElements;
	SearchPage searchPage;

	public LineagePage() {
		searchPage = new SearchPage();
		lineagePageElements = PageFactory.initElements(driver,
				LineagePageElements.class);
	}

	public boolean validateBackToPageLink(){
		boolean isLinkEnabled = false;
		if(webElement.isElementExists(lineagePageElements.backToResultLink)) {
			isLinkEnabled = webElement.isElementEnabled(lineagePageElements.backToResultLink);
		}
		return isLinkEnabled;
	}
	
	public void clickOnBackToPageLink(){
		AtlasDriverUtility.customWait(10);
		if(webElement.isElementExists(lineagePageElements.backToResultLink)) {
			lineagePageElements.backToResultLink.click();
		}
		AtlasDriverUtility.customWait(10);
	}
	
	public boolean isPageDataDisplayed(){
		boolean isTagDetailsExists = false;
		if(webElement.isElementExists(lineagePageElements.tagDetailsSection)) {
			isTagDetailsExists = webElement.isElementEnabled(lineagePageElements.tagDetailsSection);
		}
		return isTagDetailsExists;
	}
	
	private boolean isGraphSectionLoaded() {
		AtlasDriverUtility.customWait(3);
		return AtlasDriverUtility.waitUntilElementVisible(
				lineagePageElements.graphSpinner, 20).isDisplayed();
	}
	
	public boolean validateGraphSection() {
		boolean isElementsLoadedProperly = false;
		WebElement graphSection = lineagePageElements.graphSection;
		if (isGraphSectionLoaded()
				&& webElement.isElementExists(graphSection)) {
			String resetButtonText = graphSection.findElement(By.tagName("button")).getText();
			if(resetButtonText!= null && resetButtonText.equals("Reset")){
				isElementsLoadedProperly = true;
			}
			if (!webElement
					.isElementExists(lineagePageElements.noLineageDataFound)) {
				List<WebElement> graphTags = graphSection.findElements(By
						.tagName("g"));
				int imgCount = 0;
				if (graphTags.size() > 0) {
					for (WebElement gTag : graphTags) {
						gTag.findElement(By.tagName("img"));
						imgCount++;
					}
					isElementsLoadedProperly = (imgCount > 0) ? isElementsLoadedProperly = true
							: false;
				}
			}
		}
		return isElementsLoadedProperly;
	}
	
	public void validateImage() {
		searchPage.searchQuery("Fact");
		clickOnSearchData("0de1ae11-b498-484f-965f-1019501d3870");
	}
	
	public static boolean isPreviousButtonDisabled = false;
	public static boolean isNextButtonDisabled = false;
	public static boolean isPreviousButtonEnabled = false;
	public static boolean isNextButtonEnabled = false;
	boolean isTagFound = false;
	
	public void clickOnSearchData(String tagName) {
		AtlasDriverUtility.customWait(10);
		if (webElement.isElementExists(lineagePageElements.paginationBoard)) {
			List<WebElement> paginationFields = lineagePageElements.paginationBoard
					.findElements(By.tagName("li"));
			int size = paginationFields.size();
			for (int anchorTagIndex = 1; anchorTagIndex < size - 1; anchorTagIndex++) {
				WebElement listItem = paginationFields.get(anchorTagIndex);
				if(isTagFound){
					break;
				}
				if (listItem.getAttribute("class").contains("active")) {
					if (paginationFields.get(anchorTagIndex - 1).getText()
							.equals("Previous")) {
						isPreviousButtonDisabled = !lineagePageElements.paginationPrevious
								.isEnabled();
						isNextButtonDisabled = lineagePageElements.paginationNext
								.isEnabled();
					}
					if (paginationFields.get(size - 1).getText().equals("Next")) {
						isPreviousButtonEnabled = lineagePageElements.paginationPrevious
								.isEnabled();
						isNextButtonEnabled = !lineagePageElements.paginationNext
								.isEnabled();
					}
				}
				getAllTagsFromSearchResultTable();
				boolean a = searchTableForTag(tagName);
				System.out.println("anchor: " + a);
				WebElement nextPage = listItem.findElement(By.tagName("a"));
				nextPage.click();
				AtlasDriverUtility.waitUntilPageRefresh(driver);
			}
		}
	}

	public HashMap<String, WebElement> nameToElement = new HashMap<String, WebElement>();

	private void getAllTagsFromSearchResultTable() {
		List<WebElement> tableRows = lineagePageElements.resultTable
				.findElements(By.tagName("tr"));
		LOGGER.info("Search result count: " + tableRows.size());

		for (int index = 1; index < tableRows.size(); index++) {
			List<WebElement> tableCellData = tableRows.get(index).findElements(
					By.tagName("td"));
			WebElement firstCol = tableCellData.get(0).findElement(
					By.tagName("a"));
			String key = firstCol.getText();
			nameToElement.put(key, firstCol);
		}
		display();
	}

	private void display() {
		HashMap<String, WebElement> e = nameToElement;
		for (Entry<String, WebElement> ee : e.entrySet()) {
			System.out.println(ee.getKey() + "!!!!!" + ee.getValue());
		}
		System.out.println("#######");
	}
	
	private boolean searchTableForTag(String tagName) { 
		if (nameToElement != null && nameToElement.containsKey(tagName)) {
			nameToElement.get(tagName).click();
			isTagFound = true;
			waitForPageLoad(driver, 120);
		}
		return isTagFound;
	}
}
