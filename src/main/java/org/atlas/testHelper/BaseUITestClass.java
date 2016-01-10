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

import org.apache.atlas.objectwrapper.WebElementWrapper;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.log4testng.Logger;

import com.apache.atlas.listener.AtlasDriverEventListener;
/**
 * Base class for UI test classes.
 */
public class BaseUITestClass {
	private static final Logger LOGGER = Logger.getLogger(BaseUITestClass.class);
	

	private static WebDriver ffDriver;
	protected static EventFiringWebDriver driver;
	protected static BaseTestClass baseTestClass;
	protected static WebElementWrapper webElement;
	protected static Logger log;
	long testExecutionStartTime;
	
	public static WebDriver getDriver() {
		if(ffDriver == null) {
			ffDriver = new FirefoxDriver();
			driver = new EventFiringWebDriver(ffDriver);
			AtlasDriverEventListener driverLister = new AtlasDriverEventListener();
			driver.register(driverLister);
			driver.manage().window().maximize();
			webElement = new WebElementWrapper();
			initPageElements();
		}
		return driver;
	}

	public BaseTestClass launchApp() {
		long startTime = System.currentTimeMillis();
		driver.get(AtlasConstants.UI_URL);
		LOGGER.info("Opened a URL: " + AtlasConstants.UI_URL);
		AtlasDriverUtility.waitForPageLoad(driver, 30);
		AtlasDriverUtility.pageLoadedTime(startTime, AtlasConstants.UI_URL);
		return PageFactory.initElements(driver, BaseTestClass.class);
	}
	 
	
	public static void closeBrowser() {
		if (driver != null) {
			AtlasDriverUtility.waitForPageLoad(driver, AtlasConstants.DRIVER_TIMEOUT);
			driver.close();
			driver.quit();
			driver = null;
		}
	}

	private static void initPageElements() {
		loadProps();
		if (baseTestClass == null)
			baseTestClass = PageFactory.initElements(getDriver(),
					BaseTestClass.class);
		PageFactory.initElements(driver, BaseUITestClass.class);
	}

	private static void loadProps() {
		log = Logger.getLogger(BaseUITestClass.class);
		PropertyConfigurator.configure("log4j.properties");
		log.debug("Loaded props");
	}
}
