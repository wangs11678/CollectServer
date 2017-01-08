package com.rs.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
 



import com.rs.dao.SystemDao;
import com.rs.dao.VoiceDao;
import com.rs.tools.base64;
import com.rs.tools.globalSet;
import com.rs.tools.myTools;

public class SystemService {
	
//	/**
//	 * 心跳包处理
//	 * @param jsonData
//	 * @return process result
//	 * @throws JSONException 
//	 * @throws SQLException 
//	 */
//	public static String tick(JSONObject jsonData) throws SQLException, JSONException
//	{
//		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
//		if (deviceId!=null) {
//			//更新最近状态到device表
//			SystemDao.updateDeviceStatus("0", "", "", "",myTools.getCurrentTime(), deviceId);
//			//写入his表
//			SystemDao.AddHis("0", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//		}
//		
//		return "";
//	}
//	/**
//	 * 开机
//	 * @param jsonData
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 */
//	public static String start(JSONObject jsonData) throws SQLException, JSONException
//	{
//		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
//		if (deviceId!=null) {
//			//更新最近状态到device表
//			SystemDao.updateDeviceStatus("1", "", "","", myTools.getCurrentTime(), deviceId);
//			//写入his表
//			SystemDao.AddHis("1", "", "", "",System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//		}
//		return "";
//	}
//	/**
//	 * 即将关机
//	 * @param jsonData
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 */
//	public static String stop(JSONObject jsonData) throws SQLException, JSONException
//	{
//		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
//		if (deviceId!=null) {
//			//更新最近状态到device表
//			SystemDao.updateDeviceStatus("2", "", "", "",myTools.getCurrentTime(), deviceId);
//			//写入his表
//			SystemDao.AddHis("2", "", "", "",System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//		}
//		return "";
//	}
//	/**
//	 * 即将重启
//	 * @param jsonData
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 */
//	
//	public static String restart(JSONObject jsonData) throws SQLException, JSONException
//	{
//		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
//		if (deviceId!=null) {
//			//更新最近状态到device表
//			SystemDao.updateDeviceStatus("3", "", "","", myTools.getCurrentTime(), deviceId);
//			//写入his表
//			SystemDao.AddHis("3", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//		}
//		return "";
//	}
//	/**
//	 * 即将升级
//	 * @param jsonData
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 */
//	
//	public static String upgrade(JSONObject jsonData) throws SQLException, JSONException
//	{
//		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
//		JSONObject data=(JSONObject)jsonData.get("data");
//		if (deviceId!=null) {
//			//更新最近状态到device表
//			SystemDao.updateDeviceStatus("4", data.getString("currentVsersion"), data.getString("targetVersion"),"", myTools.getCurrentTime(), deviceId);
//			//写入his表
//			SystemDao.AddHis("4", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//		}
//		return "";
//	}
//	/**
//	 * 升级完成
//	 * @param jsonData
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 */
//	
//	public static String upgraded(JSONObject jsonData) throws SQLException, JSONException
//	{
//		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),1);
//		JSONObject data=(JSONObject)jsonData.get("data");
//		if (deviceId!=null) {
//			//更新最近状态到device表
//			SystemDao.updateDeviceStatus("5", data.getString("currentVsersion"), "","", myTools.getCurrentTime(), deviceId);
//			//写入his表
//			SystemDao.AddHis("5", "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//		}
//		return "";
//	}
//	/**
//	 * 检查版本
//	 * @param jsonData
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 * @throws IOException 
//	 */
//	public static String askVersion(JSONObject jsonData,Socket clientSocket) throws SQLException, JSONException, IOException
//	{
//		String typeId=SystemDao.getGetwayType(jsonData.getString("serial"));//获得类型1的
//		//JSONObject data=(JSONObject)jsonData.get("data");
//		if (typeId!=null) {
//			//得到最新版本
//			HashMap<String, String> hm=SystemDao.getClientNewVersion(typeId);
//			String backData=myTools.generateJsonData(jsonData, hm, "2100", "newVersion");
//			//发回数据
//			OutputStream outer=clientSocket.getOutputStream();
//			outer.write(myTools.getSendBackBytes(backData));
//			outer.flush();
//		}
//		return "sent";
//	}

