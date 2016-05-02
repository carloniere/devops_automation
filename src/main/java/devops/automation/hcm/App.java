package devops.automation.hcm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Options options = new Options();
        
        options.addOption("o", true, "service request number");
    	options.addOption("w", true, "path to application");
    	options.addOption("e", true, "path to excle file");
    	options.addOption("h", true, "selenium hub url");
    	options.addOption("b", true, "browser");
    	
    	String dataObject = null;
    	String workspace = null;
    	String excelPath = null;
    	String seleniumHub = null;
    	String browser = null;

    	CommandLineParser parser = new DefaultParser();
    	try {
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.hasOption("o")) dataObject = cmd.getOptionValue("o");
			if(cmd.hasOption("w")) workspace = cmd.getOptionValue("w");
			if(cmd.hasOption("e")) excelPath = cmd.getOptionValue("e");	
			if(cmd.hasOption("h")) seleniumHub = cmd.getOptionValue("h");
			if(cmd.hasOption("b")) browser = cmd.getOptionValue("b");
			
			Controller control = new Controller();
			
			control.initializeController(seleniumHub, browser, workspace, excelPath);
			control.execute(dataObject);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
