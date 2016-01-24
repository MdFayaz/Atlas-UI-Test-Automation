package org.atlas.ui.pages;

import org.apache.atlas.utilities.AtlasDriverUtility;
import org.apcahe.atlas.pageobject.AboutDialogElements;
import org.openqa.selenium.support.PageFactory;

public class AboutPage extends HomePage {

	AboutDialogElements aboutDialogElements;
	private String aboutDialogTitle;

	public AboutPage() {
		aboutDialogElements = PageFactory.initElements(driver,
				AboutDialogElements.class);
	}

	public String getAboutDialogTitle() {
		return aboutDialogTitle;
	}

	public void handlePopup() {
		AtlasDriverUtility.customWait(5);
		aboutDialogElements.aboutDialog.click();
		aboutDialogTitle = aboutDialogElements.aboutDialogHeader.getText();
		String versionText = aboutDialogElements.version.getText();
	}
}