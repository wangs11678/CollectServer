package com.rs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.xml.soap.Text;

import com.rs.base.ConnectionFactory;
import com.rs.tools.myTools;

public class VoiceDao {
	 public static int addVoiceOrCall(String filePath,String type,String userId,String stationId,String days) {
		 int r=0;
		//构造 SQL 语句
 		String sql="insert into interaction (userId,stationId,type,filePath,note,md5,sender,played,addTime,days) "
 				+ "values(?,?,?,?,?,?,?,?,?,?)";
 		//从连接池获得一个可用的连接
 		Connection connection=ConnectionFactory.getConnection();
 		PreparedStatement pst;//预编译语句
			try {
	    		pst=connection.prepareStatement(sql);
				pst.setString(1, userId);//设置参数
				pst.setString(2, stationId);//设置参数
				pst.setString(3, type);//设置参数
				pst.setString(4, filePath);//设置参数
				pst.setString(5, "");//设置参数text
				pst.setString(6, "");//md5
				pst.setString(7, "0");//sender
				pst.setString(8, "0");//played
				pst.setLong(9, System.currentTimeMillis());//设置参数
				pst.setString(10, days);
				//pst.setInt(11, 1);
				r = pst.executeUpdate();

				connection.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				myTools.saveLog(e.toString()+"\r\n");
			}//执行语句SQL
		 
		 return r;
		
	}

}
