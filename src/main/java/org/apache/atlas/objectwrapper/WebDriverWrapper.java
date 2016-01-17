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

package org.apache.atlas.objectwrapper;

import org.apache.atlas.seleniumtests.HomePageTest;
import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apache.log4j.PropertyConfigurator;
import org.atlas.testHelper.AtlasConstants;
import org.atlas.ui.pages.HomePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.log4testng.Logger;

import com.apache.atlas.listener.AtlasDriverEventListener;
/**
 * Base class for UI test classes.
 */
public class WebDriverWrapper {
	private static final Logger LOGGER = Logger.getLogger(WebDriverWrapper.class);
	
	private static WebDriver ffDriver;
	protected static EventFiringWebDriver driver;
	protected static HomePage homePage;
	protected static WebElementWrapper webElement;
	protected long testExecutionStartTime;
	
	public static void setDriver(EventFiringWebDriver driver){
		WebDriverWrapper.driver = driver;
	}
	
	public static WebDriver getDriver() {
		/*if(AtlasConstants.BROWSER_TYPE.equals("firefox")){
			ffDriver = new FirefoxDriver();
		} else if (AtlasConstants.BROWSER_TYPE.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", "");
			ffDriver = new ChromeDriver();
		}*/
		
		if(ffDriver == null) {
			ffDriver = new FirefoxDriver();
			driver = new EventFiringWebDriver(ffDriver);
			AtlasDriverEventListener driverLister = new AtlasDriverEventListener();
			driver.register(driverLister);
			driver.manage().window().maximize();
			webElement = new WebElementWrapper();
			PropertyConfigurator.configure("log4j.properties");
			homePage = new HomePage();
		}
		return driver;
	}

	public HomePageTest launchApp() {
		long startTime = System.currentTimeMillis();
		driver.get(AtlasConstants.UI_URL);
		LOGGER.info("Opened a URL: " + AtlasConstants.UI_URL);
		AtlasDriverUtility.waitForPageLoad(driver, 30);
		AtlasDriverUtility.pageLoadedTime(startTime, AtlasConstants.UI_URL);
		return PageFactory.initElements(driver, HomePageTest.class);
	}
	 
	public static void closeBrowser() {
		if (driver != null) {
			AtlasDriverUtility.waitForPageLoad(driver, AtlasConstants.DRIVER_TIMEOUT);
			driver.close();
			driver.quit();
			driver = null;
		}
	}
	
	public void switchToWindow(String browserName){
		driver.switchTo().window(browserName);
	}
	
	public Alert switchToAlert(){
		return driver.switchTo().alert();
	}
	
}