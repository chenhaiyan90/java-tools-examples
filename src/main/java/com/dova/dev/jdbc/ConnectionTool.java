package com.dova.dev.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by liuzhendong on 16/5/29.
 */
public class ConnectionTool {

    //TODO test 当线程销毁时,会自动释放连接否?
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>(){
        @Override
        protected Connection initialValue(){
           return createConnection();
        }
    };

    static {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private static final String jdbcUrl="jdbc:mysql://127.0.0.1:3205/test?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
    private static final String username="test";
    private static final String password="test";


    public static Connection createConnection(){
       return createConnection(jdbcUrl,username,password);
    }

    public static Connection createConnection(String jdbcUrl, String username, String password){
        try{
            return DriverManager.getConnection(jdbcUrl,username,password);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static  Connection getConnection(){
        return  connectionThreadLocal.get();
    }
}
