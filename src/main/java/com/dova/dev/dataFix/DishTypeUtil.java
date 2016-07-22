package com.dova.dev.dataFix;

/**
 * Created by liuzhendong on 16/7/22.
 */
public class DishTypeUtil {
    /**
     * 炒菜、双拼、主食、羹汤、凉菜、其他
     *
     * 主食,其他,凉菜,双拼,炒菜,糕点,羹汤
     *
     * DISH:单个菜,TABLE:小饭桌,SET:套餐,STAPLE:主食,SALAD:凉菜,COOKIE:糕点, SOUP:汤饮料
     */
    public enum DishType {
        DISH(1), TABLE(2), SET(3), STAPLE(4), SALAD(5),COOKIE(6),SOUP(7);
        int value;

        private DishType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DishType valueOf(int value) {
            for (DishType dishType : DishType.values()) {
                if (dishType.getValue() == value) return dishType;
            }
            throw new RuntimeException("Invalid dish type value=" + value);
        }
    }

    public static DishType parse(String name){
        switch (name){
            case "主食":
                return DishType.STAPLE;
            case "凉菜":
                return DishType.SALAD;
            case "双拼":
                return DishType.SET;
            case "炒菜":
                return DishType.DISH;
            case "糕点":
                return DishType.COOKIE;
            case "羹汤":
                return DishType.SOUP;
            case "其他":
                return DishType.DISH;
            default:
                throw new RuntimeException("unknown type:" + name);
        }
    }
}
