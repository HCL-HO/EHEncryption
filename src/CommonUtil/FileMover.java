package CommonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Property.PropertyManager;

public class FileMover {

	public FileObject getFile(File file){
		System.out.println("----------------------FileMover-----------------------------");
		return new FileObject(file);
	}
	
	
	public class FileObject{
		File file;
		
		public FileObject (File file){
		System.out.println("FileToMove: " + file.getAbsolutePath());
		this.file = file;
		}
		
		public void copyFileTo(String parentPath){
			try {
				FileInputStream FIS = new FileInputStream(file);
				byte[] fileByte = new byte[(int)file.length()];
				FIS.read(fileByte);
				System.out.println("FileMoveTo: " + parentPath);
				FileOutputStream FOS = new FileOutputStream(new File(parentPath+ "\\"+ file.getName()));
				FOS.write(fileByte);
				FOS.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void copyFileTo(){
			String parentPath = new PropertyManager().getProperties().getProperty("MoveFileToPath");
			File parent = new File(parentPath);
			if(parent != null){
				parent.mkdirs();
			}
			try {
				FileInputStream FIS = new FileInputStream(file);
				byte[] fileByte = new byte[(int)file.length()];
				FIS.read(fileByte);
				System.out.println("FileMoveTo: " + parentPath);
				FileOutputStream FOS = new FileOutputStream(new File(parentPath+ "\\"+ file.getName()));
				FOS.write(fileByte);
				FOS.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	

}
