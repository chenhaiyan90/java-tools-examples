package com.dova.dev.port_detector;

import java.util.Properties;

/**
 * Created by liuzhendong on 16/9/24.
 */
public class Conf {

    private static Properties properties = new Properties();
    static {
        try {
            properties.load(Conf.class.getClassLoader().getResourceAsStream("bomb_redis.conf"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static final String START_IP = "start_ip";
    public static final String END_IP = "end_ip";
    public static final String PORT_DETECTOR_TS_NUM = "port_detector_ts_num";

    public static final String DATA_DIR = "data_dir";

    public static String get(String key){
        return properties.getProperty(key);
    }

}
