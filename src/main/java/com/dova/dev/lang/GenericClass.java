package com.dova.dev.lang;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by liuzhendong on 16/6/1.
 */

public class GenericClass<T> {
    private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) { };
    private final Type type = typeToken.getType(); // or getRawType() to return Class<? super T>

    public Type getType() {
        return type;
    }
    public T newInstance()throws Exception{
        return ((Class<T>)getType()).newInstance();
    }

    public static void main(String[] args) throws Exception{
        //注意,生成对象必须输入参数进行重新定义
        GenericClass<String> example = new GenericClass<String>(){};
        System.out.println(example.getType()); // => class java.lang.String
    }
}