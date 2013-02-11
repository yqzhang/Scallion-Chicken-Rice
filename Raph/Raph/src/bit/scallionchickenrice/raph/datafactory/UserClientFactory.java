package bit.scallionchickenrice.raph.datafactory;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;
import bit.scallionchickenrice.raph.entity.UserClient;

public class UserClientFactory {
	public static List<UserClient> getUserClient() {
		List<UserClient> list = new ArrayList<UserClient>();
		
		ControlDB dsm = ControlDB.getInstance();
		RaphResultSet rs = null;
		
		String sql = new String();
		sql = "SELECT * FROM userInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while( rs.next() ) {
				UserClient userClient = new UserClient();
				userClient.setUsername(rs.getString("userName"));
				userClient.setPassword(rs.getString("password"));
				userClient.setUserType(new String(rs.getString("userType").getBytes("ISO-8859-1"), "GBK"));
				userClient.setCondition(rs.getBoolean("ifOnline") ? "Online" : "Offline");
				userClient.setIpAddress(rs.getString("ipAddress"));
				list.add(userClient);
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
