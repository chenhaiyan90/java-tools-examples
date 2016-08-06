package com.dova.dev.dataFix;

import com.dova.dev.jdbc.ConnectionTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by liuzhendong on 16/7/12.
 */
public class FixDishType {


    public static void fix() throws SQLException,IOException,InterruptedException{
        Connection connection = ConnectionTool.createConnection("",
                "","");
        if(connection == null){
            System.out.println("建立连接失败");
            return;
        }

        connection.setAutoCommit(true);
        String update = "update food_kitchen_dish set `type` = ? where id = ?";
        PreparedStatement updatePs = connection.prepareStatement(update);

        BufferedReader br = new BufferedReader(new FileReader(new File("/tmp/dish_wash.csv")));
        String line = null;
        long start = System.currentTimeMillis();
        int num = 0,succNum=0;
        while ((line = br.readLine()) != null){
            if(line.length() == 0) continue;
            String[] items = line.split(",");
            if(items.length < 2){
                throw new RuntimeException("items len < " + items.length);
            }
            num++;
            if(num % 100 == 0){
                Thread.sleep(200);
            }
            Dish dish = new Dish(Long.valueOf(items[0].trim()), DishTypeUtil.parse(items[1].trim()));
            System.out.println(String.format("dish:%d type:%d",dish.dishId, dish.dishType.getValue()));

            updatePs.clearParameters();
            updatePs.setInt(1, dish.dishType.getValue());
            updatePs.setLong(2, dish.dishId);
            int res = updatePs.executeUpdate();
            if(res !=1){
                System.out.println(dish.dishId + ":update fail ");
            }else {
                succNum++;
                System.out.println(dish.dishId + ":update succ ");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d succNum:%d cost:%d ms",num, succNum,end - start));
     }



    public static void main(String[] args)throws Exception{
        fix();
    }
}
