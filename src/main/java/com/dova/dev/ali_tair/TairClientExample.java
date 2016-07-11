package com.dova.dev.ali_tair;

import java.util.ArrayList;
import java.util.List;

import com.dova.dev.Config;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.impl.DefaultTairManager;

/**
 * Created by liuzhendong on 16/6/26.
 */
public class TairClientExample {
    public static void main(String[] args) {

        // 创建config server列表
        List<String> confServers = new ArrayList<String>();
        confServers.add(Config.TAIR_CFG_SERVER);
        //  confServers.add("10.10.7.144:51980"); // 可选

        // 创建客户端实例
        DefaultTairManager tairManager = new DefaultTairManager();
        tairManager.setConfigServerList(confServers);

        // 设置组名
        tairManager.setGroupName("group_1");
        // 初始化客户端
        tairManager.init();


        /*
        // put 10 items
        for (int i = 0; i < 10; i++) {
            // 第一个参数是namespace，第二个是key，第三是value，第四个是版本，第五个是有效时间
            ResultCode result = tairManager.put(0, "k" + i, "v" + i, 0);
            System.out.println("put k" + i + ":" + result.isSuccess());
            if (!result.isSuccess())
                break;
        }
        */
        // get one
        // 第一个参数是namespce，第二个是key
        Result<DataEntry> result = tairManager.get(1, "platformTaobao_"+1467805260);
        System.out.println("get:" + result.isSuccess());
        if (result.isSuccess()) {
            DataEntry entry = result.getValue();
            if (entry != null) {
                // 数据存在
                System.out.println("value is " + entry.getValue().toString() + " v:" + entry.getVersion());
            } else {
                // 数据不存在
                System.out.println("this key doesn't exist.");
            }
        } else {
            // 异常处理
            System.out.println(result.getRc().getMessage());
        }

    }

}


