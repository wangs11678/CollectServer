package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
 
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject; 


public class myTools {
	
	public static void main(String[] args) throws JSONException {
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("test", "k1");
		HashMap<String, String> hm=new HashMap<String, String>();
		hm.put("hi", "gogo");
		jsonObject.put("hmap", hm);
		System.out.print(jsonObject.toString());
		
	}
	
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// ���λ 
		targets[1] = (byte) ((res >> 8) & 0xff);// �ε�λ 
		targets[2] = (byte) ((res >> 16) & 0xff);// �θ�λ 
		targets[3] = (byte) (res >>> 24);// ���λ,�޷�����ơ� 
		return targets; 
		} 
	
	public static int byte2int(byte[] res) { 
		// һ��byte�������24λ���0x??000000��������8λ���0x00??0000 

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | ��ʾ��λ�� 
		| ((res[2] << 24) >>> 8) | (res[3] << 24); 
		return targets; 
		} 
	
	
	 /**
	  * 将时间戳转换为人类可读的时间
	  * @param timeLong
	  * @return
	  */
	 public static String ConvertTimeLongToString(long timeLong)
	 {
		 String vv = "" + timeLong;
		long time = Long.valueOf(vv);
		 TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = dateformat.format(c.getTime());
		 return s;
	 }
	
	public static String getCurrentTime(){
		long timeLong=System.currentTimeMillis();
		String vv = "" + timeLong;
		long time = Long.valueOf(vv);
		 TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String s = dateformat.format(c.getTime());
		 return s.substring(0,19);
	}
	public static String getRandomString(int length) { // 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	
	public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
	public static String getTodayHisTableName()
	{
		String dateName=getCurrentTime().substring(0,10);
		dateName=dateName.replace("-", "_");
		return "his_"+dateName;
	}
	/**
	 * 生成发回的json数据报文
	 * @param coming
	 * @param dataList
	 * @return
	 * @throws JSONException 
	 */
	public static String generateJsonData(JSONObject coming, HashMap<String, String> dataList,String code,String msg) throws JSONException
	{
		
		JSONObject jo=new JSONObject();
		jo.put("code", code);
		jo.put("serial", coming.getString("serial"));
		jo.put("msg", msg);
		jo.put("time", myTools.getCurrentTime());
		if (dataList!=null) {
			jo.put("data", dataList);//装入Data
		} 
		
		
		return jo.toString();
	}
	/**
	 * 获得返回命令的json数据
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	public static String generateOrderData(HashMap<String, String> map) throws JSONException
	{
		
		JSONObject jo=new JSONObject();
		jo.put("code", map.get("code"));
		jo.put("serial", map.get("serial"));
		//jo.put("msg", msg);
		jo.put("time", myTools.getCurrentTime());
		map.remove("code");
		map.remove("serial");
		map.remove("id");
		jo.put("data", map);
	
		return jo.toString();
	}
	/**
	 * 根据是发回的json数据，得到byte[]
	 * @param jsonBack
	 * @return
	 */
	public static byte[] getSendBackBytes(String jsonBack)
	{
		byte[] msgBody=jsonBack.getBytes();
		byte[] sendBackBytes =new byte[msgBody.length+4];
		byte[] len=int2byte(msgBody.length+4);
		System.arraycopy(len, 0, sendBackBytes, 0, 4);
		System.arraycopy(msgBody, 0, sendBackBytes, 4, msgBody.length);
		
		
		return sendBackBytes;
	}
	/**
	 * 记录日志
	 * @param str
	 */
	public static void saveLog(String str) {
		BufferedWriter out = null;
		try {
		out = new BufferedWriter(new OutputStreamWriter(
		new FileOutputStream("log/"+getCurrentTime().substring(0,10)+".log", true)));
			out.write(str+"|Time:"+getCurrentTime()+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		try {
			out.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
	}
	
}
