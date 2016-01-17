package org.atlas.ui.pages;

import java.util.List;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.apcahe.atlas.pageobject.HomePageElements;
import org.atlas.testHelper.Menu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends AtlasDriverUtility {

	private static final Logger LOGGER = Logger.getLogger(HomePage.class);

	public HomePageElements homePageElements;

	public HomePage() {
		homePageElements = PageFactory.initElements(driver,
				HomePageElements.class);
	}

	public String[] getExpectedPageMenuItems() {
		Menu[] menuLinks = Menu.values();
		String[] expectedMenuItems = new String[menuLinks.length];

		for (int index = 0; index < menuLinks.length; index++) {
			expectedMenuItems[index] = menuLinks[index].getValue();
		}
		return expectedMenuItems;
	}

	private List<WebElement> listOfMenuItems = null;
	
	public List<WebElement> getMenuLinks() {
		if(listOfMenuItems == null) {
			if(webElement.isElementExists(homePageElements.menuBar)) {
				listOfMenuItems =  homePageElements.menuBar.findElements(By.cssSelector("a"));
			}
		} 
		return listOfMenuItems;
	}

	public String[] getActualMenuLinksName() {
		List<WebElement> menuItems = getMenuLinks();
		String[] actualMenuItems = new String[menuItems.size()];
		for (int index = 0; index < menuItems.size(); index++) {
			actualMenuItems[index] = menuItems.get(index).getText();
		}
		return actualMenuItems;
	}

	public boolean validateLink(String menuLink) {
		boolean isLinkClicked = false;
		menuLink = menuLink.toUpperCase();
		AtlasDriverUtility.customWait(15);
		switch (Menu.valueOf(menuLink)) {
		case SEARCH:
			isLinkClicked = clickOnMenu(menuLink);
			break;
		case ABOUT:
			isLinkClicked = clickOnMenu(menuLink);
			new AboutPage().handlePopup();
			break;
		case HELP:
			isLinkClicked = clickOnMenu(menuLink);
			HelpPage.validateHelpPage();
			break;
		case TAGS:
			isLinkClicked = clickOnMenu(menuLink);
			break;
		default:
			break;
		}
		return isLinkClicked;
	}

	private boolean clickOnMenu(String menuName) {
		boolean isLinkEnabled = false;
		for (WebElement menuLink : getMenuLinks()) {
			if (menuLink.getText().equalsIgnoreCase(menuName)) {
				menuLink.click();
				AtlasDriverUtility.waitForPageLoad(getDriver(), 30);
				isLinkEnabled = true;
				LOGGER.info(menuName + " link enabled");
				break;
			}
		}
		if(!isLinkEnabled){
			LOGGER.error("Menu name: " + menuName +" not found to click");
		}
		return isLinkEnabled;
	}

	public void verifyPageLoadSuccessfully() {
		LOGGER.info("ENTERED: verifyPageLoadSuccessfully");
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		AtlasDriverUtility.waitUntilPageRefresh(driver);
		validateLogo();
		getExpectedPageMenuItems();
		LOGGER.info("EXITED: verifyPageLoadSuccessfully");
	}

	public void validateLogo() {
		LOGGER.info("ENTERED: validateLogo");
		AtlasDriverUtility.waitUntilElementVisible(
				homePage.homePageElements.headerController, 10);
		homePage.homePageElements.headerController.click();
		LOGGER.info("EXITED: validateLogo");
	}
}
