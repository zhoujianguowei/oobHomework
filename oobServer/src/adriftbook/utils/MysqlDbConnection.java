package adriftbook.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
public class MysqlDbConnection
{

	private static String url = "jdbc:mysql://localhost:3306/adriftbook";
	private static String user = "zhoujianguo";
	private static String password = "hello";
	private static Connection conn;
	private static Object obj = new Object();

	public static Connection getConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		return conn;
	}

	public static synchronized void execute(String sqlString)
	{
		Connection conn = getConnection();
		PreparedStatement preparedStatement = null;
		try
		{
			preparedStatement = conn.prepareStatement(sqlString);
			preparedStatement.execute(sqlString);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection();

	}

	public static ResultSet getResultSet(String sqlString)
	{

		ResultSet rSet = null;
		try
		{
			synchronized (MysqlDbConnection.class)
			{
				Connection conn = getConnection();
				rSet = conn.prepareStatement(sqlString).executeQuery();
			}

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rSet;
	}

	public static void closeConnection()
	{
		if (conn != null)
			try
			{
				conn.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}

	/**
	 * 连接数据库netbookstore,同时预先插入图书
	 * 
	 * @param args
	 */
	public static void main(String[] args)throws  Exception
	{
//		Calendar calendar=Calendar.getInstance();
//		String encode="周建国"+calendar.getTimeInMillis()+".png";
//		System.out.println(CommonUtils.transformFileToByte(encode));
		File currentFile=new File(".");
		System.out.println(currentFile.getAbsolutePath());
	}
}
