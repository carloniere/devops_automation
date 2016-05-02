package devops.automation.hcm;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import devops.automation.data.Instructions;
import devops.automation.data.Locator;
import devops.automation.data.Locators;
import devops.automation.utilities.ExcelReader;
import devops.automation.utilities.SeleniumUtility;

public class Controller {
	// private properties
	private static final long ELEMENT_APPEAR = 120L;
	private WebDriver driver = null;
	private WebDriverWait wait = null;
	private ExcelReader excelReader = null;
	private String workspace = null;
	private String excelpath = null;
	private String currentPage = "main";
	
	
	/* Initialize the WebDriver
	 * @param String seleniumHub - URL of selenium hub
	 * @param String browser - browser name e.g. firefox, chrome, ie
	 */
	public void initializeController(String seleniumHub, String browser, String workspace, String excelPath) {
		try {
			driver = new RemoteWebDriver(new URL(seleniumHub), getCapability(browser));
			wait = new WebDriverWait(driver, ELEMENT_APPEAR);
			this.workspace = workspace;
			this.excelpath = excelPath;
			excelReader = new ExcelReader();
			excelReader.loadExcelFile(excelPath + "/exelon-configurations.xlsx");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* Execute the Service Request
	 * @return boolean - status of the service request, true for success, false for failure
	 * @param String dataObject - Data Object name to be executed 	 * 
	 */
	public boolean execute(String dataObject) {
		
		Instructions ins = new Instructions(workspace);
		Vector<String> set = ins.getInstructionSet(dataObject);
		Enumeration<String> steps = set.elements();
		
		Locators locators = new Locators(workspace);
		
		login(getLoginCredentials("URL"), getLoginCredentials("USERID"), getLoginCredentials("PASSWORD"));
		
		
		
		while(steps.hasMoreElements()) {
			String current = steps.nextElement();
			String[] array = current.split("\\|");
			Locator locator = locators.getLocator(currentPage, array[1]);
			
			if(!locator.getPage().isEmpty()) currentPage = locator.getPage();
			
			SeleniumUtility.action(wait, locator.getName(), locator.getType(), locator.getLocatorType(), locator.getLocator(), "");
			System.out.println(current);
		}
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dispose();
		return false;
	}
	
	
	public void login(String siteURL, String username, String password) {
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                                Logging In . . . .                               ");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("URL loaded: " + siteURL);
		
		driver.get(siteURL);
    	driver.findElement(By.id("userid")).clear();
		driver.findElement(By.id("userid")).sendKeys(username);
		System.out.println("User Id "+username+" entered.");
				
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(password);
		System.out.println("Password ******* entered.");
		
		driver.findElement(By.xpath("//tbody/tr[4]/td/button")).click();
		
		System.out.println("LOGIN SUCCESSFUL!!!");
	}
	
	
	protected DesiredCapabilities getCapability(String browser) {
		try {
			if (browser.contentEquals("firefox")) return DesiredCapabilities.firefox();
			else return DesiredCapabilities.firefox();
			
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public String getLoginCredentials(String input) {		
		excelReader.setActiveSheet("Configurations");
    	int rowNum = 0;
    	int colNum = 0;
		String data = null;
		
		if(input.contentEquals("URL")){
			rowNum = 0;
			colNum = 1;
		}else if (input.contentEquals("USERID")){
			rowNum = 1;
			colNum = 1;
		}else if (input.contentEquals("PASSWORD")){
			rowNum = 2;
			colNum = 1;
		}
		data = excelReader.getCellData(rowNum, colNum, "");
		
		return data;
	}
	
	public void dispose() {
		driver.close();
		driver.quit();
	}
}
