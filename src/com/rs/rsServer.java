package com.rs; 

import java.io.IOException;
 
import java.net.ServerSocket;
import java.net.Socket;

import com.rs.dao.SystemDao;
import com.rs.tools.myTools;
 

public class rsServer {

	/**
	 * @param args
	 */
	public static int PORT = 20101;
	//public static int PORT = 6666;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub		
 
		
		 
			ServerSocket   server=new ServerSocket(PORT);
			System.out.println("collect server started");
			while(true)
			{
				System.out.println("ready to accept new client...");
				Socket clientSocket=server.accept();//listen
				myTools.saveLog("new client is coming...|Time:"+myTools.getCurrentTime());
				Thread msgThread=new Thread(new msgProcessor(clientSocket));
				msgThread.start();
				System.out.println("finish new client...");
				System.out.println();
			}
		 
		
	}

}

 
