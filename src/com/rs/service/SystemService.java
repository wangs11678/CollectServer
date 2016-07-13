package com.rs.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
 
import com.rs.dao.SystemDao;
import com.rs.tools.myTools;

public class SystemService {
	
	/**
	 * 心跳包处理
	 * @param jsonData
	 * @return process result
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public static String tick(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus("0", "", "", "",myTools.getCurrentTime(), deviceId);
			//写入his表
			SystemDao.AddHis("0", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
		}
		
		return "";
	}
	/**
	 * 开机
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static String start(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus("1", "", "","", myTools.getCurrentTime(), deviceId);
			//写入his表
			SystemDao.AddHis("1", "", "", "",System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
		}
		return "";
	}
	/**
	 * 即将关机
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static String stop(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus("2", "", "", "",myTools.getCurrentTime(), deviceId);
			//写入his表
			SystemDao.AddHis("2", "", "", "",System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
		}
		return "";
	}
	/**
	 * 即将重启
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	
	public static String restart(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus("3", "", "","", myTools.getCurrentTime(), deviceId);
			//写入his表
			SystemDao.AddHis("3", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
		}
		return "";
	}
	/**
	 * 即将升级
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	
	public static String upgrade(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
		JSONObject data=(JSONObject)jsonData.get("data");
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus("4", data.getString("currentVsersion"), data.getString("targetVersion"),"", myTools.getCurrentTime(), deviceId);
			//写入his表
			SystemDao.AddHis("4", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
		}
		return "";
	}
	/**
	 * 升级完成
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	
	public static String upgraded(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
		JSONObject data=(JSONObject)jsonData.get("data");
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus("5", data.getString("currentVsersion"), "","", myTools.getCurrentTime(), deviceId);
			//写入his表
			SystemDao.AddHis("5", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
		}
		return "";
	}
	/**
	 * 检查版本
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 * @throws IOException 
	 */
	public static String askVersion(JSONObject jsonData,Socket clientSocket) throws SQLException, JSONException, IOException
	{
		String typeId=SystemDao.getGetwayType(jsonData.getString("serial"));//获得类型1的
		//JSONObject data=(JSONObject)jsonData.get("data");
		if (typeId!=null) {
			//得到最新版本
			HashMap<String, String> hm=SystemDao.getClientNewVersion(typeId);
			String backData=myTools.generateJsonData(jsonData, hm, "2100", "newVersion");
			//发回数据
			OutputStream outer=clientSocket.getOutputStream();
			outer.write(myTools.getSendBackBytes(backData));
			outer.flush();
		}
		return "sent";
	}
	
	/**
	 * 查询并发回命令
	 * @param jsonData
	 * @param clientSocket
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 * @throws IOException
	 */
	public static String SendBackOrders(JSONObject jsonData,Socket clientSocket) throws SQLException, JSONException, IOException
	{
		ArrayList<HashMap<String, String>> obal=SystemDao.getOrders(jsonData.getString("serial"));//从命令服务器中获得命令集
		OutputStream outer=clientSocket.getOutputStream();
		if (obal.size()==0) {//如果没有命令
			String noOrderString=myTools.generateJsonData(jsonData, null, "2101", "noOrder");
			outer.write(myTools.getSendBackBytes(noOrderString));//写入缓冲
			outer.flush();//发送
		}
		
		for (int i = 0; i < obal.size(); i++) {
			SystemDao.updateOrderStatus(1, Integer.parseInt(obal.get(i).get("id")));//在命令数据库中标记已发送
			String backData=myTools.generateOrderData(obal.get(i));//构造发回的json格式
			
			outer.write(myTools.getSendBackBytes(backData));//写入缓冲
			outer.flush();//发送
		}
			//发回数据
			
		
		return "sendt";
	}
	

}
