package com.rs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
 
 

import com.rs.base.ConnectionFactory;
import com.rs.tools.myTools;



public class SystemDao {
    
	 /**
	  * 获得用户 ID
	  * @param serial
	  * @return
	  * @throws SQLException
	  */
	 public static String getUserId(String serial) throws SQLException{
			//构造 SQL 语句
	    	String rsString=null;
			String sql="SELECT userId  FROM getway where serialNumber=?";
					 
					
			//从连接池获得一个可用的连接
			Connection connection=ConnectionFactory.getConnection();
			PreparedStatement pst;//预编译语句
			ResultSet rs;//返回的结果集
				pst=connection.prepareStatement(sql);
				//pst.setInt(1, deviceType);//设置参数
				pst.setString(1, serial);//设置参数
				rs=pst.executeQuery();//执行查询语句SQL
				if(rs.next())
				{
					rsString=rs.getString("userId");
				}
				connection.close();
			return rsString;
	    }
/**
 * 检查是否有日期记录
 * @param days
 * @return
 * @throws SQLException
 */
	 public static boolean checkDaysExist(String days,String userId) throws SQLException{
			//构造 SQL 语句
	    	boolean r=false;
			String sql="SELECT 1  FROM interaction_days where days=?  and userId=? limit 1";
			//从连接池获得一个可用的连接
			Connection connection=ConnectionFactory.getConnection();
			PreparedStatement pst;//预编译语句
			ResultSet rs;//返回的结果集
				pst=connection.prepareStatement(sql);
				pst.setString(1, days);//设置参数
				pst.setString(2, userId);//设置参数
				rs=pst.executeQuery();//执行查询语句SQL
				if(rs.next())
				{
					r=true;
				}
				connection.close();
			return r;
	    }
	/**
	 * 插入一条日期记录
	 * @param days
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	 public static int AddInteracionDays(String days,String userId) throws SQLException{
			//构造 SQL 语句
	    	int r=0;
			String sql="insert into interaction_days  (userId,days)  values(?,?)";
			//从连接池获得一个可用的连接
			Connection connection=ConnectionFactory.getConnection();
			PreparedStatement pst;//预编译语句
			ResultSet rs;//返回的结果集
				pst=connection.prepareStatement(sql);
				pst.setString(1, userId);//设置参数
				pst.setString(2, days);//设置参数
				r=pst.executeUpdate();//执行查询语句SQL
				connection.close();
			return r;
	    }
	 /**
	  * 获得监护站 ID
	  * @param userId
	  * @return
	  * @throws SQLException
	  */
	 public static String getStationId(String userId) throws SQLException{
			//构造 SQL 语句
		 
	    	String rsString=null;
			String sql="SELECT stationId from `user` where id=?";
					 
					
			//从连接池获得一个可用的连接
			Connection connection=ConnectionFactory.getConnection();
			PreparedStatement pst;//预编译语句
			ResultSet rs;//返回的结果集
				pst=connection.prepareStatement(sql);
				//pst.setInt(1, deviceType);//设置参数
				pst.setString(1, userId);//设置参数
				rs=pst.executeQuery();//执行查询语句SQL
				if(rs.next())
				{
					rsString=rs.getString("stationId");
				}
				connection.close();
			return rsString;
	    }
	 
    /**
     * 获得设备Id
     * @param serial  序列号
     * @param deviceType  设备类型
     * @return
     * @throws SQLException 
     */
    public static String getDeviceId(String serial,int deviceType) throws SQLException{
		//构造 SQL 语句
    	String rsString=null;
		String sql="SELECT d.id  FROM getway g "+
				"LEFT JOIN device d on d.getwayId=g.id and d.deviceType=? "+
				"where g.serialNumber=?";
		//从连接池获得一个可用的连接
		Connection connection=ConnectionFactory.getConnection();
		PreparedStatement pst;//预编译语句
		ResultSet rs;//返回的结果集
			pst=connection.prepareStatement(sql);
			pst.setInt(1, deviceType);//设置参数
			pst.setString(2, serial);//设置参数
			rs=pst.executeQuery();//执行查询语句SQL
			if(rs.next())
			{
				rsString=rs.getString("id");
			}
			connection.close();
		return rsString;
    }
    
