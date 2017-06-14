package EncryptionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil extends EncryptionUtil{
	
	public static final String loadPrivateKey = "Private";
	public static final String loadPublicKey = "Public";

	private static PrivateKey privateKey;
	private static PublicKey publicKey;
	private final String ALGORITHM = "RSA";
	private final int keySize = 2048;
	
	public RSAUtil(){
		super();
	}
	
	public RSAUtil(String path){
		super(path);
	}
	
	public void init(){
		System.out.println("-----------------RSAEncryptUtil--------------------");
		if(!keyGenerated()){
			generateKey();
			System.out.println("-----------------Generating KeyPairs--------------------");
		}
		System.out.println("-----------------Using Exsiting KeyPairs--------------------");
	}
	
	public void init(String key, File file){
		if(key.equalsIgnoreCase(loadPrivateKey)){
			System.out.println("-----------------Loading Specific PrivateKey--------------------");
			System.out.println("Path: "+ file.getAbsolutePath());
			try {
				ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(file));
				privateKey = (PrivateKey) OIS.readObject();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Success");
			
		} else if(key.equalsIgnoreCase(loadPublicKey)){
			System.out.println("-----------------Loading Specific PublicKey--------------------");
			System.out.println("Path: "+ file.getAbsolutePath());
				ObjectInputStream OIS;
				try {
					OIS = new ObjectInputStream(new FileInputStream(file));
					publicKey = (PublicKey) OIS.readObject();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			System.out.println("Success");
		} else {
			System.out.println("-----------------Please check your String input--------------------");
		}
	
	}
	
	public void generateKey (){
		
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
			generator.initialize(keySize);
			KeyPair pair = generator.generateKeyPair();
			
			File publicKeyFile = new File(publicKeyPath);
			File privateKeyFile = new File(privateKeyPath);
			try {
				
			if(publicKeyFile.getParentFile() != null){
				publicKeyFile.getParentFile().mkdirs();
			}	
			
				publicKeyFile.createNewFile();
			if(privateKeyFile.getParentFile() != null){
				privateKeyFile.getParentFile().mkdirs();
			}
				privateKeyFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			ObjectOutputStream OOS = null;
			
			try {
				OOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
				OOS.writeObject(publicKey = pair.getPublic());
				OOS.close();
				
				OOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
				OOS.writeObject(privateKey = pair.getPrivate());
				OOS.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
		
		
		public boolean keyGenerated(){
			File publicKeyFile = new File (publicKeyPath);
			File privateKeyFile = new File (privateKeyPath);
			if(publicKeyFile.exists() && privateKeyFile.exists()){
				try {
					ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(publicKeyFile));
					publicKey = (PublicKey) OIS.readObject();
					OIS.close();
					ObjectInputStream OIS2 = new ObjectInputStream(new FileInputStream(privateKeyFile));
					privateKey = (PrivateKey) OIS2.readObject();
					OIS2.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}


		@Override
		public CipheredObject encryptFile(File file) {
			System.out.println("-----------------Start Encrypt--------------------");
			mode = this.encryptMode;
			byte[] results = encypt(file);
			return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension, results);			
		}

//		@Override
//		public CipheredObject encryptFile(String filename) {
//			System.out.println("-----------------Start Encrypt--------------------");
//			mode = this.encryptMode;
//			File file = new File(filename);
//			try {
//				cipher = Cipher.getInstance(ALGORITHM);
//				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//				cipherAndWriteFile(file);
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (NoSuchPaddingException e) {
//				e.printStackTrace();
//			} catch (InvalidKeyException e) {
//				e.printStackTrace();
//			}
//			
//			return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension, results);						
//		}

		@Override
		public CipheredObject encryptMessage(String text) {
			System.out.println("-----------------Start Encrypt--------------------");
			byte[] results = encypt(text);
			return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension, results);			
		}

		public byte[] encypt(Object object){
			mode = this.encryptMode;
			byte[] results = null;	
			try {
				cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				results = cipherAndWriteFile(object);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}
			return results;
		}
		
		@Override
		public CipheredObject decryptFile(File file) {
			System.out.println("-----------------Start Decrypt--------------------");
			readingFile = file;
			byte[] results = decrypt(file);	
			return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension, results);			
		}
		
		public byte[] decrypt(Object object){
			mode = this.decryptMode;
			byte[] results = null;	
			try {
			    cipher = Cipher.getInstance(ALGORITHM);
			    cipher.init(Cipher.DECRYPT_MODE, privateKey);			    
			    results = cipherAndWriteFile(object);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} finally {
			}
			return results;
		}

//		@Override
//		public CipheredObject decryptFile(String fileName) {
//			System.out.println("-----------------Start Decrypt--------------------");
//			mode = this.decryptMode;
//			try {
//				readingFile = new File(rootPath +"\\"+ encryptFolderName + "\\" + fileName);
//			    cipher = Cipher.getInstance(ALGORITHM);
//			    cipher.init(Cipher.DECRYPT_MODE, privateKey);		    
//			    cipherAndWriteFile(readingFile);
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (NoSuchPaddingException e) {
//				e.printStackTrace();
//			} catch (InvalidKeyException e) {
//				e.printStackTrace();
//			} finally {
//			}
//			return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension, results);			
//		}

}
