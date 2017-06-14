package EncryptionUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import Property.PropertyManager;


public abstract class EncryptionUtil {

	protected final static String encryptMode= "encrypt";
	protected final static String decryptMode = "decrypt";
	protected String encryptFolderName = "secret";
	protected String decryptFolderName = "unlocked";
	protected String rootPath = "C:\\Users\\eric cl ho\\Desktop\\encrypt";
	protected Cipher cipher;
	protected File readingFile;
	protected static String encryptFileName = "secret";
	protected String mode;
	protected String extension = "";
	protected static String publicKeyPath;
	protected static String privateKeyPath;
	
	public abstract CipheredObject encryptFile(File file);
	public abstract CipheredObject encryptMessage(String text);
	public abstract CipheredObject decryptFile(File file);
	
	public EncryptionUtil(){
		System.out.println("-----------------Loading config---------------------");
		PropertyManager PM = new PropertyManager();
		Properties prop = PM.getProperties();
		initObjects(prop);
		System.out.println("-----------------Finish Loading config---------------------");
	}
	
	public EncryptionUtil(String path){
		System.out.println("-----------------Loading config---------------------");
		PropertyManager PM = new PropertyManager(path);
		Properties prop = PM.getProperties();
		initObjects(prop);
		System.out.println("-----------------Finish Loading config---------------------");
	}
	
	private void initObjects(Properties prop) {
		rootPath = (String) prop.get("RootPath");
		privateKeyPath = (String) prop.get("RSA_private_key_path");
		publicKeyPath = (String) prop.get("RSA_public_key_path");
	}
	
	protected byte[] cipherAndWriteFile(Object o){	
			FileInputStream fis;
			File readingFrom = null;
			byte[] results = null;
			try {
				if(o instanceof File){
					readingFrom =(File)o;
					readingFile = ((File)o); 
					System.out.println("Reading File From: "+ readingFile.getAbsolutePath());
					fis = new FileInputStream((File)o);
					byte[] readFile = new byte[(int) ((File)o).length()];
					fis.read(readFile);			
					results = cipher.doFinal(readFile);	
				} else if (o instanceof String){
					byte[] in = ((String)o).getBytes();
					results = cipher.doFinal(in);	
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				System.out.println("Fail To "+ mode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return results;
		}		
		
		
		public class CipheredObject{
			private final static String encryptMode= "encrypt";
			private final static String decryptMode = "decrypt";
			private String encryptFolderName = "secret";
			private String decryptFolderName = "unlocked";
			private final String rootPath = "C:\\Users\\eric cl ho\\Desktop\\encrypt";
			private Cipher cipher;
			private File readingFile;
			private String encryptFileName = "secret";
			private String mode;
			private String extension = "";
			private byte[] results;
			
			public CipheredObject(String encryptFolderName, String decryptFolderName,
					Cipher cipher, File readingFile, String mode, String extension,
					byte[] results) {
				super();
				this.encryptFolderName = encryptFolderName;
				this.decryptFolderName = decryptFolderName;
				this.cipher = cipher;
				this.readingFile = readingFile;
				this.mode = mode;
				this.extension = extension;
				this.results = results;
			}
			public void write(){
				String fodler = mode == this.encryptMode? encryptFolderName : decryptFolderName;
				String newFile = mode == this.encryptMode? encryptFileName :  "decrypt_" + readingFile.getName();
				int fileNameCount = 0; 
				File targetFile = new File(rootPath + "\\" + fodler + "\\" + newFile+ fileNameCount); 

				while(targetFile.exists()){
					fileNameCount++;
					targetFile = new File(rootPath + "\\" + fodler + "\\" + newFile+ fileNameCount);
				}
				writeFileOutputStream(targetFile);
			}
			
			public void writeAndReplace(){
				if(readingFile == null){
					System.out.println("-----------------Not reading any file---------------------");
					return;
				}
				File replaceFile = new File(readingFile.getAbsolutePath());
				writeFileOutputStream(replaceFile);
			}
			
			public void writeToPath(String parentPath){
				File parent = new File(parentPath);
				if(parent.getParentFile() != null){
					parent.getParentFile().mkdirs();
				}
				
				String fileName = (this.mode == this.encryptMode)? "encrypt_"+ readingFile.getName(): "decrypt_"+ readingFile.getName();
				writeFileOutputStream(new File(parentPath+ "\\" + fileName));
			}
			
			private void writeFileOutputStream(File file){
				if(((byte[])results) == null || ((byte[])results).length == 0){
					System.out.println("ERROR: byte[] size = 0 || = null");
					return;
				}
			FileOutputStream fos;
			File targetFile = file;
				if(targetFile.getParentFile() != null){
					targetFile.getParentFile().mkdirs();
				}
				System.out.println("Target File Path: "+ targetFile.getAbsolutePath());
				
				try {
					fos = new FileOutputStream(targetFile);
					fos.write((byte[]) results);
					fos.flush();
					fos.close();	
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					extension = "";
					if(mode.equalsIgnoreCase(encryptMode)){
						System.out.println("-----------------End Encrypt--------------------");		
					} else if (mode.equalsIgnoreCase(decryptMode)){
						System.out.println("-----------------End Decrypt--------------------");
					} else {
						System.out.println("-----------------Undefined ecryption mode--------------------");
					}
				
				}
			}
			
//			private String checkContaintDot(String extension) {
//				if(!extension.isEmpty()){
//					if(!extension.contains(".")){
//						extension = "." + extension;
//					}
//				}
//				return extension;
//			}
		}
		protected String getEncryptFolderName() {
			return encryptFolderName;
		}

		protected void setEncryptFolderName(String encryptFolderName) {
			this.encryptFolderName = encryptFolderName;
		}

		protected String getDecryptFolderName() {
			return decryptFolderName;
		}

		protected void setDecryptFolderName(String decryptFolderName) {
			this.decryptFolderName = decryptFolderName;
		}
		
		protected String getRootPath (){
			return rootPath;
		}

}
