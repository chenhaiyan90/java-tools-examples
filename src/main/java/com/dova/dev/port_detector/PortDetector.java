package com.dova.dev.port_detector;

import com.sun.mail.util.SocketConnectException;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by liuzhendong on 16/9/22.
 */
public class PortDetector extends Actor{

    public String currIp;

    public final int TIME_OUT = 100;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public PortDetector accept(Object ip){
        this.currIp
                = (String) ip;
        return this;
    }

    @Override
    public String emit(){
        int port = 6379;
        String time = formatter.format(LocalDateTime.now());
        String format = "%s %s %d "+time+"\n";
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(currIp, port),TIME_OUT);
            socket.setSoTimeout(200);
            StringBuilder sb = new StringBuilder();
            sb.append("*3\r\n");
            sb.append("$3\r\n");
            sb.append("SET\r\n");
            sb.append("$6\r\n");
            sb.append("pubkey\r\n");
            sb.append("$4\r\n");
            sb.append("test\r\n");
            socket.getOutputStream().write(sb.toString().getBytes());
            byte[] input = new byte[100];
            int num = socket.getInputStream().read(input);
            if(num == -1){
                return String.format(format,"D",currIp, port);
            }
            String res = new String(input,0,num);
            if(res.startsWith("-")){
                return String.format(format,"N",currIp, port);
            }
            return String.format(format, "S", currIp, port);
        }catch (SocketTimeoutException | NoRouteToHostException  ignore){
            return  String.format(format,"F",currIp, port);
        }catch (Exception e){
            if(e.getMessage().contains("Connection refused") || e.getMessage().contains("Network is unreachable")){
                return  String.format(format,"F",currIp, port);
            }else {
                System.out.println(String.format("%s %d %s", currIp, port, e.getMessage()+" :" + e.getClass()));
                return  String.format(format,"E",currIp, port);
            }
        }finally {
            try {
                if(socket != null) socket.close();
            }catch (Exception e){
                return  String.format(format,"E",currIp, port);
            }
        }
    }
}
