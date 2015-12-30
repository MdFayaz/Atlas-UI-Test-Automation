package org.apcahe.atlas.pageobject;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TagsPageElements extends BaseTestPageElements {

	@FindBy(css = "a[data-ui-sref='tags']")
	public WebElement tagTabLink;

	@FindBy(css = ".appForm h4")
	public WebElement tagPageHeader;

	@FindBy(css = ".control-label")
	public List<WebElement> labels;

	@FindBy(id = "typeName")
	public WebElement tagNameTextField;

	@FindBy(id = "ParentTag")
	public WebElement parentTagSelectionField;

	@FindBy(className = "addAttr")
	public WebElement addAttributeButton;

	@FindBy(id = "attributeId_0")
	public WebElement addAttributeName;
	
	@FindBy(css = ".saveAttr")
	public WebElement saveButton;
	
	@FindBy(css = "span[class='ng-binding ng-scope']")
	public WebElement notificationBanner;
}
