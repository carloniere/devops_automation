package devops.automation.data;

import java.util.Enumeration;
import java.util.Vector;

import devops.automation.utilities.TextUtility;

public class Instructions {
	private TextUtility textUtil = null;
	private Vector<String> lines = null;
	
	public Instructions(String workspace) {
		textUtil = new TextUtility();
		lines = textUtil.read(workspace + "/lib/hcm-usecase");
	}
	
	public Vector<String>  getInstructionSet(String srname){
		Vector<String> instructionset = new Vector<String>();
		boolean foundSr = false;
		//boolean foundType = false;
		
		Enumeration<String> elements = lines.elements();
		
		while(elements.hasMoreElements()) {
			String current = elements.nextElement();
			if(foundSr) {
				if(current.startsWith("[")){
				{
					break;
				}
				} else {
					instructionset.addElement(current);
				}
			} else 
				if(current.contains(srname)) foundSr = true;
		}
		
		return instructionset;
	}
}
