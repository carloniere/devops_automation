package devops.automation.hcm;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Pattern;

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
	private String dataObject = "";
	
	
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
		this.dataObject = dataObject;
		
		Locators locators = new Locators(workspace);
		
		login(getLoginCredentials("URL"), getLoginCredentials("USERID"), getLoginCredentials("PASSWORD"));
		
		this.parse(locators, set);
		
		dispose();
		return false;
	}
	
	
	public void login(String siteURL, String username, String password) {
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                                Logging In . . . .");
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
	
	
	private boolean parse(Locators locators, Vector<String> instructions) {
		int length = instructions.size();
		
		for(int i = 0; i < length; i+=1) {
			String current = instructions.get(i);
			String[] step = current.split("\\|");
			String instruction = step[0];
			if(instruction.toUpperCase().contentEquals("EXCELSHEET")) excelReader.setActiveSheet(step[1]);
			else if(instruction.toUpperCase().contentEquals("CLICK")) clickExecutor(locators, step);
			else if(instruction.toUpperCase().contentEquals("INPUT")) inputExecutor(locators, step);
			else if(instruction.toUpperCase().contentEquals("WAIT")) waitExecutor(locators, step);
			else if(instruction.toUpperCase().contentEquals("SCREENSHOT")) takeScreenShot(dataObject, step);
		}
		return true;
	}
	
	private boolean clickExecutor(Locators locators, String[] instructions) {
		int size = instructions.length;
		

		Locator locator = locators.getLocator(dataObject, currentPage, instructions[1]);
		
		if(size == 2) {
			SeleniumUtility.action(wait, locator.getName(), locator.getType(), locator.getLocatorType(), locator.getLocator(), "");
			if(!locator.getPage().isEmpty()) currentPage = locator.getPage();
		} else if(size > 2) {
			if(size == 4) {
				int rowNum = Integer.valueOf(instructions[2]);
				int colNum = Integer.valueOf(instructions[3]);
				
				String data = excelReader.getCellData(rowNum, colNum, "");
				String locatorValue = locator.getLocator();	
				
				locatorValue = locatorValue.replace("~", data);
				SeleniumUtility.action(wait, locator.getName(), locator.getType(), locator.getLocatorType(), locatorValue, "");
			} else {
				String locatorValue = locator.getLocator();
				for(int i = 2; i < size; i+=1) 
					locatorValue = locatorValue.replaceFirst("~", instructions[i]);
				SeleniumUtility.action(wait, locator.getName(), locator.getType(), locator.getLocatorType(), locatorValue, "");
			}
		}
		
		return true;
	}
	
	private boolean inputExecutor(Locators locators, String[] instructions) {
		int size = instructions.length;
		
		Locator locator = locators.getLocator(dataObject, currentPage, instructions[1]);
		
		if(size == 4) {
			int rowNum = Integer.valueOf(instructions[2]);
			int colNum = Integer.valueOf(instructions[3]);
			
			String data = excelReader.getCellData(rowNum, colNum, "");
			
			SeleniumUtility.action(wait, locator.getName(), locator.getType(), locator.getLocatorType(), locator.getLocator(), data);
		} else if(size == 3) {
			String regex = "\\d+";
			if(!Pattern.matches(regex, instructions[2])) SeleniumUtility.action(wait, locator.getName(), locator.getType(), locator.getLocatorType(), locator.getLocator(), instructions[2]);
			else {
				//TODO
			}
		}
		
		return true;
	}
	
	private boolean waitExecutor(Locators locators, String[] instructions) {
		if(instructions[1].contentEquals("time")) {
			try {
				Thread.sleep(Integer.valueOf(instructions[2]));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(instructions[1].contentEquals("presence")) {
			Locator locator = locators.getLocator(dataObject, currentPage, instructions[2]);
			wait.until(ExpectedConditions.presenceOfElementLocated(SeleniumUtility.getLocator(locator.getLocatorType(), locator.getLocator())));
		} else if(instructions[1].contentEquals("clickable")) {
			Locator locator = locators.getLocator(dataObject, currentPage, instructions[2]);
			wait.until(ExpectedConditions.elementToBeClickable(SeleniumUtility.getLocator(locator.getLocatorType(), locator.getLocator())));
		}
		
		return true;
	}
	
	private boolean takeScreenShot(String dataObject, String[] instructions) {
		SeleniumUtility.takeScreenShot(driver, workspace, "/lib/screenhots/", dataObject, instructions[1]);		
		return true;
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
