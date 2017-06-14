package CommonUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLog {
	
	public WriterObject init(String path){
			return new WriterObject(path);
	}
	
	public WriterObject init(){
			return new WriterObject("FileLIst");
	}
	
	public class WriterObject{
		String path;
		BufferedWriter BW;
		public WriterObject(String path) {
			this.path =path;
			try {
				this.BW = new BufferedWriter(new FileWriter(path, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void write(String s){
			if(BW == null){
				System.out.println("Please check the File List Path");
				return;
			}
			try {
				System.out.println("line: " + s);
				BW.newLine();
				BW.write(s);
				BW.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void clearLog(){
			try {
				BW.write("");
				BW.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
}
