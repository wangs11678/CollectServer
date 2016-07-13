package com.rs.base;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;
 

public class ConnectionFactory {

    private ConnectionFactory(){
    }    

    private static ComboPooledDataSource ds = null;

    static {
        try {
            // Logger log = Logger.getLogger("com.mchange"); // 日志
              // log.setLevel(Level.WARNING);
            ds = new ComboPooledDataSource();
            // 设置JDBC的Driver类
              ds.setDriverClass("com.mysql.jdbc.Driver");  // 参数由 Config 类根据配置文件读取
              
            //--------------------以下是生产部署参数-------------------------------------------
            ds.setJdbcUrl("jdbc:mysql://localhost/smart_care?characterEncoding=UTF-8");//生产部署地址
            //ds.setJdbcUrl("jdbc:mysql://202.115.16.248/xrcms");//远程调试
               
         	  ds.setUser("root");
        //   ds.setPassword("root");//测试密码
         ds.setPassword("kjlhDB20160430!");//生产密码
              
         
         ds.setMaxIdleTime(25200);
            // 设置连接池的最大连接数
              ds.setMaxPoolSize(800);
            // 设置连接池的最小连接数
              ds.setMinPoolSize(30);
              ds.setMaxStatements(0);
              ds.setMaxStatementsPerConnection(100);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
  
    public static synchronized Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return con;
    }
    // C3P0 end
}

