package bit.scallionchickenrice.raph.datafactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;
import bit.scallionchickenrice.raph.entity.HistoryData;

public class HistoryDataFactory {
	public static List<HistoryData> getHistoryData() {
		List<HistoryData> list = new ArrayList<HistoryData>();
		
		ControlDB dsm = ControlDB.getInstance();
		RaphResultSet rs = null;
		
		String sql = new String();
		sql = "SELECT * FROM orderInfo";
		
		try {
			rs = dsm.executeQuery(sql);
			
			while( rs.next() ) {
				HistoryData historyData = new HistoryData();
				
				historyData.setOrderId(rs.getString("orderId"));
				historyData.setTableNumber(rs.getString("tableNo"));
				if(rs.getString("discountType") != null) {
					historyData.setHistoryDiscount(new String(rs.getString("discountType").getBytes("ISO-8859-1"), "GBK"));
				}
				else {
					historyData.setHistoryDiscount("");
				}
				historyData.setIfPaid(rs.getBoolean("ifPaid"));
				historyData.setHistoryPrice(rs.getString("orderPrice"));
				historyData.setHistoryTime(rs.getString("orderTime"));
				
				list.add(historyData);
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