    /**
     * 获得设备的类型Id
     * @param serial  序列号
     * @param deviceType  设备类型
     * @return
     * @throws SQLException 
     */
    public static String getGetwayType(String serial) throws SQLException{
		//构造 SQL 语句
    	String rsString=null;
		String sql="SELECT g.typeId  FROM getway g where g.serialNumber=?";
		//从连接池获得一个可用的连接
		Connection connection=ConnectionFactory.getConnection();
		PreparedStatement pst;//预编译语句
		ResultSet rs;//返回的结果集
			pst=connection.prepareStatement(sql);
			//pst.setInt(1, deviceType);//设置参数
			pst.setString(1, serial);//设置参数
			rs=pst.executeQuery();//执行查询语句SQL
			if(rs.next())
			{
				rsString=rs.getString("typeId");
			}
			connection.close();
		return rsString;
    }
    
    /**
     * 更新设备最近状态
     * @param value1
     * @param value2
     * @param value3
     * @param time
     * @param id
     * @return
     * @throws SQLException
     */
    public static int updateDeviceStatus(String  value1, String value2,String value3,String value4,String time,String deviceId) throws SQLException
    {
    		
    		//构造 SQL 语句
    		String sql="update device set value1=?,value2=?,value3=?,value4=?,updateTime=? where id=?";
    		//从连接池获得一个可用的连接
    		Connection connection=ConnectionFactory.getConnection();
    		PreparedStatement pst;//预编译语句
    		pst=connection.prepareStatement(sql);
			pst.setString(1, value1);//设置参数
			pst.setString(2, value2);//设置参数
			pst.setString(3, value3);//设置参数
			pst.setString(4, value4);//设置参数
			pst.setString(5, time);//设置参数
			pst.setString(6, deviceId);//设置参数
    		int rs=pst.executeUpdate();//执行查询语句SQL
    		connection.close(); 
    	return rs;
    }
    /**
     * 增加历史记录
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @param time
     * @param deviceId
     * @param tableName
     * @return
     * @throws SQLException 
     */
    public static int AddHis(String  value1, String value2,String value3,String value4 ,long time,String deviceId,String tableName) throws SQLException
    {
    		
    		//构造 SQL 语句
    		String sql="insert into "+tableName+" (deviceId,value1,value2,value3,value4,addTime) values(?,?,?,?,?,?)";
    		//从连接池获得一个可用的连接
    		Connection connection=ConnectionFactory.getConnection();
    		PreparedStatement pst;//预编译语句

    		int rs=0;
		//	try {
	    		pst=connection.prepareStatement(sql);
				pst.setString(1, deviceId);//设置参数
				pst.setString(2, value1);//设置参数
				pst.setString(3, value2);//设置参数
				pst.setString(4, value3);//设置参数
				pst.setString(5, value4);//设置参数
				pst.setLong(6, time);//设置参数
				rs = pst.executeUpdate();

				connection.close(); 
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				myTools.saveLog(e.toString()+"\r\n");
//			}//执行查询语句SQL
    		
    	return rs;
    }
    
