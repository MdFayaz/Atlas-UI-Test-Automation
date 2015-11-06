package org.atlas.ui.search;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.atlas.utilities.WebElementWrapper;
import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.testHelper.BaseTestClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;

public class SearchPage extends AtlasDriverUtility {

	private static final Logger LOGGER = Logger.getLogger(BaseTestClass.class);
	static WebElementWrapper webElement;
	static WebDriver driver = getDriver();
	
	@FindBy(css =".mainSearch")
	private WebElement mainSearch;
	
	@FindBy(css = ".form-control")
	private WebElement searchBox;
	
	@FindBy(css = ".btn-success")
	private WebElement searchIcon;
	
	@FindBy(css = ".datatable")
	private WebElement resultTable;
	
	public static SearchPage open() {
		driver.get(AtlasConstants.UI_URL);
		waitForAngularToFinish();
		LOGGER.info("Opened a URL: " + AtlasConstants.UI_URL);
		webElement = new WebElementWrapper();
		return PageFactory.initElements(driver, SearchPage.class);
	}

	public void searchQuery(String text) {
		customWait(10);
		atlasLogo.click();
		mainSearch.click();
		webElement.clearAndSendKeys(searchBox, text);
		searchIcon.click();
		waitForAngularToFinish();
		
	}
	
	@DataProvider(name=AtlasConstants.SEARCH_STRING)
	public static Object[][] searchData(){
		Object[][] object =  new Object[][]{{
				"*",
				"Table"}};
		return object;
	}
}
