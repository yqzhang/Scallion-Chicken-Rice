package bit.scallionchickenrice.raph.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CloseDB {
	/**
	 * The closeObject of the CloseDB
	 * Close select JDBC
	 * @param ResultSet rs, Statement stm, Connection con
	 */
	public static void closeObject(ResultSet rs, Statement stm, Connection con) 
	{
		closeObject(rs);
		closeObject(stm, con);
	}

	/**
	 * The closeObject of the CloseDB
	 * Close other JDBC
	 * @param Statement stm, Connection con
	 */
	public static void closeObject(Statement stm, Connection con) 
	{
		closeObject(stm);
		closeObject(con);
	}

	/**
	 * The closeObject of the CloseDB
	 * close Connection
	 * @param Connection con
	 */
	public static void closeObject(Connection con) 
	{
		try 
		{
			if (con != null) 
			{
				con.close();
			}
		}
		catch (Exception e) 
		{
			System.out.println("关闭Connection异常");
		}
	}

	/**
	 * The closeObject of the CloseDB
	 * close ResultSet
	 * @param ResultSet rs
	 */
	public static void closeObject(ResultSet rs) 
	{
		try 
		{
			if (rs != null) 
			{
				rs.close();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("关闭ResultSet异常");
		}
	}

	/**
	 * The closeObject of the CloseDB
	 * close Statement
	 * @param Statement st
	 */
	public static void closeObject(Statement st) 
	{
		try 
		{
			if (st != null) 
			{
				st.close();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("关闭Statement异常");
		}
	}
}
