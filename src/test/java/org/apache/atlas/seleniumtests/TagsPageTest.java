package org.apache.atlas.seleniumtests;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.Logger;
import org.atlas.testHelper.BaseTestClass;
import org.atlas.ui.pages.TagsPage;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//@Listeners(JyperionListener.class)
public class TagsPageTest extends BaseTestClass {

	private static final Logger LOGGER = Logger.getLogger(TagsPageTest.class);
	private TagsPage tagsPage = null;
	long testExecutionStartTime;

	@BeforeClass
	public void loadObject(){
		tagsPage = new TagsPage();
		tagsPage.launchApp();
	}
	
	@AfterMethod
	public void getMethodName(ITestResult result){
		System.out.println("in getMethodName: ");
		System.out.println("result getMethodName: " +  result.getMethod().getMethodName());
		System.out.println("status: " + result.getStatus());
		System.out.println("Failure status: ");
		if(result.getStatus() == result.FAILURE){
			AtlasDriverUtility.getScreenshot(result.getMethod().getMethodName());
		}
	}
	
	@Test
	public void testPageElementsFromTagsTab() {
		LOGGER.info("STARTED: Test testPageElementsFromTagsTab");
		baseTestClass.verifyPageLoadSuccessfully();
		LOGGER.info("ENDED: Test testPageElementsFromTagsTab");
	}
	
	@Test
	public void validateTagsPage(){
		LOGGER.info("STARTED: Test validateTagsPage");
		tagsPage.navigateToTagsTab();
		Assert.assertTrue(tagsPage.validateTagsSections(), "Validating tags page sections");
		LOGGER.info("ENDED: Test validateTagsPage");
	}
	
	@Test
	public void addAttribute(){
		LOGGER.info("STARTED: Test add Attribute");
		tagsPage.navigateToTagsTab();
		tagsPage.
		enterTagName("TestInput").
		addAddtribute().
		enterAttributeName("TestAttr").
		saveTagName();
		LOGGER.info("ENDED: Test add Attribute");
	}
	
	@Test
	public void validateTagDefinition(){
		LOGGER.info("STARTED: Test validation of Tag Definition");
		tagsPage.navigateToTagsTab();
		LOGGER.info("ENDED: Test validation of Tag Definition");
	}

}
