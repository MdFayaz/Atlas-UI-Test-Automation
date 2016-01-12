/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.atlas.seleniumtests;

import static org.atlas.testHelper.AtlasConstants.LINK_CLICKED;

import org.apache.atlas.objectwrapper.WebDriverWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.atlas.utilities.UIAssert;
import org.apache.log4j.Logger;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.testHelper.Menu;
import org.atlas.ui.pages.HomePage;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * Base class for test classes.
 */
public class HomePageTest extends WebDriverWrapper {

	private static final Logger LOGGER = Logger.getLogger(HomePageTest.class);

	private HomePage homePage = null;
	
	@BeforeSuite
	public void init(){
		getDriver();
	}

	@BeforeClass
	public void loadHomeTest() {
		testExecutionStartTime = System.currentTimeMillis();
		homePage = new HomePage();
		homePage.launchApp();
	}

	@AfterSuite
	public void tearDown() {
		closeBrowser();
		AtlasDriverUtility.testSuiteExecutionTime(testExecutionStartTime,
				" execute entire test suite");
		/*
		  SendReport.sendReportByEmail("fayazm@mprglobalsolutions.com",
		  "Fayaz@786", "fayazm@mprglobalsolutions.com", "Test Excution Report",
		  "");
		 */
		/*
		  CopyReport cp = new CopyReport("/hw/atlas/TER/", new
		  File(AtlasConstants.PWD + "\\test-output")); try {
		  cp.saveFilesToServer("ananya@mprhost.com", "changeme12345"); } catch
		  (IOException e) { e.printStackTrace(); }
		 */
	}

	@BeforeMethod
	public void beforeMethod() {
		AtlasConstants.START_TIME = System.currentTimeMillis();
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		String testMethodName = result.getMethod().getMethodName();
		if (result.getStatus() == ITestResult.FAILURE) {
			AtlasDriverUtility.getScreenshot(testMethodName);
		}
		AtlasDriverUtility.testCaseExecutionTime(testMethodName);
	}

	@Test
	public void validateAtlasLogo() {
		LOGGER.info("ENTERED: validateAtlasLogo");
		UIAssert.assertDisplayed(homePage.homePageElements.atlasLogo,
				"Atlas Logo");
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		homePage.validateLogo();
		LOGGER.info("EXITED: validateAtlasLogo");
	}

	//TODO: duplicated with validateMenuLinks
	@Test
	public void validateMenuLinks() {
		LOGGER.info("ENTERED: validatePageMenuBar");
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		String[] actualMenuItems = homePage.getActualMenuLinksName();
		String[] expectedMenuItems = homePage.getExpectedPageMenuItems();
		Assert.assertEquals(actualMenuItems, expectedMenuItems,
				"ValidatePageMenuBar Items");
		LOGGER.info("EXITED: validatePageMenuBar");
	}

	/*@Test
	public void validateMenuLinksClickable() {
		LOGGER.info("ENTERED: validateMenuLinks");
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		Assert.assertTrue(homePage.validateLink(Menu.SEARCH.toString()),
				Menu.SEARCH.toString() + LINK_CLICKED);
		Assert.assertTrue(homePage.validateLink(Menu.TAGS.toString()),
				Menu.TAGS.toString() + LINK_CLICKED);
		Assert.assertTrue(homePage.validateLink(Menu.HELP.toString()),
				Menu.HELP.toString() + LINK_CLICKED);
		Assert.assertTrue(homePage.validateLink(Menu.ABOUT.toString()),
				Menu.ABOUT.toString() + LINK_CLICKED);
		LOGGER.info("EXITED: validateMenuLinks");
	}*/

	@Test 
	public void validateHelpLink() {
		LOGGER.info("ENTERED: validateHelpLink");
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		Assert.assertTrue(homePage.validateLink(Menu.HELP.toString()),
				Menu.HELP.toString() + LINK_CLICKED);
		LOGGER.info("EXITED: validateHelpLink");
	}
	
	@Test 
	public void validateAboutLink() {
		LOGGER.info("ENTERED: validateAboutLink");
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		Assert.assertTrue(homePage.validateLink(Menu.ABOUT.toString()),
				Menu.ABOUT.toString() + LINK_CLICKED);
		LOGGER.info("EXITED: validateAboutLink");
	}
}
