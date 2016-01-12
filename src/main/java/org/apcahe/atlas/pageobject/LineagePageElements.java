package org.apcahe.atlas.pageobject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LineagePageElements extends HomePageElements {

	@FindBy(xpath = "//*[contains(text(), 'Back To Result')]")
	public WebElement backToResultLink;
	
	@FindBy(xpath = "//*[@role='tabpanel']")
	public WebElement tagDetailsSection;
	
	@FindBy(xpath = "//*[contains(@class, 'fa-spinner')]")
	public WebElement graphSpinner;
	
	@FindBy(xpath = "//*[@data-ng-controller='Lineage_ioController']")
	public WebElement graphSection;
	
	@FindBy(xpath = "//*[contains(text(), 'No lineage data found')]")
	public WebElement noLineageDataFound;
	
}
