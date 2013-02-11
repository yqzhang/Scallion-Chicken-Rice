package com.Mickey;

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

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


public class AuthorizeTool {
	private final static String BOOTFILE = "mnt/sdcard/CJFConfiguration/boot.ini";
	private final static String PUBKEYFILE = "public.dat";
	private final static String PRIKEYFILE = "mnt/sdcard/CJFConfiguration/private.dat";
	
	public static boolean validate(Activity activity) throws IOException {
		File file = new File(BOOTFILE);
		if(!file.exists()) {
			return false;
		}
		//Log.v("file exists", "yes");
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		//Log.v("boot.ini", Integer.toString(bis.available()));
		
		byte[] author = new byte[bis.available()];
		bis.read(author);
		bis.close();
		
		
		//Log.v("boot.ini", new String(author));

		try {
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(new File(PRIKEYFILE)));
				byte[] de = cipher.doFinal(author);
				
				String mac = new String(de);
				
				mac = mac.substring(mac.length() - 17, mac.length());
				
				if(mac.equals(getMACAddress(activity))) {
					return true;
				}
				else {
					Log.v("MAC not equal", mac);
					return false;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InvalidKeyException e) {
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
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance("RSA");
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(new File(PUBKEYFILE)));
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeySpecException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			byte[] e = cipher.doFinal(MACAddress.getBytes());
			return e;
		} catch (InvalidKeyException e) {
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
		
		//Log.v("privateKey", Integer.toString(bis.available()));
		
		byte[] bytePrivateKey = new byte[bis.available()];
		bis.read(bytePrivateKey);
		bis.close();
		
		//Log.v("privateKey", new String(bytePrivateKey));
		
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		
		return privateKey;
	}
	
	 public static String getMACAddress(Activity activity) {     
	        WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);     
	        WifiInfo info = wifi.getConnectionInfo(); 
	        return info.getMacAddress();
	    }
   
}
