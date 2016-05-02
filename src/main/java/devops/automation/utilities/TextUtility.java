package devops.automation.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class TextUtility {
	
	private BufferedReader br = null;
	private Vector<String> lines = null;
	private int size = 0;
	
	public Vector<String> read(String path) {
		try {
			lines = new Vector<String>();
			String sCurrentLine = null;
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
				lines.addElement(sCurrentLine);
			}
			return lines;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	
}
