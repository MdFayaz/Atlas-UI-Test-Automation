package org.apache.atlas.seleniumtests;

import org.apache.atlas.objectwrapper.WebDriverWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.ui.pages.TagsPage;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

//@Listeners(JyperionListener.class)
public class TagsPageTest extends WebDriverWrapper {

	private static final Logger LOGGER = Logger.getLogger(TagsPageTest.class);
	private TagsPage tagsPage = null;
	long testExecutionStartTime;

	@BeforeClass
	public void loadTagsTest(){
		tagsPage = new TagsPage();
		tagsPage.launchApp();
	}
	
	@AfterMethod
	public void getMethodName(ITestResult result){
		if(result.getStatus() == ITestResult.FAILURE){
			AtlasDriverUtility.getScreenshot(result.getMethod().getMethodName());
		}
	}
	
	@BeforeMethod
	public void beforeMethod(){
		AtlasConstants.START_TIME = System.currentTimeMillis();
	}
	
	@Test
	public void testPageElementsFromTagsTab() {
		LOGGER.info("STARTED: Test testPageElementsFromTagsTab");
		homePage.verifyPageLoadSuccessfully();
		LOGGER.info("ENDED: Test testPageElementsFromTagsTab");
	}
	
	@Test
	public void validateTagsPage(){
		LOGGER.info("STARTED: Test validateTagsPage");
		tagsPage.navigateToTagsTab();
		boolean isTagsSectionExist = tagsPage.validateTagsSections();
		Assert.assertTrue(isTagsSectionExist, "Validating tags page sections");
		LOGGER.info("ENDED: Test validateTagsPage");
	}
	
	@Test
	public void addAttribute(){
		LOGGER.info("STARTED: Test Create Attribute");
		tagsPage.navigateToTagsTab();
		tagsPage.
		enterTagName("TestInput").
		addAddtribute().
		enterAttributeName("TestAttr").
		saveTagName();
		LOGGER.info("ENDED: Test Create Attribute");
	}
}
