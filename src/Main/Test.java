package Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;


public class Test {

	/**
	   * String to hold name of the encryption algorithm.
	   */
	  public static final String ALGORITHM = "RSA";

	  /**
	   * String to hold the name of the private key file.
	   */
	  public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";

	  /**
	   * String to hold name of the public key file.
	   */
	  public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";

	  /**
	   * Generate key which contains a pair of private and public key using 1024
	   * bytes. Store the set of keys in Prvate.key and Public.key files.
	   * 
	   * @throws NoSuchAlgorithmException
	   * @throws IOException
	   * @throws FileNotFoundException
	   */
	  public static void generateKey() {
	    try {
	      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	      keyGen.initialize(1024);
	      final KeyPair key = keyGen.generateKeyPair();

	      File privateKeyFile = new File(PRIVATE_KEY_FILE);
	      File publicKeyFile = new File(PUBLIC_KEY_FILE);

	      // Create files to store public and private key
	      if (privateKeyFile.getParentFile() != null) {
	        privateKeyFile.getParentFile().mkdirs();
	      }
	      privateKeyFile.createNewFile();

	      if (publicKeyFile.getParentFile() != null) {
	        publicKeyFile.getParentFile().mkdirs();
	      }
	      publicKeyFile.createNewFile();

	      // Saving the Public key in a file
	      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
	          new FileOutputStream(publicKeyFile));
	      publicKeyOS.writeObject(key.getPublic());
	      publicKeyOS.close();

	      // Saving the Private key in a file
	      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
	          new FileOutputStream(privateKeyFile));
	      privateKeyOS.writeObject(key.getPrivate());
	      privateKeyOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	  }

	  /**
	   * The method checks if the pair of public and private key has been generated.
	   * 
	   * @return flag indicating if the pair of keys were generated.
	   */
	  public static boolean areKeysPresent() {

	    File privateKey = new File(PRIVATE_KEY_FILE);
	    File publicKey = new File(PUBLIC_KEY_FILE);

	    if (privateKey.exists() && publicKey.exists()) {
	      return true;
	    }
	    return false;
	  }

	  /**
	   * Encrypt the plain text using public key.
	   * 
	   * @param text
	   *          : original plain text
	   * @param key
	   *          :The public key
	   * @return Encrypted text
	   * @throws java.lang.Exception
	   */
	  public static byte[] encrypt(String text, PublicKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return cipherText;
	  }

	  /**
	   * Decrypt text using private key.
	   * 
	   * @param text
	   *          :encrypted text
	   * @param key
	   *          :The private key
	   * @return plain text
	   * @throws java.lang.Exception
	   */
	  public static String decrypt(byte[] text, PrivateKey key) {
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      dectyptedText = cipher.doFinal(text);

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }

	    return new String(dectyptedText);
	  }

	  /**
	   * Test the EncryptionUtil
	   */
		private static String encryptFolderName = "secret";
		private static String decryptFolderName = "unlocked";
		private final static String encryptMode= "encrypt";
		private final static String decryptMode = "decrypt";
	  public static void main(String[] args) {

	    try {

	      // Check if the pair of keys are present else generate those.
	      if (!areKeysPresent()) {
	        // Method generates a pair of keys using the RSA algorithm and stores it
	        // in their respective files
	        generateKey();
	      }

	      final String originalText = "Text to be gdhfsgh hencrypted ";
	      ObjectInputStream inputStream = null;

	      // Encrypt the string using the public key
	      inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
	      final PublicKey publicKey = (PublicKey) inputStream.readObject();
	      final byte[] cipherText = encrypt(originalText, publicKey);

	      // Decrypt the cipher text using the private key.
	      inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
	      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
	      
	      writeFile(cipherText, encryptMode);
	      	File readingFile = new File("C:/keys/" + "secret0");
			FileInputStream fis = new FileInputStream(readingFile);
			byte[] readByte = new byte[(int) readingFile.length()];
			fis.read(readByte);
			fis.close();
		 System.out.println("TEST: " + readByte);

		final String plainText = decrypt(readByte, privateKey);

	      writeFile(plainText, decryptMode);

	      // Printing the Original, Encrypted and Decrypted Text
	      System.out.println("Original: " + originalText);
	      System.out.println("Encrypted: " +cipherText.toString());
	      System.out.println("Decrypted: " + plainText);

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

		
		public static void writeFile(Object text, String mode){	
			String fodler = mode == encryptMode? encryptFolderName : decryptFolderName;
			String newFile = mode == encryptMode? "secret" : "decrypt";
			int fileNameCount = 0; 
//			while(new File("C:/keys/" + newFile+ fileNameCount).exists()){
//				fileNameCount++;
//			}
			File targetFile = new File("C:/keys/"  + newFile+ fileNameCount );
			if(targetFile.getParentFile() != null){
				targetFile.getParentFile().mkdirs();
			}
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(targetFile);
				if(mode == encryptMode){
					fos.write((byte[]) text);
				} else {
					fos.write(((String) text).getBytes());
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

}
