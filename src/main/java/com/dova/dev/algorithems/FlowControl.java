package com.dova.dev.algorithems;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhendong on 16/5/21.
 */
public class FlowControl {
    private final int MAX_TOTAL;  //过去一段时间允许的最大总量
    private final int MAX_PER_UNIT;  //过去一段事件单个时间单元允许的最大总量
    private final TimeUnit TIMEUNIT; // 时间周期的单位
    private final int DURATION;  //时间周期的长度
    private final int UNIT_NUM;   //时间周期的个数

    public FlowControl(final int max_total,final int max_per_unit, final  TimeUnit timeUnit,
                       final int duration,final  int UNIT_NUM){
        this.MAX_TOTAL = max_total;
        this.MAX_PER_UNIT = max_per_unit;
        this.TIMEUNIT = timeUnit;
        this.DURATION = duration;
        this.UNIT_NUM = UNIT_NUM;
    }

    public class KeyStruct{
        public int[] count; //过去一定数目的时间单元的流量统计
        public int last_index; //上一次查询时的索引
        public int last_count; //上一次查询的时候的总次数
        public long last_time; //上一次查询的时间,unix时间戳,精确到秒
        public KeyStruct(int unitNum){
            count = new int[unitNum];
            last_count = 0;
            last_index = 0;
            last_time = 0;
        }
        public int getLast_index() {
            return last_index;
        }

        public int getLast_count() {
            return last_count;
        }

        public long getLast_time() {
            return last_time;
        }

        public int[] getCount(){
            return this.count;
        }
        
        public String getTimeStr(){
            return  new Timestamp(last_time * 1000).toString();
        }
    }

    //todo 这里应该用cache, google的LoadingCache是绝佳的选择
    private Map<String, KeyStruct> keyDict =  new ConcurrentHashMap<>();

    public synchronized  boolean accept(String key){
        KeyStruct keyStruct = fresh(key, 1);
        return check(keyStruct);
    }

    public synchronized boolean valid(String key){
        KeyStruct keyStruct = fresh(key, 0);
        return check(keyStruct);
    }


    private synchronized KeyStruct fresh(String key, int n) {
        long curr = System.currentTimeMillis() / 1000;
        if (!keyDict.containsKey(key)) {
            KeyStruct keyStruct = new KeyStruct(UNIT_NUM);
            keyStruct.last_time = curr;
            keyStruct.count[0] = n;
            keyStruct.last_count = n;
            keyDict.put(key, keyStruct);
            return keyStruct;
        } else {
            KeyStruct keyStruct = keyDict.get(key);
            int currUnit = (int) (curr / (TIMEUNIT.toSeconds(DURATION)));
            int lastUnit = (int) (keyStruct.last_time / (TIMEUNIT.toSeconds(DURATION)));
            //如果是同一时间单元
            if (lastUnit == currUnit) {
                keyStruct.count[keyStruct.last_index] += n;
                keyStruct.last_count += n;
                return keyStruct;
            }
            //如果不是同一时间单元
            int add = currUnit - lastUnit;
            int new_index = getIndex(keyStruct.last_index, add);
            int curr_count = 0;
            if (add >= UNIT_NUM) {
                for (int i = 0; i < keyStruct.count.length; i++) {
                    keyStruct.count[i] = 0;
                }
            } else if (add > UNIT_NUM / 2) {
                int new_add = UNIT_NUM - add;
                int increment = 0;
                //获取历史量
                for (int i = 1; i <= new_add; i++) {
                    int index = getIndex(new_index, i);
                    increment += keyStruct.count[index];
                }
                //更新
                for (int i = 1; i <= add; i++) {
                    int index = getIndex(keyStruct.last_index, i);
                    keyStruct.count[index] = 0;
                }
                curr_count = increment;
            } else {
                int decrement = 0;
                //滑动获取差量并更新
                for (int i = 1; i <= add; i++) {
                    int index = getIndex(keyStruct.last_index, i);
                    decrement += keyStruct.count[index];
                    keyStruct.count[index] = 0;
                }
                curr_count = keyStruct.last_count - decrement;
            }
            //更新数据结构
            keyStruct.last_index = new_index;
            keyStruct.count[new_index] += n;
            keyStruct.last_time = curr;
            keyStruct.last_count = curr_count + n;
            return keyStruct;
        }
    }

    private boolean check(KeyStruct keyStruct){
        if(keyStruct != null &&
                (keyStruct.last_count > MAX_TOTAL
                        || keyStruct.count[keyStruct.last_index] > MAX_PER_UNIT)){
            return  false;
        }
        return true;
    }

    private int getIndex(int curr, int add){
        int tmp = add % UNIT_NUM;
        return curr + tmp < UNIT_NUM ? curr + tmp : curr + tmp - UNIT_NUM;
    }

    public int getMAX_TOTAL() {
        return MAX_TOTAL;
    }

    public int getMAX_PER_UNIT() {
        return MAX_PER_UNIT;
    }

    public TimeUnit getTIMEUNIT() {
        return TIMEUNIT;
    }

    public int getDURATION() {
        return DURATION;
    }

    public int getUNIT_NUM() {
        return UNIT_NUM;
    }

    public Map<String, KeyStruct> getKeyDict(){
        return  this.keyDict;
    }
    public void clear(){
        keyDict.clear();
    }
}
