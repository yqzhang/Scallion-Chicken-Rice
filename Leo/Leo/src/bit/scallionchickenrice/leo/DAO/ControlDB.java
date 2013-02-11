package bit.scallionchickenrice.leo.DAO;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ControlDB {
	
	private static ControlDB control = null;
	
	/**
	 * The Constructor of the ControlDB
	 */
	public ControlDB()
	{
		
	}
	
	/**
	 * The getInstance of the ControlDB
	 * @return ControlDB
	 */
	public static ControlDB getInstance() 
	{
		if (control == null) 
		{
			control = new ControlDB();
		}
		
		return control;
	}
	
	/**
	 * The executeQuery of the ControlDB
	 * @return ResultSet
	 * @throws SQLException
	 */
	public LeoResultSet executeQuery(String sql) throws Exception 
	{
		//define ResultSet Connection and Statement
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		LeoResultSet rrs = null;
		
		try 
		{
			con = DBConnectionFactory.getConnection();
			
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			
			rs = stmt.executeQuery(sql);
			
			rrs = new LeoResultSet();
			
			while(rs.next()) {
				Map<String, String> item = new HashMap<String,String>();
				
				int count = rs.getMetaData().getColumnCount();
				for(int i = 1; i <= count; i++) {
					item.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
				
				rrs.add(item);
			}
		} 
		catch (SQLException e) 
		{
			throw e;
		}
		finally 
		{
			CloseDB.closeObject(stmt, con);
		}
		
		return rrs;
	}
	
	/**
	 * The executeUpdate of the ControlDB
	 * @return Boolean
	 * if success return true
	 * if fail return false
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql) throws Exception 
	{
		boolean flag = false;
		Connection con = null;
		Statement stmt = null;
		
		try 
		{
			con = DBConnectionFactory.getConnection();
			stmt = con.createStatement();
			
			int row = stmt.executeUpdate(sql);
		
			//row>0 return true row<0 return false
			flag = row > 0 ? true : false;
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
			flag = false;
		} 
		
		finally 
		{
			CloseDB.closeObject(stmt, con);
		}
		
		return flag;
	}
}
