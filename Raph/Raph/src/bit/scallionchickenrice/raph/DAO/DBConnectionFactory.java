package bit.scallionchickenrice.raph.DAO;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBConnectionFactory {
	/** 
	 * Database driver name
	 */
	private String driver = "";

	/** 
	 * connect database url
	 */
	private String dbURL = "";

	/** 
	 * database name
	 */
	private String user = "";

	/** 
	 * database password 
	 */
	private String password = "";

	/** 
	 * Instantiated factory
	 */
	private static DBConnectionFactory factory = null;

	/**
	 * The Constructor of the DBConnectionFactory
	 * @throws IOException
	 */
	private DBConnectionFactory() throws Exception 
	{
		//new a properties
		Properties prop = new Properties();

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("jdbc.properties");
		try 
		{
			prop.load(is);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("加载配置文件出错");
		}
		
		//get driver
		this.driver = (String) prop.get("driver");
		
		//get url
		this.dbURL = (String) prop.get("url");
		
		//get user
		this.user = (String) prop.get("user");
		
		//get password
		this.password = (String) prop.get("password");
	}

	/**
	 * The getDbURL method of the DBConnectionFactory
	 * @return String
	 */
	public String getDbURL() 
	{
		return dbURL;
	}

	/**
	 * The getDriver method of the DBConnectionFactory
	 * @return String
	 */
	public String getDriver() 
	{
		return driver;
	}

	/**
	 * The getPassword method of the DBConnectionFactory
	 * @return String
	 */
	public String getPassword() 
	{
		return password;
	}

	/**
	 * The getUser method of the DBConnectionFactory
	 * @return String
	 */
	public String getUser() 
	{
		return user;
	}

	/**
	 * The getConnection method of the DBConnectionFactory
	 * @return Connection
	 * if success return Connection
	 * if fail return null
	 * @throws SQLException
	 */
	
	public static Connection getConnection() 
	{
		Connection conn = null;

		if (factory == null) 
		{
			try 
			{
				factory = new DBConnectionFactory();
			}
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
				
				return null;
			}
		}

		//create connection
		try 
		{
			Class.forName(factory.getDriver());
			
			conn = DriverManager.getConnection(factory.getDbURL(), factory
					.getUser(), factory.getPassword());
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println(" No class " + factory.getDriver()
					+ " found error");
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			System.out.println("Failed to get connection :" + e.getMessage());
			e.printStackTrace();
		}

		return conn;
	}
}
