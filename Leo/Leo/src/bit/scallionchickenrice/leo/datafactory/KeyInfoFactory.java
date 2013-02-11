package bit.scallionchickenrice.leo.datafactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bit.scallionchickenrice.leo.DAO.ControlDB;
import bit.scallionchickenrice.leo.DAO.LeoResultSet;
import bit.scallionchickenrice.leo.entity.KeyInfo;

public class KeyInfoFactory {
	public static List<KeyInfo> getKeyInfo() {
		List<KeyInfo> list = new ArrayList<KeyInfo>();
		
		ControlDB dsm = ControlDB.getInstance();
		LeoResultSet rs = null;
		
		String sql = new String();
		sql = "SELECT * FROM keyInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while( rs.next() ) {
				KeyInfo keyInfo = new KeyInfo();
				
				keyInfo.setActiveKey(rs.getString("activeKey"));
				
				list.add(keyInfo);
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
