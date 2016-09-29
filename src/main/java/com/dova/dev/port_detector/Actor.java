package com.dova.dev.port_detector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liuzhendong on 16/9/22.
 */
public abstract class Actor implements Cloneable{

    public abstract Actor accept(Object from)throws IOException;
    public abstract Object emit()throws IOException;

    @Override
    public Actor clone()throws CloneNotSupportedException{
        return (Actor) super.clone();
    }
}
