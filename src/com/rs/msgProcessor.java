package com.rs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

 



import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.rs.tools.myTools;

public class  msgProcessor extends Thread {
	Socket clientSocket;
	public msgProcessor(Socket clientSocket) { 
		 this.clientSocket=clientSocket;
	}

	 
	public void run() {
		//SocketAddress clientAddress=clientSocket.getRemoteSocketAddress();
		
		InputStream in;
		String thisIP=clientSocket.getInetAddress().getHostAddress();
		System.out.println(thisIP+" connected...");
		myTools.saveLog(thisIP+" connected...|Time:"+myTools.getCurrentTime());
		try {
			clientSocket.setSoTimeout(300000);//设置线程超时时间
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			myTools.saveLog(thisIP+" time out...");
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			byte []buffer =new byte[1024*128];//每次读取(read)的大小临时大小 
			
			byte []msgBuffer= new byte[1024*1024*16];// 消息队列16MB
			//byte []singleMsg= new byte[1024*1024*8];
			in = clientSocket.getInputStream();
			
			int msgBufferLength=0;
			int readLength=0;
			int thisMsgLen=0;//  
			do// 
			{
				System.out.println("start to read...wait for data");
				myTools.saveLog("start to read...wait for data");
				readLength=in.read(buffer);//读单次缓冲
				if(readLength!=-1)//若有数据
				{
					myTools.saveLog("data coming...");
					System.arraycopy(buffer, 0, msgBuffer, msgBufferLength, readLength);//从缓冲区拷贝到消息队列
					msgBufferLength+=readLength;// 消息队列长度增加
				    while(msgBufferLength>=4)//如果消息队列长度达到4字节
				    {
				    	if(thisMsgLen==0)//获取头部长度值
				    	{
					    	byte []length=new byte[4];
					    	System.arraycopy(buffer, 0, length, 0, 4);
					    	thisMsgLen=myTools.byte2int(length);//获得消息长度
				    	}
				    	if (msgBufferLength>=thisMsgLen&&thisMsgLen!=0)//若消息队列长度大于等于消息头部标识长度（至少到来一条消息）
				    	{
				    		myTools.saveLog("single msg is ok...");
				    		byte[] msgBody=new byte[thisMsgLen-4];
				    		System.arraycopy(msgBuffer, 4, msgBody, 0, thisMsgLen-4) ;//从msgBuffer拷贝到消息体msgBody
				    		int afterLen=msgBufferLength-thisMsgLen;//获得消息队列中后部数据的长度
				    		System.arraycopy(msgBuffer, thisMsgLen, msgBuffer, 0, afterLen) ;
				    		thisMsgLen=0;//重置当前消息的头部标识长度 
				    		msgBufferLength=afterLen;//重置消息队列长度值
				    		
							String msg =new String(msgBody);//获得消息体的String
							JSONObject fileStruct=new JSONObject(msg);//得到消息体JSON
							System.out.println("command is ok from "+thisIP+", responding... ");
							myTools.saveLog("command is ok from "+thisIP+", responding... ");
							String re=dispatcher.processMsg(fileStruct,clientSocket);//分发到路由处理，并获得发回消息
							 
							
							if (re.equals("")) {//如果无返回消息，返回0000用于测试。
								JSONObject j=new JSONObject();
								j.put("code", "0000");
								OutputStream out=clientSocket.getOutputStream();
								out.write(myTools.getSendBackBytes(j.toString()));
								out.flush();
								System.out.println("send to "+thisIP+" code 0000[test]");
								myTools.saveLog("send to "+thisIP+" code 0000[test]");
							}
							
						}else {
							break;
						}
			    	}
			    }
			}while(readLength!=-1);
			
			
			
		} catch (IOException e ){
			// TODO Auto-generated catch block
			e.printStackTrace();
			myTools.saveLog("msgProcessor IOException..|"+e.toString()+"|"+myTools.getCurrentTime());
			System.out.println("msgProcessor IOException..|"+myTools.getCurrentTime());
			if (clientSocket!=null) {
				try {
					clientSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		catch (JSONException e){
			// TODO: handle exception
			e.printStackTrace();
		
			
			myTools.saveLog(e.toString());
			
		}

		//OutputStream out=clientSocket.getOutputStream();
		//关闭socket
		if (clientSocket!=null) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		myTools.saveLog(thisIP+" closed,exit|Time:"+myTools.getCurrentTime());
		System.out.println(thisIP+" closed,exit|Time:"+myTools.getCurrentTime());
	}

}
