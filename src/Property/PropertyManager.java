package Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	private File propFile;
	
	public PropertyManager(){
		propFile = new File("config.properties");
		System.out.println("Reading from: "+ propFile.getAbsolutePath());
	}
	
	public PropertyManager(String path){
		propFile = new File(path);
		System.out.println("Reading from: "+ propFile.getAbsolutePath());
	}
	
	public Properties getProperties(){
		Properties prop = new Properties();
		try {
			FileInputStream FIS = new FileInputStream(propFile);
			prop.load(FIS);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
