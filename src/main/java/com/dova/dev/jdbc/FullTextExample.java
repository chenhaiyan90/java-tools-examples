package com.dova.dev.jdbc;

import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liuzhendong on 16/5/29.
 */
public class FullTextExample {

    private List<String> wordList;
    private Random random = new Random();
    public FullTextExample(){
        try {
            wordList = loadWords();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private List<String> loadWords()throws Exception{
        List<String> words = new ArrayList<>(150 * 1000);
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("data/chinese_phases.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = br.readLine()) != null){
            line = line.trim();
            if(line.length() > 0){
                words.add(line);
            }
        }
        return words;
    }

    private String generateName(){
        StringBuilder sb = new StringBuilder();
        sb.append(wordList.get(random.nextInt(wordList.size()-1)));
        for(int i =0 ;i < 50;i++){
            sb.append(',');
            sb.append(wordList.get(random.nextInt(wordList.size()-1)));
        }
        return sb.toString();
    }

    public void insertCall(){
        String insert = "call insertBillionTest()";
        PreparedStatement ps = null;
        try{
            ps = ConnectionTool.getConnection().prepareCall(insert);
            long start = System.currentTimeMillis();
            ps.executeUpdate();
            long end = System.currentTimeMillis();
            System.out.println(String.format("cost:%d", end-start));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertOneByOne(int num){
        String insert = "insert into `innodb_ft_noindex`(`name`)values(?)";
        PreparedStatement ps = null;
        try{

            ps = ConnectionTool.getConnection().prepareStatement(insert);
            for (int i = 1; i <= num; i++){
                //System.out.println(i);
                String name = generateName();
                ps.setString(1,name);
                ps.addBatch();
                if(i % 10 == 0){
                    long start = System.currentTimeMillis();
                    ps.executeBatch();
                    long end = System.currentTimeMillis();
                    System.out.println("cost exe:" + (end -start) + " " + i);
                    ps.clearBatch();
                }
                /*
                int res = ps.executeUpdate();
                if(res != 1){
                    System.out.println("插入失败" + name);
                }
                ps.clearParameters();
                */
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }


    @Test
    public void testGenerateName()throws Exception{
        long start =  System.currentTimeMillis();
        String name = generateName();
        int byteSize = name.getBytes("utf-8").length;
        System.out.println(name);
        long end = System.currentTimeMillis();
        System.out.println(String.format("size:%d cost:%d", byteSize, end-start));


    }
    @Test
    public void testInsert(){
        int num = 1000;
        long start =  System.currentTimeMillis();
        for (int i = 0; i< num; i++) {
            insertCall();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d",num, end-start));
    }

    @Test
    public void testUpdateSame(){
        String update = "update billion_test set tag_id  = 4 where id = 1";
        PreparedStatement ps = null;
        try{

            ps = ConnectionTool.getConnection().prepareStatement(update);
            int rows = ps.executeUpdate();
            System.out.println("rows:" + rows);
            System.out.println("updateCount:" + ps.getUpdateCount());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