	/**
	 * 添加一条识别记录
	 * @param data
	 * @param len
	 * @param clientSocket
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 * @throws IOException
	 */
	public static String addRecognition(byte[] data, int len,Socket clientSocket) throws SQLException, JSONException, IOException
	{
		byte[] deviceSerial=new byte[4];
		System.arraycopy(data, 0, deviceSerial, 0, 4) ;
		int ds=myTools.byte2int(deviceSerial);//得到设备号
		System.out.print("devNo: ");
		System.out.println(ds);
		
		byte[] msgId=new byte[4];
		System.arraycopy(data, 4, msgId, 0, 4) ;
		int msgID=myTools.byte2int(msgId);//得到消息 ID;
		System.out.print("msgID: ");
		System.out.println(msgID);
		
		byte[] rsNumber=new byte[4];
		System.arraycopy(data, 8, rsNumber, 0, 4) ;
		float rs=myTools.byte2float(rsNumber, 0);//得到计算结果(内容)
		System.out.print("result: ");
		System.out.println(rs);
		
		//增加到数据库
		//SystemDao.insertRecognition(ds, rs+"", System.currentTimeMillis()+"",msgID);
		
		return "";
	}
	
	
	
//	/**
//	 * 查询并发回命令
//	 * @param jsonData
//	 * @param clientSocket
//	 * @return
//	 * @throws SQLException
//	 * @throws JSONException
//	 * @throws IOException
//	 */
//	public static String SendBackOrders(JSONObject jsonData,Socket clientSocket) throws SQLException, JSONException, IOException
//	{
//		ArrayList<HashMap<String, String>> obal=SystemDao.getOrders(jsonData.getString("serial"));//从命令服务器中获得命令集
//		OutputStream outer=clientSocket.getOutputStream();
//		if (obal.size()==0) {//如果没有命令
//			String noOrderString=myTools.generateJsonData(jsonData, null, "2101", "noOrder");
//			outer.write(myTools.getSendBackBytes(noOrderString));//写入缓冲
//			outer.flush();//发送
//		}
//		
//		for (int i = 0; i < obal.size(); i++) {
//			SystemDao.updateOrderStatus(1, Integer.parseInt(obal.get(i).get("id")));//在命令数据库中标记已发送
//			String backData=myTools.generateOrderData(obal.get(i));//构造发回的json格式
//			
//			outer.write(myTools.getSendBackBytes(backData));//写入缓冲
//			outer.flush();//发送
//		}
//			//发回数据
//			
//		
//		return "sendt";
//	}
	/**
	 * 保存静态文件
	 * @param data
	 * @param len
	 * @param clientSocket
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static String saveImg(byte[] data, int len,Socket clientSocket) throws JSONException, IOException, SQLException
	{

		
		byte[] deviceSerial=new byte[4];
		System.arraycopy(data, 0, deviceSerial, 0, 4) ;
		int ds=myTools.byte2int(deviceSerial);//得到设备号，长度4
		System.out.print("devNo: ");
		System.out.println(ds);
		
		byte[] msgId=new byte[4];
		System.arraycopy(data, 4, msgId, 0, 4) ;
		int msgID=myTools.byte2int(msgId);//得到消息 ID; 长度4
		System.out.print("msgID: ");
		System.out.println(msgID);
		
		
		//byte[] rsNumber=new byte[4];				
		//System.arraycopy(data, 8, rsNumber, 0, len-24) ;//得到结果(二进制数据)，变长
		
        byte[] rsNumber=new byte[len-12];		
		System.out.println(len-12);
		
		
		
		//System.out.println();
		System.arraycopy(data, 8, rsNumber, 0, len-12);
		
		//System.out.println(Arrays.toString(rsNumber));
		
	
		//System.out.println();
		
		
		//得到二进制数据
		byte []fileOupt=rsNumber;// base64.decode(fileString, base64.NO_WRAP);//得到byte字节
		
	
		//检查目录是否存在
		String dirName=myTools.getCurrentTime().substring(0,10);
		String basePath=globalSet.uploadFilePath+dirName+"/";
		File file =new File(basePath);
		if (!file.exists()) {
			file.mkdir();
		}
		//生成文件名
		
		
		String justFileName=System.currentTimeMillis()+myTools.getRandomString(6)+".jpg";
		String fileFullName=basePath+justFileName;
		
		System.out.println(fileFullName);
	
		
		RandomAccessFile randomFile = new RandomAccessFile(fileFullName, "rw"); 
				
		long fileLength = randomFile.length();  
		
		randomFile.seek(fileLength);  //移到文件头
		//randomFile.writeBytes(fileString);
	   
	    randomFile.write(fileOupt);  //写入文件
	   
	    randomFile.close();  

	    //记录到会话数据库
	    //    
	    //String UrlFullName="fileserver/"+dirName+"/"+justFileName;
	    //if (ds!=0) {
	    //		//修改photoUrl
	    //		SystemDao.updatePhotoUrl(ds, msgID, UrlFullName);
	    //}
		 
	    
		return "";
	}

}
