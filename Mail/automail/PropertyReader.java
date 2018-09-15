package automail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is responsible for reading the input file and load the property
 */
public class PropertyReader {
	public String filename;
	
	public PropertyReader (String filename){
		this.filename = filename;
	}
	/**
	 * load predefined properties
	 */
	public Properties loadPredefinedProperty() throws IOException {
		
		Properties automailProperties = new Properties();
		// Default properties  "Big,Careful,Standard,Weak";
    	automailProperties.setProperty("Robots", "Standard");
    	automailProperties.setProperty("MailPool", "strategies.SimpleMailPool");
    	automailProperties.setProperty("Floors", "10");
    	automailProperties.setProperty("Fragile", "false");
    	automailProperties.setProperty("Mail_to_Create", "80");
    	automailProperties.setProperty("Last_Delivery_Time", "100");
    	
    	return automailProperties;
	}
	/**
	 * load properties from the given file
	 */
	public Properties loadInputProperty() throws IOException {
		Properties automailProperties = new Properties();
		FileReader inStream = null;
		try {
			inStream = new FileReader(filename);
			automailProperties.load(inStream);
		} finally {
			 if (inStream != null) {
	                inStream.close();
	            }
		}
		return automailProperties;
		
	}
	
}
