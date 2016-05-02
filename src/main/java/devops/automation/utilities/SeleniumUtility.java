package devops.automation.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class SeleniumUtility {
	private SeleniumUtility() {
		
	}
	
	public static By getLocator(String type, String value) {
		if(type.contentEquals("id")) return By.id(value);
		else if(type.contentEquals("xpath")) return By.xpath(value);
		else if(type.contentEquals("tagname")) return By.xpath(value);
		else if(type.contentEquals("classname")) return By.className(value);
		else if(type.contentEquals("cssselector")) return By.cssSelector(value);
		else if(type.contentEquals("name")) return By.name(value);
		else if(type.contentEquals("linktext")) return By.linkText(value);
		else return By.partialLinkText(value);
	}
	
	public static WebElement getElement(WebDriverWait wait, String type, String value) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(type,value)));
	}
	
	public static void action(WebDriverWait wait, String name, String type, String locatorType, String locator, String data) {
			WebElement element = getElement(wait, locatorType, locator);
			element.click();
	}
}
