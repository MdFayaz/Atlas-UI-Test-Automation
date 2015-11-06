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

import java.util.List;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.atlas.utilities.UIAssert;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base class for test classes.
 */
public class BaseTestClass extends BaseUITestClass {

	public void verifyPageLoadSuccessfully() {
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
		Assert.assertArrayEquals("Menu Items are displayed as expected",
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
				AtlasDriverUtility.waitForAngularToFinish();
				Assert.assertTrue("Search Link clicked", menuLink.isEnabled());
			} else if (menuLink.getText().equalsIgnoreCase(AtlasConstants.TAGS)) {
				menuLink.click();
				AtlasDriverUtility.waitForAngularToFinish();
				Assert.assertTrue("Tags Link clicked", menuLink.isEnabled());
			}
		}
	}

}
