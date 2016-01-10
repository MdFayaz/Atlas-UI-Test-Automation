package org.atlas.ui.pages;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.apcahe.atlas.pageobject.TagsPageElements;
import org.atlas.testHelper.BaseTestClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class TagsPage extends AtlasDriverUtility {

	static WebDriver driver = getDriver();
	private static final Logger LOGGER = Logger.getLogger(BaseTestClass.class);
	TagsPageElements tagsPageElements = null;

	public TagsPage() {
		tagsPageElements = PageFactory.initElements(driver,
				TagsPageElements.class);
	}

	public void navigateToTagsTab() {
		waitForPageLoad(driver, 30);
		if (webElement.isElementExists(tagsPageElements.tagTabLink)) {
			tagsPageElements.tagTabLink.click();
			waitUntilPageRefresh(driver);
		} else {
			log.error("Tags tab not present");
		}
	}

	public String getPageHeader() {
		return tagsPageElements.tagPageHeader.getText();
	}

	public boolean validateTagsSections() {
		String labelName = "";
		boolean isTagFieldDisplayed = false;

		int numberOfLables = tagsPageElements.labels.size();

		for (int index = 0; index < numberOfLables; index++) {
			labelName += tagsPageElements.labels.get(index).getText();
		}

		if (labelName.contains("Tag Name") && labelName.contains("Parent Tag")) {
			isTagFieldDisplayed = webElement
					.isElementExists(tagsPageElements.tagNameTextField)
					&& webElement
							.isElementExists(tagsPageElements.parentTagSelectionField);
		}
		if (isTagFieldDisplayed) {
			isTagFieldDisplayed = webElement
					.isElementEnabled(tagsPageElements.addAttributeButton)
					&& webElement.isElementExists(tagsPageElements.saveButton);
		} else {
			LOGGER.error("Tag label fields " + labelName + " not avaialble");
		}
		return isTagFieldDisplayed;
	}

	public TagsPage enterTagName(String tagName) {
		webElement.clearAndSendKeys(tagsPageElements.tagNameTextField, tagName);
		return this;
	}

	public TagsPage selectParentTag(String parentTagName) {
		// TODO: code to select text area for parent tag name
		return this;
	}

	public TagsPage addAddtribute() {
		tagsPageElements.addAttributeButton.click();
		waitUntilElementVisible(tagsPageElements.addAttributeName, 10);
		return this;
	}

	public TagsPage enterAttributeName(String attrName) {
		webElement
				.clearAndSendKeys(tagsPageElements.addAttributeName, attrName);
		return this;
	}

	public TagsPage saveTagName() {
		tagsPageElements.saveButton.click();
		return this;
	}

}
