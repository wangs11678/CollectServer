package com.rs.tools;

import java.io.FileInputStream; 
import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	    FileInputStream in = new FileInputStream("1.mp3");  
//      File file = new File(dec);  
//      if(!file.exists())  
//          file.createNewFile();  
          
        //FileOutputStream out = new FileOutputStream(dec);  
        byte buffer[] = new byte[1024*1024];  
        //byte buffer2[] = new byte[1024*1024];  
        int i;
        int count=0;  
        while((i=in.read(buffer))!=-1){  
           count+=i;
        }  
        byte[] buffer2=new byte[count];
        System.arraycopy(buffer, 0, buffer2, 0, count);//从缓冲区拷贝到消息队列
        buffer2=	base64.encode(buffer2, base64.NO_WRAP);
       String str= new String(buffer2);
       System.out.print(str);
        in.close();  
	}

}