    public static int AddOrder(String serial,String code ,String  value1, String value2,String value3,String value4 ,String time) throws SQLException
    {
    		
    		//构造 SQL 语句
    		String sql="insert into `order` (serial,code,value1,value2,value3,value4,addTime,send) values(?,?,?,?,?,?,?,?)";
    		//从连接池获得一个可用的连接
    		Connection connection=ConnectionFactory.getConnection();
    		PreparedStatement pst;//预编译语句

    		int rs=0;
		//	try {
	    		pst=connection.prepareStatement(sql);
				pst.setString(1, serial);//设置参数
				pst.setString(2, code);//设置参数
				pst.setString(3, value1);//设置参数
				pst.setString(4, value2);//设置参数
				pst.setString(5, value3);//设置参数
				pst.setString(6, value4);//设置参数
				pst.setString(7, time);//设置参数
				pst.setString(8, "0");
				rs = pst.executeUpdate();

				connection.close(); 
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				myTools.saveLog(e.toString()+"\r\n");
//			}//执行查询语句SQL
    		
    	return rs;
    }
    /**
     * 获得版本号和新版本路径
     * @param type
     * @return
     * @throws SQLException
     */
    public static HashMap<String, String> getClientNewVersion(String  type) throws SQLException  
    {
    	    HashMap<String, String> al=new  HashMap<String, String>();
		   
			String sql="SELECT clientVersion,filePath,md5 FROM client_version where type=? order by clientVersion desc limit 1";
			//从连接池获得一个可用的连接
			Connection connection=ConnectionFactory.getConnection();
			PreparedStatement pst;//预编译语句
			ResultSet rs;//返回的结果集
			HashMap<String, String> map=new HashMap<String, String>();
			//	try {
					pst=connection.prepareStatement(sql);
					pst.setString(1, type);//设置参数
					rs=pst.executeQuery();//执行查询语句SQL
					if(rs.next())
					{
						 
						 map.put("newVersion", rs.getString(1));
						 map.put("filePath", rs.getString(2));
						 map.put("hash", rs.getString(3));
					}
					connection.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					myTools.saveLog(e.toString()+"\r\n");
//				}
				
		 
			return map;
    }
    /**
     * 获得最新命令
     * @param serial
     * @return
     * @throws SQLException
     */
    public static ArrayList<HashMap<String, String>> getOrders(String  serial) throws SQLException  
    {
    	ArrayList<HashMap<String, String>> alArrayList =new ArrayList<HashMap<String, String>>();
    	
    	String sql="SELECT code,value1,value2,value3,value4 ,serial,id FROM `order` where serial=?  and send=0  limit 5";
    	
    	Connection connection=ConnectionFactory.getConnection();
		PreparedStatement pst;//预编译语句
		ResultSet rs;//返回的结果集

			//try {
				pst=connection.prepareStatement(sql);
			
				pst.setString(1, serial);//设置参数
				rs=pst.executeQuery();//执行查询语句SQL
				while(rs.next())
				{
					 HashMap<String, String> map=new HashMap<String, String>();
					 map.put("code", rs.getString(1));
					 String v[];
					 for (int i = 2; i <= 5; i++) {
					 	if(rs.getString(i)!=null&&(!rs.getString(i).equals("")))//装填命令参数
					 	{
					 		v=rs.getString(i).split("::");
					 		map.put(v[0], v[1]);
					 	}
					 }
					 map.put("serial", rs.getString(6));
					 map.put("id", rs.getString(7));
					alArrayList.add(map);
				}
				connection.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				myTools.saveLog(e.toString()+"\r\n");
//			}
    	
    	return alArrayList;
    }
    
    public static int updateOrderStatus(int  send, int orderId) throws SQLException 
    {
    	int rs=-1;
    		//构造 SQL 语句
    		String sql="update `order` set send=?  where id=?";
    		//从连接池获得一个可用的连接
    		Connection connection=ConnectionFactory.getConnection();
    		PreparedStatement pst;//预编译语句
    		//try {
				pst=connection.prepareStatement(sql);
				pst.setInt(1, send);//设置参数
				pst.setInt(2, orderId);//设置参数
	    		 rs=pst.executeUpdate();//执行查询语句SQL
	    		connection.close(); 
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				myTools.saveLog(e.toString()+"\r\n");
//			}
			
    	return rs;
    }
    
    
}
