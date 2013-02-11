package bit.scallionchickenrice.raph.communication;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;
import bit.scallionchickenrice.raph.action.UserManage;

public class ProcessMessage {
	private Socket socket = null;

	private FileInputStream fis;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private static final int COOKPORT = 9998;
	private static final int ORDERPORT = 9998;

	public ProcessMessage(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void query(String str, String ipAddress) throws Exception {
		String[] temp = str.split(" ");
		System.out.println("Query: " + temp[0]);
		int type = Integer.parseInt(temp[0]);
		
		System.out.println("Context:" + str);

		switch (type) {
		// Update Menu
		case (0): {
			System.out.println("Update Menu");
		
			generateDishInfo();
			generateClassInfo();
			generateDiscountInfo();

			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());

			transmit(new File("pic/"));
			
			break;
		}
		//Submit Order
		case (1): {
			System.out.println("Submit Order");
			
			int tableNum = Integer.parseInt(temp[1]);
			
			int num = Integer.parseInt(temp[2]);
			
			int orderId = generateOrder(tableNum, ipAddress);
			
			for(int i = 0; i < num; i++) {
				generateOrderDetail(orderId, temp[i * 2 + 3], temp[i * 2 + 4]);
			}
			
			String postMessage = new String();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String dateTime = df.format(calendar.getTime());
			
			postMessage = "0 " + orderId + " " + tableNum + " " + num + " " + dateTime;
			
			for(int i = 0; i< num ;i++) {
				postMessage += " " + temp[i * 2 + 3] + " " + temp[i * 2 + 4];
			}
			
			System.out.println("Post Message to Cook:" + postMessage);
			
			postOrder(UserManage.getOnlineCook(), postMessage);
			
			PrintWriter pout = null;  
	        try {  
	            pout = new PrintWriter(new BufferedWriter(  
	                   new OutputStreamWriter(socket.getOutputStream())),  
	                   true);  
	            pout.println(orderId);
	            pout.flush();
	            pout.close();
	        } catch (IOException e) {  
	            e.printStackTrace();
	        }
	        
	        break;
		}
	    //Add into Order
		case (2): {
			System.out.println("Add into Order");
			
			temp[1] = temp[1].replace("\r", "");
		
			int orderId = Integer.parseInt(temp[1]);
			
			int num = Integer.parseInt(temp[2]);
			
			for(int i = 0; i < num; i++) {
				addIntoOrder(orderId, temp[i * 2 + 3], temp[i * 2 + 4]);
			}
			
			String postMessage = new String();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String dateTime = df.format(calendar.getTime());
			
			postMessage = "0 " + orderId + " " + getTableByOrderId(orderId) + " " + num + " " + dateTime;
			
			for(int i = 0; i< num ;i++) {
				postMessage += " " + temp[i * 2 + 3] + " " + temp[i * 2 + 4];
			}
			
			postOrder(UserManage.getOnlineCook(), postMessage);
			
			break;
		}
		//Pay the Bill
		case (3): {
			System.out.println("Pay the Bill");
			
			String orderId = temp[1];
			
			String discountType = temp[2];
			
			String price = temp[3];
			
			payTheBill(orderId, discountType, price);
			
			break;
		}
		//Change Condition
		case(4): {
			System.out.println("Change Condition");
			
			String orderId = temp[1];
			String dishName = temp[2];
			
			String postMessage = "0 " + dishName + " " + temp[3] + " " + temp[4];
			
			postChange(getIpAddressByOrderId(orderId), postMessage);
			
			break;
		}
		//Delete Dish From Order
		case(5): {
			System.out.println("Delete Dish From Order");
			
			String orderId = temp[1];
			String dishName = temp[2];
			
			deleteDishFromOrder(orderId, dishName);
			
			String postMessage = "1 " + orderId + " " + dishName;
			
			postOrder(UserManage.getOnlineCook(), postMessage);
			
			break;
		}
		//User Login
		case(6): {
			System.out.println("User Login");
			
			String username = temp[1];
			String password = temp[2];
			
			if(validateUserLogin(username, password, ipAddress)) {
				PrintWriter pout = null;  
		        try {  
		            pout = new PrintWriter(new BufferedWriter(  
		                   new OutputStreamWriter(socket.getOutputStream())),  
		                   true);  
		            pout.println("true");
		            pout.flush();
		            pout.close();
		        } catch (IOException e) {  
		            e.printStackTrace();
		        }
			}
			else {
				PrintWriter pout = null;  
		        try {
		            pout = new PrintWriter(new BufferedWriter(  
		                   new OutputStreamWriter(socket.getOutputStream())),  
		                   true);  
		            pout.println("false");
		            pout.flush();
		            pout.close();
		        } catch (IOException e) {  
		            e.printStackTrace();
		        }
			}
			
			break;
		}
		//Log Off
		case(7): {
			System.out.println("Log Off");
			
			String username = temp[1];
			
			userLogOff(username);
			
			break;
		}
		}
	}
	
