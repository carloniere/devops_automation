package devops.automation.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
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
			if(type.contentEquals("button"))
				element.click();
			else if(type.contentEquals("textbox")) {
				element.clear();
				element.sendKeys(data);
				element.clear();
				element.sendKeys(data);
			}
	}
	
	public static boolean takeScreenShot(WebDriver driver, String workspace_path, String screenShotPath, String dataObject, String name) {		
		String datePrefix = new SimpleDateFormat("yyyyMMdd_HHmmssSSS")
				.format(new Date());
		
		String path = workspace_path + screenShotPath + dataObject.replace(" ", "_");
		String ssPath = workspace_path + screenShotPath;
		try {
			File ssDir = new File(ssPath);
			if(!ssDir.exists())
				ssDir.mkdir();
			File dir = new File(path);
			if (!dir.exists())
				dir.mkdir();
			
			WebDriver augmentedDriver = new Augmenter().augment(driver); 
			byte[] screenshot;
			
			screenshot = ((org.openqa.selenium.TakesScreenshot) augmentedDriver)
					.getScreenshotAs(OutputType.BYTES);

			File screenshotFile = new File(MessageFormat.format("{0}/{1}-{2}",
					path, datePrefix, dataObject.replace(" ", "_") + "_" +  name.replace(" ", "_") +  ".png"));
			
			FileOutputStream outputStream = new FileOutputStream(screenshotFile);
			try {
				outputStream.write(screenshot);
			} finally {
				outputStream.close();
			}
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
