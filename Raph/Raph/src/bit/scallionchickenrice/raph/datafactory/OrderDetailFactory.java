package bit.scallionchickenrice.raph.datafactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.DAO.RaphResultSet;
import bit.scallionchickenrice.raph.entity.OrderDetail;

public class OrderDetailFactory {
	public static List<OrderDetail> getOrderDetail(String orderId) {
		List<OrderDetail> list = new ArrayList<OrderDetail>();
		
		ControlDB dsm = ControlDB.getInstance();
		RaphResultSet rs = null;
		
		String sql = new String();
		sql = "SELECT * FROM orderDetail WHERE orderId = " + orderId;
		
		try {
			rs = dsm.executeQuery(sql);
			
			while( rs.next() ) {
				OrderDetail orderDetail = new OrderDetail();
				
				orderDetail.setDishName(new String(rs.getString("dishName").getBytes("ISO-8859-1"), "GBK"));
				orderDetail.setNumber(rs.getString("number"));
				
				list.add(orderDetail);
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
