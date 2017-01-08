package com.rs.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.rs.dao.SystemDao;
import com.rs.dao.VoiceDao;
import com.rs.tools.base64;
import com.rs.tools.globalSet;
import com.rs.tools.myTools;

public class VoiceService {
	/**
	 * 语音上传
	 * @param jsonData
	 * @return
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static String voiceUpload(JSONObject jsonData,Socket client) throws JSONException, IOException, SQLException
	{

		String fileString="";
		JSONObject data=(JSONObject)jsonData.get("data");
		fileString =data.getString("file");//得到文件base64编码
		
		//save file
		byte []fileOupt= base64.decode(fileString, base64.NO_WRAP);//得到byte字节
		//检查目录是否存在
		String dirName=myTools.getCurrentTime().substring(0,10);
		//server
		//String basePath=globalSet.uploadFilePath+dirName+"/";
		//local
		String basePath=globalSet.uploadFilePath+dirName+"/";
		File file =new File(basePath);
		if (!file.exists()) {
			file.mkdir();
		}
		String justFileName=System.currentTimeMillis()+myTools.getRandomString(6)+".wav";
		String fileFullName=basePath+justFileName;
		RandomAccessFile randomFile = new RandomAccessFile(fileFullName, "rw"); 
		
		
		long fileLength = randomFile.length();  
	    randomFile.seek(fileLength);  //移到文件头
	    //randomFile.writeBytes(fileString);
	    randomFile.write(fileOupt);  
	    randomFile.close();  
		//---------保存记录到数据库
//	    String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),3);//获得某网关的外放（语音）设备Id
//	    if (deviceId!=null) {
//	    	//参数：文件目录，是否已放（已读），发送方向(1：从网关，2：从服务器 )
//			//SystemDao.updateDeviceStatus(fileFullName, "0", "1", "", myTools.getCurrentTime(), deviceId);
//			//SystemDao.AddHis(fileFullName, "0", "1", "", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
//	    	
//	    	
//	    }
	    //记录到会话数据库
	    String userId=SystemDao.getUserId(jsonData.getString("serial"));
	    //JSONObject dataObject=(JSONObject)jsonData.get("data");
	    //String type=dataObject.getString("level");
	    String UrlFullName="fileserver/"+dirName+"/"+justFileName;
	    if (userId!=null) {
			//String careId=SystemDao.getCareId(userId);
	    	String stationId=SystemDao.getStationId(userId);
			String days=myTools.ConvertTimeLongToString(System.currentTimeMillis()).substring(0, 10);
			VoiceDao.addVoiceOrCall(UrlFullName, "10", userId, stationId,days);
			
			boolean exist=SystemDao.checkDaysExist(days,userId);
			if (!exist) {//如果不存在对应日期记录
				SystemDao.AddInteracionDays(days, userId);
			}
		}
	    
	    //文件路径、是否已读、发送方向(1--网关发送给服务器,2--服务器给网关) 
		 
		//发回收到语音的响应数据
		
		JSONObject j=new JSONObject();
		j.put("code", "0001");
		
		OutputStream outer=client.getOutputStream();
		outer.write(myTools.getSendBackBytes(j.toString()));
		outer.flush();	
	    
		return "sent";
	}
	/**
	 * 测试
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException {
		JSONObject jsonObject=new JSONObject();
		JSONObject o=new JSONObject();
		o.put("file", "AA+");
		jsonObject.put("data", o);
		
		try {
			try {
				voiceUpload(jsonObject,new Socket());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


