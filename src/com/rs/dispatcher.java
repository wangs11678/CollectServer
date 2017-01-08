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
	public  static String processMsg(byte[] data ,int len,Socket clientSocket) throws SQLException, JSONException, IOException  
	{
		//当前只有一种消息
		byte commandByte[]=new byte[4];
		
		String returnString="";//初始化返回字符串
		System.arraycopy(data, 0, commandByte, 0, 4) ;
		int command=myTools.byte2int(commandByte);//得到命令
		
		System.out.print("cmdNo: ");
		System.out.println(command);
		
		int bodyLen= len-4;
		
		//System.out.println(command);
		
		byte dataBody[]=new byte[bodyLen];
		System.arraycopy(data, 4, dataBody, 0, bodyLen) ;//得到命令编号之后的数据块
		switch (command) {
		case 101:
			returnString= SystemService.addRecognition(dataBody, bodyLen, clientSocket);
			break;
		case 102:
			returnString= SystemService.saveImg(dataBody, bodyLen, clientSocket);
			break;
		default:
			break;
		}
			
			
		return returnString;
	}
}
