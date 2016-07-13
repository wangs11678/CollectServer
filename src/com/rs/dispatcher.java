package com.rs;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.rs.service.EmergencyService;
import com.rs.service.SystemService;
import com.rs.service.VoiceService;
import com.rs.tools.myTools;

public class dispatcher {
	/**
	 * single message process router
	 * @param jsonData
	 * @return process result
	 * @throws JSONException
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public  static String processMsg(JSONObject jsonData,Socket clientSocket)  
	{
		try {
			String code=jsonData.getString("code");
			//heart tick
			if (code.equals("1100")) {
					//心跳包
					 SystemService.tick(jsonData);
					//查询任务指令
					 SystemService.SendBackOrders(jsonData, clientSocket);
					 return "";
			}
			else if (code.equals("1101")) {
					return SystemService.start(jsonData);
			}
			else if (code.equals("1102")) {
					return SystemService.stop(jsonData);
			}
			else if (code.equals("1103")) {
 
					return SystemService.restart(jsonData);
			}
			else if (code.equals("1104")) {
					return SystemService.upgrade(jsonData);
			}
			else if (code.equals("1105")) {
					return SystemService.upgraded(jsonData);
			}
			//检查新版本
			else if (code.equals("1106")) {
					return SystemService.askVersion(jsonData,clientSocket);
			}
			//紧急呼叫
			else if(code.equals("1110")){
					return EmergencyService.emergency(jsonData);
			}
			//file upload
			else if (code.equals("1211")) {
					return VoiceService.voiceUpload(jsonData,clientSocket);
			}
			//处理异常
		} catch (SQLException e) {
			e.printStackTrace();
			myTools.saveLog(e.toString()+"|Time:"+myTools.getCurrentTime()+"\r\n");
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			myTools.saveLog(e.toString()+"|Time:"+myTools.getCurrentTime()+"\r\n");
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			myTools.saveLog(e.toString()+"|Time:"+myTools.getCurrentTime()+"\r\n");
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return "Err";
		
		
	}
}
