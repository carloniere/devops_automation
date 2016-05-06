package devops.automation.data;

import java.util.Enumeration;
import java.util.Vector;

import devops.automation.utilities.TextUtility;

public class Locators {
	
	private TextUtility textUtil = null;
	private Vector<String> lines = null;

	public Locators(String workspace) {
		textUtil = new TextUtility();
		lines = textUtil.read(workspace + "/lib/locators.txt");
	}
	
	/* 
	 * 
	 */
	
	public Locator getLocator(String dataObject, String page, String name) {
		Locator locator = null;
		boolean pageFound = false;
		Enumeration<String> lines = this.lines.elements();
		while(lines.hasMoreElements()) {
			String current = lines.nextElement();
			
			if(pageFound) {
				
				
				
				
				
				String[] array = current.split(" \\| ");
				if(array[0].contentEquals(name)) {
					if(array.length == 5) {
						locator = new Locator(array[0], array[1], array[2], array[3], array[4]);
						break;
					}
					else {
						locator = new Locator(array[0], array[1], array[2], array[3], "");
						break;
					}
				}
			}
			
			if(current.startsWith(">>"))  {
				if(current.contains(page)) pageFound = true;
				else pageFound = false;
			}
		}
		
		return locator;
	}
	
}
