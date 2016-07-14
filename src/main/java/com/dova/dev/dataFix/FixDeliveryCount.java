package com.dova.dev.dataFix;

import com.dova.dev.jdbc.ConnectionTool;

import java.security.*;
import java.sql.*;
import java.sql.Timestamp;

/**
 * Created by liuzhendong on 16/7/12.
 */
public class FixDeliveryCount {



    public static void fix()throws SQLException{
        Connection connection = ConnectionTool.createConnection("",
                "","");
        if(connection == null){
            System.out.println("建立连接失败");
            return;
        }

        connection.setAutoCommit(true);
        String get = "select * from express_courier_delivery";
        String update = "update express_courier_delivery set total_num = ? where courier_id = ? and `date` = ? ";
        String getWaybillNum = "select count(1) as num from express_waybill where (create_time between ?  and ?) and `state` in (10,11) and courier_id = ? ";
        PreparedStatement getPs = connection.prepareStatement(get);
        PreparedStatement updatePs = connection.prepareStatement(update);
        PreparedStatement getWaybillNumPs = connection.prepareStatement(getWaybillNum);
        ResultSet resultSet = getPs.executeQuery();
        while (resultSet.next()){

            Long courierId = resultSet.getLong(2);
            Date date = resultSet.getDate("date");
            Timestamp begin = Timestamp.valueOf(date.toString() + " 00:00:00");
            Timestamp end = Timestamp.valueOf(date.toString() + " 23:59:59");
            getWaybillNumPs.clearParameters();
            getWaybillNumPs.setTimestamp(1,begin);
            getWaybillNumPs.setTimestamp(2,end);
            getWaybillNumPs.setLong(3,courierId);
            ResultSet waybillNumRes = getWaybillNumPs.executeQuery();
            if(!waybillNumRes.next()){
                System.out.println(String.format("Get No Waybill Res courier_id:%d date:%s",courierId,date));
            }
            Long waybillNum = waybillNumRes.getLong("num");
            updatePs.clearParameters();
            updatePs.setLong(1, waybillNum);
            updatePs.setLong(2, courierId);
            updatePs.setDate(3,date);
            updatePs.executeUpdate();
            int rows = 1;
            if(rows == 1){
                System.out.println(String.format("update succ courierId:%d date:%s num:%d", courierId, date, waybillNum));
            }else {
                System.out.println(String.format("update fail courierId:%d date:%s num:%d", courierId, date, waybillNum));
            }
        }
     }

    public static void main(String[] args)throws Exception{
        fix();
    }
}
