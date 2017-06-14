package EncryptionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricEncryptUtil extends EncryptionUtil{
	private String algo = "AES";
	private SecretKeySpec secretKeySpec;
	private String SysmetricKey = "FLYMETOTHEMOON";
	private int length = 16;
	private byte[] key;
	private String keyPath = rootPath + "\\" + "SymetricKeyFolder" + "\\"+ "sKey.key";

	
	public SymmetricEncryptUtil(){
		super();
	};
	
	public SymmetricEncryptUtil(String path){
		super(path);
	};
	
	public void init(){
		System.out.println("-----------------SysmetricEncryptUtil--------------------");
		generateKey(SysmetricKey, length);
	}
	
	
	public void init(String key, int len){
		System.out.println("-----------------SysmetricEncryptUtil--------------------");
		generateKey(key, len);
	}
	
	public void init(String key, int len, String path){
		System.out.println("-----------------SysmetricEncryptUtil--------------------");
		this.keyPath = path;
		generateKey(key, len);
	}
	
	public void init(String keyPath){
		if(new File(keyPath).exists()){
				System.out.println("-----------------Loading existing Symetric Key--------------------");
				ObjectInputStream OIS;
				try {
					OIS = new ObjectInputStream(new FileInputStream(new File(keyPath)));
					secretKeySpec = (SecretKeySpec) OIS.readObject();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		} else {
			System.out.println("Symetric Key not exists, loading default key");
			init();
		}
	}
	
	private void generateKey(String secret, int length){		
		if(new File(keyPath).exists()){
			try {
				System.out.println("-----------------Loading existing Symetric Key--------------------");
				ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(new File(keyPath)));
				secretKeySpec = (SecretKeySpec) OIS.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("-----------------Generating SymetricKey--------------------");
			SysmetricKey = secret;
			if(secret.length()<length){
				int suffix = length - secret.length();
				for(int i = 0; i< suffix; i++){
					secret += " ";
				}
			}
			try {
				key = secret.substring(0, length).getBytes("UTF-8");
				secretKeySpec = new	SecretKeySpec(key, algo);
				cipher = Cipher.getInstance(algo);
				
				File keyFile = new File(keyPath);		
				
				if(keyFile.getParentFile() != null){
					keyFile.getParentFile().mkdirs();
				}
				
				ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(keyFile));
				OOS.writeObject(secretKeySpec);
				OOS.flush();
				OOS.close();
//				if(secureMode.equals("SECURE")){
//					System.out.println("SecureMode-----------------Encrypting SymetricKey--------------------");
//					new RSAUtil().encryptFile(keyFile);
//				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("SymetricKey is Stored in: "+ keyPath);
	}

	@Override
	public CipheredObject encryptFile(File file) {
		System.out.println("-----------------Start Encrypt--------------------");
		byte[] results = encrypt(file);
		
		return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension,results);	
	}

//	@Override
//	public CipheredObject encryptFile(String filename) {
//		return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension,results);
//		
//	}

	@Override
	
	public CipheredObject encryptMessage(String text) {
		System.out.println("-----------------Start Encrypt--------------------");
		byte[] results = encrypt(text);
		
		return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension,results);
	}

	private byte[] encrypt(Object object){
		mode = this.encryptMode;
		byte[] results = null;	
		try {
			cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
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
		byte[] results = decrypt(file);
		return new CipheredObject(encryptFolderName, decryptFolderName,cipher,  readingFile,  mode,  extension,results);				
	}

	
	private byte[] decrypt(Object object){
		mode = this.decryptMode;
		byte[] results = null;	
		try {
			cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
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
	
	public String getSysmetricKey() {
		return SysmetricKey;
	}
	public void setSysmetricKey(String sysmetricKey) {
		SysmetricKey = sysmetricKey;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getKeyPath() {
		return keyPath;
	}
	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}
}
