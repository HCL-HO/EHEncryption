package Main;
import java.io.File;
import java.util.List;

import CommonUtil.FileMover;
import CommonUtil.FileSearcher;
import CommonUtil.MyFilter;
import CommonUtil.RandomStringGenerator;
import EncryptionMethod.DoubleEncrypt;
import EncryptionUtil.EncryptionUtil;
import EncryptionUtil.RSAUtil;
import EncryptionUtil.SymmetricEncryptUtil;
import Property.PropertyManager;


public class Process {

	private static File file = new File("C:\\Users\\eric cl ho\\Desktop\\export.xlsx");
	public static void main(String[] args) {

		DoubleEncrypt DE = new DoubleEncrypt();
//
		String[] except = new String[]{
			"test","UAT", "Mapping", "Deployment", "Release Note", "archive", "3000-12-3-7 PMI Search Enhancement"	
		};
		
		List<File> files = new FileSearcher().searchAllInnerFilesContainExcept(new File
				("C:\\Users\\eric cl ho\\Desktop\\PDF"), MyFilter.documents, null);
		
		System.out.println("-----------------------Files size: " + files.size() + "--------------------------------------");
		for(File f : files){
			System.out.println("File: "+ f.getAbsolutePath());
			DE.getRandomSymetricKey(f.getParent()).decryptReplace(f).encryptKeyWithDefaultPublicKey();
		}
//		
//		List<File> files1 = new FileSearcher().searchAllInnerFilesContainExcept(new File
//				("C:\\Users\\eric cl ho\\Desktop\\PDF"), MyFilter.documents, null);
//		
//		System.out.println("-----------------------Files size: " + files1.size() + "--------------------------------------");
//		for(File f : files1){
//			System.out.println("File: "+ f.getAbsolutePath());
//		}
//		DE.getRandomSymetricKey("C:\\Users\\eric cl ho\\Desktop\\PDF").decryptFileToPath(files1).encryptKeyWithDefaultPublicKey();

//		System.out.println("-----------------------FINISH--------------------------------------");
//		DoubleEncrypt DE = new DoubleEncrypt();
//		DE.getRandomSymetricKey("C:\\Users\\eric cl ho\\Desktop\\encrypt\\SysmetricKey.key")
//		.encryptFileToPath(files, "C:\\Users\\eric cl ho\\Desktop\\encrypt\\test")
//		.encryptKeyWithDefaultPublicKey();
		
		
		
//		DE.getRandomSymetricKey("C:\\Users\\eric cl ho\\Desktop\\encrypt\\SysmetricKey.key")
//		.encryptFileToPath(file, "C:\\Users\\eric cl ho\\Desktop\\encrypt\\encryptFile")
//		.encryptKeyWithDefaultPublicKey();	
//		File[] filess = new FileSearcher().searchFilesExtension(new File("C:\\Users\\eric cl ho\\Desktop\\encrypt\\test"), "");
//	
//		DE.getRandomSymetricKey("C:\\Users\\eric cl ho\\Desktop\\encrypt\\SysmetricKey.key")
//		.decryptFileToPath(filess, "C:\\Users\\eric cl ho\\Desktop\\encrypt\\test")
//				.encryptKeyWithDefaultPublicKey();
	}
}
