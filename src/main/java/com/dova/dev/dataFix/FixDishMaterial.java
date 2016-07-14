package com.dova.dev.dataFix;

import com.dova.dev.common.JSON;
import com.dova.dev.jdbc.ConnectionTool;
import com.google.common.base.Strings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhendong on 16/7/12.
 */
public class FixDishMaterial {



    public static final String dishIds = "4832,4839";

    public static void fix()throws SQLException{
        Connection connection = ConnectionTool.createConnection("",
                "","");
        if(connection == null){
            System.out.println("建立连接失败");
            return;
        }

        connection.setAutoCommit(true);
        String getById = "select * from food_kitchen_dish where id = ? ";
        String update = "update food_kitchen_dish set materials = ? where id = ?";
        PreparedStatement getPs = connection.prepareStatement(getById);
        PreparedStatement updatePs = connection.prepareStatement(update);
        String[] ids = dishIds.split(",");
        for(String id: ids){
            if(id.length() == 0) continue;
            Long dishId = Long.valueOf(id);
            getPs.clearParameters();
            getPs.setLong(1,dishId);
            ResultSet resultSet = getPs.executeQuery();
            if(!resultSet.next()){
                System.out.println(dishId +":dish does not exist");
            }
            String materialStr = resultSet.getString("materials");
            if(Strings.isNullOrEmpty(materialStr)) {
                System.out.println(dishId + ":dish material is null or empty");
                continue;
            }

            List<DishMaterial> materials = JSON.safeReadList(materialStr,DishMaterial.class);
            String newstr = JSON.safeToJson(fixMaterail(materials));
            updatePs.clearParameters();
            updatePs.setString(1,newstr);
            updatePs.setLong(2,dishId);
            int res = updatePs.executeUpdate();
            if(res !=1){
                System.out.println(dishId + ":update fail ");
            }else {
                System.out.println(dishId + ":update succ ");
            }

        }
     }

    public static List<DishMaterial> fixMaterail(List<DishMaterial> origins){
        List<DishMaterial> res = new ArrayList<>();
        for(DishMaterial dishMaterial : origins){
            if(Strings.isNullOrEmpty(dishMaterial.getVolume())){
                res.add(dishMaterial);
                continue;
            }
            if(dishMaterial.getVolume().contains("0")
                    ||dishMaterial.getVolume().contains("1")
                    ||dishMaterial.getVolume().contains("2")
                    ||dishMaterial.getVolume().contains("3")
                    ||dishMaterial.getVolume().contains("4")
                    ||dishMaterial.getVolume().contains("5")
                    ||dishMaterial.getVolume().contains("6")
                    ||dishMaterial.getVolume().contains("7")
                    ||dishMaterial.getVolume().contains("8")
                    ||dishMaterial.getVolume().contains("9")){
                res.add(dishMaterial);
                continue;
            }
            DishMaterial tmp = new DishMaterial();
            tmp.setName(dishMaterial.getVolume());
            tmp.setVolume("");
            dishMaterial.setVolume("");
            res.add(dishMaterial);
            res.add(tmp);
        }
        return res;
    }


    public static void main(String[] args)throws Exception{
        fix();
    }
}
