package com.dova.dev.netIO;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
 
public class SimpleHttpServer {
    private int port = 8888;
    private ServerSocketChannel serverSocketChannel = null;
    private ExecutorService executorService;
    private static final int EACH_CORE_TNUM = 10;
    private String webRoot  = "./www/";
 
    public SimpleHttpServer() throws IOException {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * EACH_CORE_TNUM);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
    }
 
    public void service() {
        while (true) {
            SocketChannel socketChannel = null;
            try {
                socketChannel = serverSocketChannel.accept();
                System.out.println(socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
                executorService.execute(new Task(socketChannel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void main(String[] args) throws IOException {
        new SimpleHttpServer().service();
    }
    
    
    class Task implements Runnable {
        private SocketChannel socketChannel;
        public Task(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }
     
        @Override
        public void run() {
            request(socketChannel);
        }
     
        private void request(SocketChannel socketChannel) {
            try {
            	long i = 0;
            	//服务端不主动关闭链接，等待客户端发送关闭信息,相当于可以任意长的keep-alived
            	while (i++ < Long.MAX_VALUE){
            		try {
            			ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int res = socketChannel.read(buffer);
                        if(res == -1){
                        	//意味者客户端那边已经没有什么要写，正在申请关闭客户端，客户端FIN，ACK
                        	System.out.println(String.format("port:%d connect:%s open:%s",socketChannel.socket().getPort(),socketChannel.isConnected(), socketChannel.isOpen()));
                        	break;
                        }
                        String request = decode((ByteBuffer)(buffer.flip()));
                        if(request.length() == 0){
                        	System.out.println("request is empty:" + socketChannel.socket().getPort() + "\t" + i
                        			+ "\t" + socketChannel.isConnected()
                        			+ "\t" + res);
                        	Thread.sleep(1000);
                        	continue;
                        }
                        String url = getUrl(request);
                        System.out.println(String.format("port:%d index:%d url:%s",socketChannel.socket().getPort(), i, url));
                        if(url == null){
                        	throw new Exception("没找到url");
                        }
                        String[] ufs = url.split("\\?");
                        FileInputStream in = new FileInputStream(this.getClass().getClassLoader().getResource(webRoot + ufs[0]).getFile());
                        FileChannel fileChannel = in.getChannel();
                        StringBuffer sb = new StringBuffer("heheHTTP/1.1 200 OK\r\n");
                        sb.append("Content-Length:" + fileChannel.size()  + "\r\n");
                        sb.append("Content-Type:" + getContentType(url) + "\r\n");
                        sb.append("\r\n"); //空一行
                        ByteBuffer headBuffer = encode(sb.toString());
                        socketChannel.write(headBuffer);
                        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
                        //headBuffer.rewind();
                        //socketChannel.write(headBuffer);
                        System.out.println(String.format("port:%d index:%d end",socketChannel.socket().getPort(), i));
                    }catch (Exception e) {
		                e.printStackTrace();
		            } 
				}
            }finally {
                try {
                    if (socketChannel != null)
                        socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
     
        private Charset charset = Charset.forName("UTF-8");
     
        private ByteBuffer encode(String string) {
            return (ByteBuffer)(ByteBuffer.allocate(string.length() * 2).put(
                    string.getBytes(charset)).flip());
        }
     
        private String decode(ByteBuffer buffer) {
            byte[] source = new byte[buffer.remaining()];
            buffer.get(source);
            return new String(source, charset);
        }
        
        private Pattern p = Pattern.compile("GET\\s+(\\S*?)\\s+HTTP");
        
        private String getUrl(String request){
        	String url = null;
        	Matcher matcher = p.matcher(request);
        	if(matcher.find()){
        		url  = matcher.group(1);
        	}
        	return url;
        }
        
        private String getContentType(String url){
        	String base = url.toLowerCase();
        	if(base.endsWith(".html") || base.endsWith("htm")){
        		return "text/html;charset=UTF-8";
        	}else if (base.endsWith(".jpg") || url.endsWith("jpeg")) {
				return "image/jpeg";
			}else if (base.endsWith(".gif")) {
				return "image/gif";
			}else if (base.endsWith(".png")) {
				return "image/png";
			}else if (base.endsWith(".css")) {
				return "text/css";
			}else if (base.endsWith(".js")) {
				return "application/x-javascript";
			}
        	return "application/octet-stream";
        }
    }
    
    
 
}
 


