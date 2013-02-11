package bit.scallionchickenrice.raph.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;

public class UserManage {
	public static boolean insertUser(String username, String password, String userType) {
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "INSERT INTO userInfo(userName,password,userType) VALUES('" + username + "','" + password + "','" + new String(userType.getBytes("GBK"), "ISO-8859-1") + "')";
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}
	
	public static boolean changeProfile(String username, String password, String userType) {
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "UPDATE userInfo SET password = '" + password + "', userType = '" + new String(userType.getBytes("GBK"), "ISO-8859-1") + "' WHERE userName='" + username + "'";
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}
	
	public static boolean deleteUser(String username) {
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "DELETE FROM userInfo WHERE userName = '" + username + "'";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static ArrayList<String> getOnlineCook() {
		// TODO Auto-generated method stub
		ArrayList<String> ipList = new ArrayList<String>();
		
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		RaphResultSet rs = null;
		
		try {
			sql = "SELECT * FROM userInfo WHERE userType = '" + new String("³øÊ¦".getBytes("GBK"), "ISO-8859-1") + "' AND ifOnline = 1";
			
			rs = dsm.executeQuery(sql);
			
			while(rs.next()) {
				ipList.add(rs.getString("ipAddress"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ipList;
	}
}
