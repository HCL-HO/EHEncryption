package EncryptionMethod;

import java.io.File;
import java.util.List;

import CommonUtil.RandomStringGenerator;
import EncryptionUtil.EncryptionUtil;
import EncryptionUtil.RSAUtil;
import EncryptionUtil.SymmetricEncryptUtil;
import Property.PropertyManager;

public class DoubleEncrypt{
	
/*
 * Need 2 keys to decrypt files
 * 1. encrypt files with symmetric key first
 * 2. encrypt symmetric key with RSA key
 * 3. store the encrypted symmetric key in the target folder
 * 4. RSA private key is kept privately
 * 
 * */
		private File publicKey;
		private File fileToEncrypt;
		private int keyLength = 16; 
		private String encryptPath;
		private String keyPath;
		private String keyName= "EHKey.key";
		
		public ReturnKey getRandomSymetricKey(String Path){
			this.encryptPath = Path;
			keyPath = encryptPath + "\\"+ keyName;
			File key = new File(keyPath);
			if(key.exists()){
				System.out.println("-----------------RSA Decrypting Symetric Key--------------------");
				RSAUtil rSAUtil = new RSAUtil();
				rSAUtil.init();
				rSAUtil.decryptFile(new File(keyPath)).writeAndReplace();
				return new ReturnKey(keyPath);
			} else {
				System.out.println("-----------------Generate Random Symetric Key--------------------");
				String secretString = new RandomStringGenerator().getRandomString(keyLength);
				return new ReturnKey(secretString, keyLength, keyPath);		
			}
		}
		
		public ReturnKey getRandomSymetricKey(){
			keyPath = new PropertyManager().getProperties().getProperty("DoubleEncryptionencryptPath") + "\\"+ keyName;
			File key = new File(encryptPath);
			if(key.exists()){
				System.out.println("-----------------RSA Decrypting Symetric Key--------------------");
				RSAUtil rSAUtil = new RSAUtil();
				rSAUtil.init();
				rSAUtil.decryptFile(new File(keyPath)).writeAndReplace();
				return new ReturnKey(keyPath);
			} else {
				System.out.println("-----------------Generate Random Symetric Key--------------------");
				String secretString = new RandomStringGenerator().getRandomString(keyLength);
				return new ReturnKey(secretString, keyLength, keyPath);		
			}
		}
	
	public class ReturnKey{
		SymmetricEncryptUtil SEU;
		
		public ReturnKey(String secret, int len, String path){
			SymmetricEncryptUtil SEU = new SymmetricEncryptUtil();
			SEU.init(secret, keyLength, path); //"C:\\Users\\eric cl ho\\Desktop");
			this.SEU = SEU;
		}
		
		public ReturnKey(String path) {
			SymmetricEncryptUtil SEU = new SymmetricEncryptUtil();
			SEU.init(path);
			this.SEU = SEU;
		}
		
		public ReturnKey encryptReplace(File file){
			SEU.encryptFile(file).writeAndReplace();
			return this;
		}
		
		public ReturnKey decryptReplace(File file){
			SEU.decryptFile(file).writeAndReplace();
			return this;
		}
		
		public ReturnKey encryptFileToPath(File file){
//			SEU = new SysmetricEncryptUtil(symKey, 16);
			SEU.encryptFile(file).writeToPath(encryptPath);
			return this;
		}		
		
		
		public ReturnKey encryptFileToPath(List<File> files){
//			SEU = new SysmetricEncryptUtil(symKey, 16);
			for(File f : files){
				SEU.encryptFile(f).writeToPath(encryptPath);
			}
			return this;
		}		
		
		public ReturnKey decryptFileToPath(File file){
			SEU.decryptFile(file).writeToPath(encryptPath);
			return this;
		}
		
		public ReturnKey decryptFileToPath(List<File> files){
			for(File f: files){
				SEU.decryptFile(f).writeToPath(encryptPath);	
			}
			return this;
		}
		
		public void encryptKeyWithPublicKey(File file){
			RSAUtil RU = new RSAUtil();
			RU.init(RSAUtil.loadPublicKey, file);
			RU.encryptFile(new File(keyPath)).writeAndReplace();
			System.out.println("----------------- Symetric Key is encrypted--------------------");
			System.out.println("Path: "+ SEU.getKeyPath());
		}
		
		public void encryptKeyWithDefaultPublicKey(){
			RSAUtil RU = new RSAUtil();
			RU.init();
			RU.encryptFile(new File(keyPath)).writeAndReplace();
			System.out.println("----------------- Symetric Key is encrypted--------------------");
			System.out.println("Path: "+ encryptPath);
		}
	}

}
