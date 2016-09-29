package com.dova.dev.port_detector;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by liuzhendong on 16/9/22.
 */
public class ResultCollector extends Actor{

    String file;
    FileChannel  fCh;
    ByteBuffer buff;
    public ResultCollector(String file)throws IOException{
        this.file = file;
        Path path = Paths.get(file);
        Path parent = Paths.get(file).getParent();
        if(!Files.exists(parent)){
            Files.createDirectories(parent);
        }
        fCh = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        buff = ByteBuffer.allocate(1024);
    }

    RandomAccessFile randomAccessFile;
    public ResultCollector(){}

    @Override
    public String emit(){
        return "";
    }

    @Override
    public ResultCollector accept(Object from)throws IOException{
        String tmp = (String)from;
        if(buff.remaining() < tmp.length()){
            buff.flip();
            fCh.write(buff);
            buff.clear();
        }
        buff.put(tmp.getBytes());
        return this;
    }

    @Override
    public ResultCollector clone()throws CloneNotSupportedException{
        ResultCollector tmp = (ResultCollector)super.clone();
        //fch共用 但是buff重启分配
        tmp.buff = ByteBuffer.allocate(1024);
        return tmp;
    }
}
