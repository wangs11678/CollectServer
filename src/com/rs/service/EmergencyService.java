package com.rs.service;

import java.sql.SQLException;

import javax.tools.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import com.rs.dao.SystemDao;
import com.rs.dao.VoiceDao;
import com.rs.tools.myTools;

public class EmergencyService {
	/**
	 * 紧急呼叫
	 * @param jsonData
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static String emergency(JSONObject jsonData) throws SQLException, JSONException
	{
		String deviceId=SystemDao.getDeviceId(jsonData.getString("serial"),2);//获得紧急呼叫的设备ID号
		JSONObject jobj=jsonData.getJSONObject("data");
		String level=jobj.getString("level");
		String days=myTools.ConvertTimeLongToString(System.currentTimeMillis()).substring(0, 10);
		if (deviceId!=null) {
			//更新最近状态到device表
			SystemDao.updateDeviceStatus(level, "", "", "",myTools.getCurrentTime(), deviceId);
			//写入his表
			 
			SystemDao.AddHis(level, "", "","", System.currentTimeMillis(), deviceId, myTools.getTodayHisTableName());
			//写入interaction 
			String userId=SystemDao.getUserId(jsonData.getString("serial"));
		    //JSONObject dataObject=(JSONObject)jsonData.get("data");
 
		    if (userId!=null&&level!=null) {
				String stationId=SystemDao.getStationId(userId);
				//增加一条Interaction记录
				VoiceDao.addVoiceOrCall("", level, userId, stationId,days);
				boolean exist=SystemDao.checkDaysExist(days,userId);
				if (!exist) {//如果不存在对应日期记录
					SystemDao.AddInteracionDays(days, userId);
				}
				
			}
		}
		
		
		return "";
	}
}
