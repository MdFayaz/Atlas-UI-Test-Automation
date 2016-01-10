package org.apcahe.atlas.pageobject;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPageElements extends BaseTestPageElements {

	@FindBy(css = ".mainSearch")
	public WebElement mainSearch;
	
	@FindBy(css = "a[data-ui-sref='search()'][class='menulink']")
	public WebElement searchTab;
	
	@FindBy(css = "input[type='text']")
	public WebElement searchBox;

	@FindBy(css = "button[type='submit']")
	public WebElement searchIcon;

	@FindBy(css = ".datatable")
	public WebElement resultTable;

	@FindBy(css = ".tabsearchResult")
	public WebElement resultCount;

	@FindBy(css = ".alert")
	public WebElement noResultFound;

	@FindBy(css = ".mainTags")
	public WebElement tagsSection;

	@FindBy(css=".pull-right .pagination")
	public WebElement paginationBoard;
	
	@FindBy(xpath = "//*[contains(@ng-class, 'noPrevious')]")
	public WebElement paginationPrevious;

	@FindBy(xpath = "//*[contains(@ng-class, 'noNext')]")
	public WebElement paginationNext;

	@FindBy(css = ".datatable .tabsearchanchor")
	public List<WebElement> searchResultsTags;
}
