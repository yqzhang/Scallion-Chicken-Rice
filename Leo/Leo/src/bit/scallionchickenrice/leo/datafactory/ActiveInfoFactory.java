package bit.scallionchickenrice.leo.datafactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bit.scallionchickenrice.leo.DAO.ControlDB;
import bit.scallionchickenrice.leo.DAO.LeoResultSet;
import bit.scallionchickenrice.leo.entity.ActiveInfo;

public class ActiveInfoFactory {
	public static List<ActiveInfo> getActiveInfo() {
		List<ActiveInfo> list = new ArrayList<ActiveInfo>();
		
		ControlDB dsm = ControlDB.getInstance();
		LeoResultSet rs = null;
		
		String sql = new String();
		sql = "SELECT * FROM activeInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while( rs.next() ) {
				ActiveInfo activeInfo = new ActiveInfo();
				
				activeInfo.setActiveKey(rs.getString("activeKey"));
				activeInfo.setActiveTime(rs.getString("activeTime"));
				activeInfo.setActiveType(new String(rs.getString("activeType").getBytes("ISO-8859-1"), "GBK"));
				activeInfo.setMACAddress(rs.getString("MACAddress"));
				
				list.add(activeInfo);
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
