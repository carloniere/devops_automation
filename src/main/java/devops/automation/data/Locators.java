package devops.automation.data;

import java.util.Enumeration;
import java.util.Vector;

import devops.automation.utilities.TextUtility;

public class Locators {
	
	private TextUtility textUtil = null;
	private Vector<String> lines = null;

	public Locators(String workspace, String page, String name) {
		textUtil = new TextUtility();
		lines = textUtil.read(workspace + "/lib/locators.txt");
	}
	
	public Locator getLocator(String page, String name) {
		Locator locator = null;
		boolean pageFound = false;
		Enumeration<String> lines = this.lines.elements();
		while(lines.hasMoreElements()) {
			String current = lines.nextElement();
			
			if(pageFound) {
				if(current.contains(name)) {
					String[] array = current.split(" \\| ");
					
				}
			}
			
			if(current.startsWith(">>") && current.contains(page))  {
				if(pageFound) pageFound = false;
				else pageFound = true;
			}
		}
		
		return locator;
	}
	
}
