package com.dova.dev.dataFix;

import com.dova.dev.common.JSON;
import com.dova.dev.jdbc.ConnectionTool;
import com.google.common.base.Strings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhendong on 16/7/12.
 */
public class FixCourierBalance {



    public static void fix()throws SQLException{
        Connection connection = ConnectionTool.createConnection("",
                "","");
        if(connection == null){
            System.out.println("建立连接失败");
            return;
        }

        connection.setAutoCommit(true);
        String getWaybill = "select sum(delivery_fee) as fee,courier_id from express_waybill where create_time > ? and state = 10 and courier_id > 0 group by courier_id";
        String update = "update express_courier_balance set total = ?,balance=? where courier_id = ?";
        PreparedStatement getPs = connection.prepareStatement(getWaybill);
        PreparedStatement updatePs = connection.prepareStatement(update);
        getPs.setTimestamp(1, Timestamp.valueOf("2016-07-01 00:00:00"));
        ResultSet resultSet = getPs.executeQuery();
        while (resultSet.next()){
            double fee = resultSet.getBigDecimal(1).doubleValue();
            Long courierId = resultSet.getLong(2);
            updatePs.clearParameters();
            updatePs.setDouble(1,fee);
            updatePs.setDouble(2,fee);
            updatePs.setLong(3, courierId);
            updatePs.executeUpdate();
            int rows = 1;
            if(rows == 1){
                System.out.println(String.format("update succ courierId:%d balance:%.2f", courierId, fee));
            }else {
                System.out.println(String.format("update fail courierId:%d balance:%.2f", courierId, fee));
            }
        }
     }

    public static void main(String[] args)throws Exception{
        fix();
    }
}
