package bit.scallionchickenrice.raph.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;

public class DishManage {
	public static ArrayList<String> getDishClasses() {
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		RaphResultSet rs = null;
		
		try {
			ArrayList<String> dishList = new ArrayList<String>();
			
			sql = "SELECT * FROM classInfo";
			
			rs = dsm.executeQuery(sql);
			
			while(rs.next()) {
				dishList.add(new String(rs.getString("className").getBytes("ISO-8859-1"), "GBK"));
			}
			
			return dishList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}

	public static boolean addDish(String dishName, String dishClass, String dishPrice,
			String dishDir, String dishRes) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "INSERT INTO dishInfo(dishName, dishClass, dishResource, dishPrice, dishPhoto) VALUES('" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "','" + new String(dishClass.getBytes("GBK"), "ISO-8859-1") + "','" + new String(dishRes.getBytes("GBK"), "ISO-8859-1") + "'," + dishPrice + ",'" + dishDir + "')";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static String getDishPictureDir(String dishName) {
		// TODO Auto-generated method stub
		
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		RaphResultSet rs = null;
		
		try {
			sql = "SELECT dishPhoto FROM dishInfo WHERE dishName = '" +  new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				return new String(rs.getString("dishPhoto").getBytes("ISO-8859-1"), "GBK");
			}
			else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static boolean editDish(String dishName, String dishClass, String dishPrice,
			String dishDir, String dishRes) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "UPDATE dishInfo SET dishClass = '" + new String(dishClass.getBytes("GBK"), "ISO-8859-1") + "', dishResource = '" + new String(dishRes.getBytes("GBK"), "ISO-8859-1") + "', dishPrice = " + dishPrice + ", dishPhoto = '" + dishDir + "' WHERE dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static boolean deleteDish(String dishName) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "DELETE FROM dishInfo WHERE dishName = '" + new String(dishName.getBytes("GBK"), "ISO-8859-1") + "'";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}
	
	public static ArrayList<String> getAllClass() {
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		RaphResultSet rs = null;
		
		ArrayList<String> classList = new ArrayList<String>();
		
		sql = "SELECT * FROM classInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while(rs.next()) {
				classList.add(new String(rs.getString("className").getBytes("ISO-8859-1"), "GBK"));
			}
			
			return classList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}

	public static boolean editClass(String className, String text) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "UPDATE classInfo SET className = '" + new String(text.getBytes("GBK"),"ISO-8859-1") + "' WHERE className = '" + new String(className.getBytes("GBK"),"ISO-8859-1") + "'";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static boolean deleteClass(String className) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "DELETE FROM classInfo WHERE className = '" + new String(className.getBytes("GBK"),"ISO-8859-1") + "'";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static boolean addClass(String className) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "INSERT INTO classInfo(className) VALUES('" + new String(className.getBytes("GBK"),"ISO-8859-1") + "')";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static ArrayList<String> getAllDiscount() {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		RaphResultSet rs = null;
		
		ArrayList<String> classList = new ArrayList<String>();
		
		sql = "SELECT * FROM discountInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while(rs.next()) {
				classList.add(new String(rs.getString("discountName").getBytes("ISO-8859-1"), "GBK") + " " + new String(rs.getString("discountPercentage").getBytes("ISO-8859-1"), "GBK"));
			}
			
			return classList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}

	public static boolean addDiscount(String discountName, String discountPercentage) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "INSERT INTO discountInfo(discountName, discountPercentage) VALUES('" + new String(discountName.getBytes("GBK"),"ISO-8859-1") + "', " + discountPercentage +")";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static boolean editDiscount(String discountName, String name,
			String percentage) {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "UPDATE discountInfo SET discountName = '" + new String(name.getBytes("GBK"),"ISO-8859-1") + "', discountPercentage = " + percentage +" WHERE discountName = '" + new String(discountName.getBytes("GBK"),"ISO-8859-1") + "'";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	public static boolean deleteDiscount(String item) {
		// TODO Auto-generated method stub
ControlDB dsm = ControlDB.getInstance();
		
		String sql = new String();
		
		try {
			sql = "DELETE FROM discountInfo WHERE discountName = '" + new String(item.getBytes("GBK"),"ISO-8859-1") + "'";
			
			//System.out.println(sql);
			
			dsm.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}
}
