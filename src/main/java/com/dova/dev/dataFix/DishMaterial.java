package com.dova.dev.dataFix;

/**
 * Created by liuzhendong on 16/5/4.
 */
public class DishMaterial {
    private String name;
    private String volume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString(){
        return  String.format("{\"name\":\"%s\",\"volume\":\"%s\"}",this.name, this.getVolume());
    }

}
