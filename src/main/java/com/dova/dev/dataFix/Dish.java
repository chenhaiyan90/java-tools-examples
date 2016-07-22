package com.dova.dev.dataFix;

/**
 * Created by liuzhendong on 16/7/22.
 */
public class Dish {
    public long dishId;
    public DishTypeUtil.DishType dishType;

    public Dish(long dishId, DishTypeUtil.DishType dishType){
        this.dishId = dishId;
        this.dishType = dishType;
    }
}
