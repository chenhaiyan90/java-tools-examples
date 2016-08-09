package com.dova.dev.test.extreme;

import com.dova.dev.jdbc.ConnectionTool;
import org.junit.Test;

import java.sql.Connection;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by liuzhendong on 16/8/7.
 */
public class ExtremeConnection {

    @Test
    public void testExtremeConnection(){
        Connection[] connections = new Connection[1000];
        for (int i = 0; i < connections.length; i++) {
            connections[i] = ConnectionTool.createConnection();
            if(connections[i] == null){
                break;
            }
            System.out.println(i);
        }
        LockSupport.park();

    }
}
