package com.Mickey;

import java.io.*;
import java.net.*;
import android.util.Log;

public class MyServer {
	boolean flag = true;
	DataInputStream dis;
	DataOutputStream dos;
	FileOutputStream fos;

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else
				temp = new File(path + File.separator + tempList[i]);
			if (temp.isFile()) {
				if (!temp.delete())
					Log.v("message", "delete Fail");
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// É¾³ýÎÄ¼þ
				flag = true;
			}
		}
		return flag;
	}

	public void ServerStart(InfoStore infoStore) {

		Socket socket = null;
		// TCPServer.SERVERIP
		try {
			socket = new Socket(infoStore.getIPAddress(), 7797);
			DataOutputStream out = new DataOutputStream(socket
					.getOutputStream());
			out.writeUTF("0 HAHA");

			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			String answer = "g";
			byte ans[] = answer.getBytes();
			byte b[] = new byte[1000];
			int ti;
			new File("mnt/sdcard/Dishes").mkdirs();
			while (flag) {
				ti = dis.read(b);
				dos.write(ans);
				String select = new String(b, 0, ti);

				Log.v("receive", select);

				if (select.contains("/]0f")) {
					File f = new File("mnt/sdcard/Dishes/"
							+ (select.replace("/]0f", "")));
					// System.out.println("creat directory");
					f.mkdirs();
				} else if (select.contains("/]0c")) {
					fos = new FileOutputStream("mnt/sdcard/Dishes/"
							+ (select.replace("/]0c", "")));

					Log.v("Path", "mnt/sdcard/Dishes/"
							+ (select.replace("/]0c", "")));

					//String cs;
					//boolean cflag = true;
					int tip = dis.readInt();
					dos.write(ans);
					while (tip > 0) {
						ti = dis.read(b, 0, (tip > 1000 ? 1000 : tip));
						tip = tip - ti;
						//cs = new String(b, 0, 4);
						fos.write(b, 0, ti);
					}
					fos.flush();
					fos.close();
					dos.write(ans);
				} else if (select.contains("/]00")) {
					flag = false;
				}
			}
			dis.close();
		} catch (IOException e) {
			// System.out.println("Error");
		}

	}
}