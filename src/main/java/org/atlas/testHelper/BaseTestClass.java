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

package org.atlas.testHelper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.atlas.utilities.UIAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
/**
 * Base class for test classes.
 */
public class BaseTestClass extends BaseUITestClass {

	@FindBy(css = "div[data-ng-controller='HeaderController']")
	protected WebElement headerController;

	@FindBy(css = ".mainLogo")
	protected WebElement atlasLogo;

	@FindBy(css = ".menuBar")
	protected WebElement menuBar;
	
	@BeforeSuite
	public void setup() {
		testExecutionStartTime = System.currentTimeMillis();
	}

	@AfterSuite
	public void tearDown() {
		closeBrowser();
		AtlasDriverUtility.testSuiteExecutionTime(testExecutionStartTime,
				" execute entire test suite");
		/*SendReport.sendReportByEmail("fayazm@mprglobalsolutions.com",
				"Fayaz@786", "fayazm@mprglobalsolutions.com",
				"Test Excution Report", "");*/
		/*CopyReport cp = new CopyReport("/hw/atlas/TER/", new File(AtlasConstants.PWD + "\\test-output"));
		try {
			cp.saveFilesToServer("ananya@mprhost.com", "changeme12345");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public void verifyPageLoadSuccessfully() {
		AtlasDriverUtility.waitForPageLoad(driver, 60);
		AtlasDriverUtility.waitUntilPageRefresh(driver);
		validateAtlasLogo();
		validatePageMenuBar();
		validateLinks();
	}
	
	public void validateAtlasLogo() {
		AtlasDriverUtility.waitUntilElementVisible(headerController, 10);
		headerController.click();
		UIAssert.assertDisplayed(atlasLogo, "Atlas Logo");
	}

	static String[] expectedMenuItems = { AtlasConstants.SEARCH,
			AtlasConstants.TAGS, AtlasConstants.HELP, AtlasConstants.ABOUT };

	public void validatePageMenuBar() {
		String[] actualMenuItems = getMenuLinksName();
		assertArrayEquals("Menu Items are displayed as expected",
				expectedMenuItems, actualMenuItems);
	}

	private List<WebElement> getMenuLinks() {
		return menuBar.findElements(By.cssSelector("a"));
	}

	private String[] getMenuLinksName() {
		List<WebElement> menuItems = getMenuLinks();
		String[] actualMenuItems = new String[menuItems.size()];
		for (int index = 0; index < menuItems.size(); index++) {
			actualMenuItems[index] = menuItems.get(index).getText();
		}
		return actualMenuItems;
	}

	public void validateLinks() {
		for (WebElement menuLink : getMenuLinks()) {
			if (menuLink.getText().equalsIgnoreCase(AtlasConstants.SEARCH)) {
				menuLink.click();
				AtlasDriverUtility.waitForPageLoad(getDriver(), 30);
				assertTrue("Search Link clicked", menuLink.isEnabled());
			} else if (menuLink.getText().equalsIgnoreCase(AtlasConstants.TAGS)) {
				menuLink.click();
				AtlasDriverUtility.waitForPageLoad(getDriver(), 30);
				assertTrue("Tags Link clicked", menuLink.isEnabled());
			}
		}
	}

}
