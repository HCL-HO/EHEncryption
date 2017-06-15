package Property;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingManager implements PropertiesKey{
	Properties prop;
	
	
	
	public SettingManager(){
		prop = new PropertyManager().getProperties();
	}
	
	public SettingManager(String path){
		prop = new PropertyManager(path).getProperties();
	}
	
	public void setProperties(String key, String value){
		if(prop.get(key)!= null){
			prop.setProperty(key, value);
		} else {
			prop.put(key, value);	
		}
		save();
	}
	public void removeProperties(String key){
		prop.remove(key);
		save();
	}
	
	private void save(){
		FileOutputStream FOS = null;
		try {
			FOS = new FileOutputStream(new File("config.properties"));
			prop.store(FOS, "test");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				FOS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
