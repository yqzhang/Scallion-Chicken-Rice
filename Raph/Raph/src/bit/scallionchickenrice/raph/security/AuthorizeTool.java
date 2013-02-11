package bit.scallionchickenrice.raph.security;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class AuthorizeTool {
	private final static String BOOTFILE = "boot.ini";
	private final static String PUBKEYFILE = "public.dat";
	private final static String PRIKEYFILE = "private.dat";
	
	public static boolean validate() throws IOException {
		File file = new File(BOOTFILE);
		if(!file.exists()) {
			return false;
		}
		
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		byte[] author = new byte[bis.available()];
		bis.read(author);
		bis.close();

		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(new File(PRIKEYFILE)));
			byte[] de = cipher.doFinal(author);
			if((new String(de)).equals(getMACAddress())) {
				return true;
			}
			else {
				return false;
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static byte[] generateBootFile(String MACAddress) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(new File(PUBKEYFILE)));
			byte[] e = cipher.doFinal(MACAddress.getBytes());
			return e;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static PublicKey getPublicKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		byte[] bytePublicKey = new byte[bis.available()];
		bis.read(bytePublicKey);
		bis.close();
		
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		
		return publicKey;
	}
	
	private static PrivateKey getPrivateKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		byte[] bytePrivateKey = new byte[bis.available()];
		bis.read(bytePrivateKey);
		bis.close();
		
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		
		return privateKey;
	}
	
	public static String getMACAddress() throws IOException {
		// TODO
		Process process = Runtime.getRuntime().exec("ipconfig /all");
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		String MACAddr = null;
		while ((line = input.readLine()) != null) {
			if (line.indexOf("Physical Address") > 0 || line.indexOf("ÎïÀíµØÖ·") > 0) {
				MACAddr = line.substring(line.indexOf("-") - 2);
				return MACAddr;
			}
		}
		
		return null;
	}
}
