package CommonUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import CommonUtil.WriteLog.WriterObject;

public class FileSearcher{
	private String singleMode = "S";
	private String allMode = "A";
	private String searchMode = singleMode;
	private List<File> folders = new ArrayList<File>();
	private List<File> allFileList = new ArrayList<File>();
	private WriterObject Logger;
 
	/*
	 * update for file search interrupted case
	 * 
	 * 
	 * */
	
//	public FileSearcher(){
//		Logger = new WriteLog().init();
//		loadLastFileSearch();
//	}
//	
//	private void loadLastFileSearch(){
//		System.out.println("----------------------------loadLastFileSearch---------------------------------");
//		try {
//			BufferedReader BR = new BufferedReader(new FileReader(new File("FileList")));
//			String line = BR.readLine();
//			while(line != null){
//				if(!line.isEmpty()){
//					System.out.println("Initial Files: "+ line);
//					allFileList.add(new File(line));
//				}
//				line = BR.readLine();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void clearRecord(){
//		Logger.clearLog();
//	}

	public File[] searchFilesContain(File folder, String name){
		System.out.println("----------------------------searchFilesContain---------------------------------");
		
		if(folder.isDirectory()){
			File[] list = folder.listFiles(new mFilter(name));
			return list;
		} else {
			System.out.println("File is not a folder");
			return null;
		}	
	}
	
	public List<File> searchAllInnerFilesContain(File folder, String name){
		System.out.println("----------------------------searchAllInnerFilesContain---------------------------------");	
		System.out.println("Folder: "+ folder.getAbsolutePath());	

		if(folders.size()>0){
			folders.remove(0);
		}
			searchMode = allMode;
		if(folder.isDirectory()){
			File[] list = folder.listFiles(new mFilter(""));
			searchMode = this.singleMode;
			for(File file : Arrays.asList(list)){
				if(file.isFile() && file.getName().contains(name)){
					allFileList.add(file);
				}
			}
			if(folders.size() >0){
				searchAllInnerFilesContain(folders.get(0), name);
			}
			return allFileList;
		} else {
			System.out.println("File is not a folder");
			return null;
		}	
	}
	
	public File[] searchFilesContain(File folder, String[] names){
		System.out.println("----------------------------searchFilesContain---------------------------------");
		
		if(folder.isDirectory()){
			File[] list = folder.listFiles(new mFilter(names));
			return list;
		} else {
			System.out.println("File is not a folder");
			return null;
		}	
	}
	
	public List<File> searchAllInnerFilesContain(File folder, String[] names){
		System.out.println("----------------------------searchAllInnerFilesContain---------------------------------");	
		System.out.println("Folder: "+ folder.getAbsolutePath());	

		if(folders.size()>0){
			folders.remove(0);
		}
			searchMode = allMode;
		if(folder.isDirectory()){
			File[] list = folder.listFiles(new mFilter(""));
			searchMode = this.singleMode;
			for(File file : Arrays.asList(list)){
				if(file.isFile() && checkContain(file.getName(), names, null)){
					allFileList.add(file);
				}
			}
			if(folders.size() >0){
				searchAllInnerFilesContain(folders.get(0), names);
			}
			return allFileList;
		} else {
			System.out.println("File is not a folder");
			return null;
		}	
	}
	
	public List<File> searchAllInnerFilesContainExcept(File folder, String[] names, String[] except){
		if(except == null){
			except = new String[]{};
		}
		System.out.println("----------------------------searchAllInnerFilesContainExcept---------------------------------");	
		System.out.println("Folder: "+ folder.getAbsolutePath());	

		if(folders.size()>0){
			folders.remove(0);
		}
		if(folder.isDirectory()){
			if(checkExcept(folder.getName(), except)){
				searchMode = allMode;
				File[] list = folder.listFiles(new mFilter(""));
				searchMode = this.singleMode;
				for(File file : Arrays.asList(list)){
					if(file.isFile() && checkContain(file.getName(), names, except)){
						allFileList.add(file);
					}
				}
			}
			
			if(folders.size() >0){
				searchAllInnerFilesContainExcept(folders.get(0), names, except);
			}
			return allFileList;
		} else {
			System.out.println("File is not a folder");
			return null;
		}	
	}
	
	public boolean checkContain(String A, String[] B, String[] ex){
		if(ex == null){
			ex = new String[]{};
		}
		for(String s: B){
			if(A.contains(s) && checkExcept(A, ex)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkExcept(String A, String[] ex){
		if(ex == null){
			ex = new String[]{};
		}
		for(String s: ex){
			if(A.contains(s)){
				return false;
			}
		}
		return true;
	}
	
	private class mFilter implements FileFilter{
		private String name;
		private String[] names;
		
		public mFilter(String name){
			System.out.println("----------------------------mFilter---------------------------------");
			this.name = name;
		}
		
		public mFilter(String[] names){
			System.out.println("----------------------------mFilter---------------------------------");
			this.names = names;
		}
		
		@Override
		public boolean accept(File pathname) {
			if(pathname.getName().contains(name)){
				if(pathname.isDirectory()){
					System.out.println("Folder Found: "+ pathname.getAbsolutePath());
					if(searchMode.equalsIgnoreCase(allMode)){
						folders.add(pathname);
					}
					return false;
				} else {
					System.out.println("File Found: "+ pathname.getAbsolutePath());
					return true;		
				}
			} else {
				return false;	
			}
		}
		
	}
	
	
}