	private void userLogOff(String username) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = "UPDATE userInfo SET ifOnline = 0, ipAddress = NULL WHERE userName = '" + username + "'";
		
		try {
			dsm.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean validateUserLogin(String username, String password, String ipAddress) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = "SELECT * FROM userInfo WHERE userName = '" + username + "' AND password = '" + password + "'";
		
		RaphResultSet rs = null;
		
		try {
			rs = dsm.executeQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(rs.next()) {
			sql = "UPDATE userInfo SET ifOnline = 1, ipAddress = '" + ipAddress + "' WHERE userName = '" + username + "' AND password = '" + password + "'";
			
			try {
				dsm.executeUpdate(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
		else {
			return false;
		}
	}

	private void deleteDishFromOrder(String orderId, String dishName) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		RaphResultSet rs = null;
		
		String sql = null;
		
		try {
			sql = "SELECT number FROM orderDetail WHERE orderId = " + orderId + " AND dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
			
			rs = dsm.executeQuery(sql);
			
			int num = 0;
			
			if(rs.next()) {
				num = rs.getInt(1);
			}
			
			if(num == 1) {
				sql = "DELETE FROM orderDetail WHERE orderId = " + orderId + " AND dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
				
				dsm.executeUpdate(sql);
			}
			else {
				sql = "UPDATE orderDetail SET number = " + (num - 1) + " WHERE orderId = " + orderId + " AND dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
				
				dsm.executeUpdate(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void postChange(String ipAddressByOrderId, String postMessage) {
		// TODO Auto-generated method stub
		Socket mySocket;
		try {
			mySocket = new Socket(ipAddressByOrderId, ORDERPORT);
			DataOutputStream out = new DataOutputStream(mySocket.getOutputStream());
			out.writeUTF(postMessage);
			mySocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getIpAddressByOrderId(String orderId) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = "SELECT ipAddress FROM orderInfo WHERE orderId = " + orderId;
		
		RaphResultSet rs = null;
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private String getTableByOrderId(int orderId) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = null;
		
		sql = "SELECT tableNo FROM orderInfo WHERE orderId = " + orderId;
		
		RaphResultSet rs = null;
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private void payTheBill(String orderId, String discountType, String price) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = null;
		
		try {
			sql = "UPDATE orderInfo SET ifPaid = 1, discountType = '" + new String(discountType.getBytes("GBK"), "ISO-8859-1") + "', orderPrice = " + price + " WHERE orderId = " + orderId;
			
			dsm.executeUpdate(sql);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addIntoOrder(int orderId, String dishName, String num) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = null;
		RaphResultSet rs = null;
		
		try {
			sql = "SELECT number FROM orderDetail WHERE orderId = " + orderId + " AND dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
			
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				int no = rs.getInt(1);
				
				sql = "UPDATE orderDetail SET number = " + (no + 1) + " WHERE orderId = " + orderId + " AND dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
				
				dsm.executeUpdate(sql);
			}
			else {
				sql = "INSERT INTO orderDetail(orderId, dishName, number) VALUES(" + orderId + ",'" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'," + num +")";
				
				dsm.executeUpdate(sql);
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	private void postOrder(ArrayList<String> ipList, String postMessage) {
		for(String str : ipList) {
			System.out.println("IP Address:" + str);
			
			Socket mySocket;
			try {
				mySocket = new Socket(str, COOKPORT);
				DataOutputStream out = new DataOutputStream(mySocket.getOutputStream());
				out.writeUTF(postMessage);
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void generateOrderDetail(int orderId, String dishName, String num) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = null;
		
		try {
			sql = "INSERT INTO orderDetail(orderId, dishName, number) VALUES(" + orderId + ",'" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'," + num +")";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			dsm.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int generateOrder(int tableNum, String ipAddress) {
		ControlDB dsm = ControlDB.getInstance();
		
		RaphResultSet rs  = null;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String dateTime = df.format(calendar.getTime());
		
		System.out.println(dateTime);
		
		String sql = "INSERT INTO orderInfo(tableNo, orderTime, ipAddress) VALUES(" + tableNum + ",'" + dateTime + "','" + ipAddress + "')";
		
		try {
			dsm.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql = "SELECT MAX(orderId) FROM orderInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private void generateDishInfo() {
		File file = new File("pic/dish.dat");
		
		if(file.exists()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ControlDB dsm = ControlDB.getInstance();
		
		RaphResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM dishInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				PrintWriter out = new PrintWriter(
						file.getAbsoluteFile());
				out.println(rs.getString(1));
				int num = Integer.parseInt(rs.getString(1));
				
				sql = "SELECT * FROM dishInfo";
				
				rs = dsm.executeQuery(sql);
				
				for(int i = 0; i < num; i++) {
					rs.next();
					out.println(new String(rs.getString("dishClass").getBytes("ISO-8859-1"), "GBK"));
					out.println(new String(rs.getString("dishName").getBytes("ISO-8859-1"), "GBK"));
					out.println(new String(rs.getString("dishResource").getBytes("ISO-8859-1"), "GBK"));
					out.println(rs.getString("dishPrice"));
					out.println(rs.getString("dishPhoto").replace("pic/", ""));
				}
					
				out.close();
			}
			else {
				PrintWriter out = new PrintWriter(
						file.getAbsoluteFile());
				out.println(0);
				out.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generateClassInfo() {
		File file = new File("pic/class.dat");
		
		if(file.exists()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ControlDB dsm = ControlDB.getInstance();
		
		RaphResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM classInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				PrintWriter out = new PrintWriter(
						file.getAbsoluteFile());
				out.println(rs.getString(1));
				int num = Integer.parseInt(rs.getString(1));
				
				sql = "SELECT * FROM classInfo";
				
				rs = dsm.executeQuery(sql);
				
				for(int i = 0; i < num; i++) {
					rs.next();
					out.println(new String(rs.getString("className").getBytes("ISO-8859-1"), "GBK"));
				}
					
				out.close();
			}
			else {
				PrintWriter out = new PrintWriter(
						file.getAbsoluteFile());
				out.println(0);
				out.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generateDiscountInfo() {
		File file = new File("pic/discount.dat");
		
		if(file.exists()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ControlDB dsm = ControlDB.getInstance();
		
		RaphResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM discountInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				PrintWriter out = new PrintWriter(
						file.getAbsoluteFile());
				out.println(rs.getString(1));
				int num = Integer.parseInt(rs.getString(1));
				
				sql = "SELECT * FROM discountInfo";
				
				rs = dsm.executeQuery(sql);
				
				for(int i = 0; i < num; i++) {
					rs.next();
					out.println(new String(rs.getString("discountName").getBytes("ISO-8859-1"), "GBK"));
					out.println(rs.getString("discountPercentage"));
				}
					
				out.close();
			}
			else {
				PrintWriter out = new PrintWriter(
						file.getAbsoluteFile());
				out.println(0);
				out.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void transmit(File file) {
		// TODO Auto-generated method stub
		byte b[];
		String ts;
		int ti;
		for (File f1 : file.listFiles()) { // 首先通过if语句判断f1是文件还是文件夹
			
			System.out.println(f1.getPath());
			
			if (f1.isDirectory()) // fi是文件夹,则向服务器端传送一条信息
			{
				ts = "/]0f" + f1.getPath().replace("pic\\", "");// "/]0f"用于表示这条信息的内容是文件夹名称
				b = ts.getBytes();
				try {
					dos.write(b);
					dos.flush();
					dis.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				transmit(f1);// 由于f1是文件夹(即目录),所以它里面很有可能还有文件或者文件夹,所以进行递归
			} else {
				try {
					fis = new FileInputStream(f1);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ts = "/]0c" + f1.getPath().replace("pic\\", "");// 同上,表示这是一个文件的名称
				
				System.out.println(ts);
				
				b = ts.getBytes();
				try {
					dos.write(b);
					dos.flush();
					dis.read();
					dos.writeInt(fis.available());// 传输一个整型值,指明将要传输的文件的大小
					dos.flush();
					dis.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				b = new byte[1000];
				try {
					while (fis.available() > 0)// 开始传送文件
					{
						ti = fis.read(b);
						dos.write(b, 0, ti);
						dos.flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					dos.flush();
					fis.close();
					dis.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		ts = "/]00";// 同上,表示这是一个文件的名称
		
		System.out.println(ts);
		
		b = ts.getBytes();
		try {
			dos.write(b);
			dos.flush();
			dis.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			dis.close();
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
