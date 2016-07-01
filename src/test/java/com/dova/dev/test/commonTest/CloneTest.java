package com.dova.dev.test.commonTest;

import com.dova.dev.algorithems.FlowControl;
import com.dova.dev.common.TimeCounter;
import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhendong on 16/7/1.
 */
public class CloneTest {
    class Base implements Cloneable{
        String a;
        int b;
        Integer c;
        List<String> childs;
        public Base(){
            childs = Lists.newArrayList("1","2","3");
        }

        @Override
        public boolean equals(Object other){
            if(other == null || !(other instanceof Base)){
                return false;
            }
            if(this == other){
                return true;
            }
            Base base = (Base)other;

            if(!Objects.equals(this.a,base.a)){
              return false;
            }
            if(this.b !=  base.b){
                return false;
            }
            if(!Objects.equals(this.c, base.c)){
                return false;
            }
            if(!Objects.equals(this.childs, base.childs)){
                return false;
            }
            return true;
        }

        @Override
        public Base clone() {
            Base base = null;
            try {
                base = (Base)super.clone();
            }catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            return  base;
        }
    }

    @Test
    public void  testClone(){
        Object a = new Base();
        Base base1 = new Base();
        Base base2 = base1.clone();
        System.out.println(base2.equals(base1));
        System.out.println(base2.childs == base1.childs);
        System.out.println(base2.a == base1.a);
        System.out.println(base2.b == base1.b);
        System.out.println(base2.c == base1.c);
        System.out.println(ToStringBuilder.reflectionToString(base1, ToStringStyle.SHORT_PREFIX_STYLE));
        System.out.println(ToStringBuilder.reflectionToString(base2, ToStringStyle.SHORT_PREFIX_STYLE));
    }

    @Test
    public void testSerialize(){
        int num = 1000 * 1000;
        TimeCounter.sStartOrAdd("ToStringBuilder-" +num);
        FlowControl flowControl  = new FlowControl(0,0, TimeUnit.DAYS,0,0);
        System.out.println(ToStringBuilder.reflectionToString(flowControl, ToStringStyle.SHORT_PREFIX_STYLE));
        for (int i = 0; i < num; i++){
            ToStringBuilder.reflectionToString(flowControl, ToStringStyle.SHORT_PREFIX_STYLE);
        }
        TimeCounter.sEndAndPrint("ToStringBuilder-" +num);
    }
}
