package bit.scallionchickenrice.raph.datafactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;
import bit.scallionchickenrice.raph.entity.MenuManagement;

public class MenuManagementFactory {
	public static List<MenuManagement> getMenuManagement() {
		List<MenuManagement> list = new ArrayList<MenuManagement>();
		
		ControlDB dsm = ControlDB.getInstance();
		RaphResultSet rs = null;
		
		String sql = new String();
		sql = "SELECT * FROM dishInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while( rs.next() ) {
				MenuManagement menuManagement = new MenuManagement();
				menuManagement.setDishName(new String(rs.getString("dishName").getBytes("ISO-8859-1"), "GBK"));
				menuManagement.setDishClass(new String(rs.getString("dishClass").getBytes("ISO-8859-1"), "GBK"));
				menuManagement.setDishResource(new String(rs.getString("dishResource").getBytes("ISO-8859-1"), "GBK"));
				menuManagement.setDishPrice(Integer.toString((rs.getInt("dishPrice"))));
				list.add(menuManagement);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
}
