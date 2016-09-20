package com.dova.dev.netIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 *
 */
public class SimpleChatServer {
    private int port = 8808;
    private ServerSocketChannel serverSocketChannel = null;

	private SocketChannel socketChannel = null;
    
    private Selector selector = null;


	private BlockingQueue<byte[]> msgs = new ArrayBlockingQueue<byte[]>(10000);

    public SimpleChatServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
		System.out.println(selector.getClass());
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
    
    
    public void service(){
    	while (true) {
    		try {
    			int num = selector.select();
    			//System.out.println("SELECT:" + num);
        		if(num == 0){
        			continue;
        		}
        		Set channelSet = selector.selectedKeys();
        		//System.out.println("SELECT:" + num + ":" + channelSet.size());
        		Iterator it = channelSet.iterator();
        		while(it.hasNext()) {
    				SelectionKey key = (SelectionKey)it.next();
    				if(key.isAcceptable()){
    					SocketChannel socketChannel = serverSocketChannel.accept();
	                    //socketChannel.configureBlocking(false);
	                    socketChannel.socket().setReuseAddress(true);
						if(this.socketChannel == null){
							this.socketChannel = socketChannel;
						}
	                    System.out.println("ACCEPT_NEW_SOCKET:" + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
	                    //socketChannel.register(selector, SelectionKey.OP_READ);
						//socketChannel.register(selector,SelectionKey.OP_WRITE);
    				}else if(key.isReadable()){
    					SocketChannel channel = (SocketChannel)key.channel();
        				ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        				int res = channel.read(byteBuffer);
        				if(res != -1){
        					byteBuffer.flip();
            				byte[] bytes = new byte[byteBuffer.remaining()];
            				byteBuffer.get(bytes);
             				System.out.println(channel.socket().getPort() + ":" + new String(bytes));
        				}else {
        					channel.close();
        					key.cancel();
						}
					}else if(key.isWritable()){
    					SocketChannel channel = (SocketChannel)key.channel();
        				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        				byteBuffer.put("from server".getBytes());
        				byteBuffer.flip();
    					channel.write(byteBuffer);
    					System.out.println("server write:" + new String(byteBuffer.array()));
					}else {
						throw new Exception("invalid key option");
					}
     				it.remove();
    			}
        		//System.out.println("SELECTOR KEY SIZE:" + selector.keys().size());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
    	}
    }

	public void write(){
		ByteBuffer buff = ByteBuffer.allocate(100);
		String mess = String.format("from server:" + System.currentTimeMillis());
		for(int i = 0;i < Integer.MAX_VALUE;i++){
			try {
				buff.put(msgs.take());
				buff.flip();
				if(socketChannel == null){
					System.out.println("sleep");
					Thread.sleep(1000);
					continue;
				}
				if(!socketChannel.isConnected() || !socketChannel.isOpen()){
					break;
				}
				socketChannel.write(buff);
				buff.clear();
				System.out.println("send:" + i +"-" + mess);
				//Thread.sleep(200);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}
	public void read(){
		ByteBuffer buff = ByteBuffer.allocate(100);

		for(int i = 0;i < Integer.MAX_VALUE;i++){
			try {

				if(socketChannel == null){
					System.out.println("sleep");
					Thread.sleep(1000);
					continue;
				}
				if(!socketChannel.isConnected() || !socketChannel.isOpen()){
					break;
				}
				int res = socketChannel.read(buff);
				buff.flip();
				msgs.offer(buff.array());
				System.out.println("receive:" + res);
				buff.clear();
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}
    private class HandleAccept implements Runnable{
    	public void run() {
    		System.out.println("HandleAccept start");
            while (true) {
                SocketChannel socketChannel = null;
                try {
                    socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.socket().setReuseAddress(true);
                    System.out.println("ACCEPT_NEW_SOCKET:" + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("register over");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private class HandleSelector implements Runnable{
    	public void run(){
    		System.out.println("HandleSelector start");
        	while (true) {
        		try {
        			System.out.println("Start to select:");
        			int num = selector.select();
        			System.out.println("SELECT" + num);
            		if(num <= 0){
            			continue;
            		}
            		Set channelSet = selector.selectedKeys();
            		Iterator it = channelSet.iterator();
            		while(it.hasNext()) {
        				SelectionKey key = (SelectionKey)it.next();
        				SocketChannel channel = (SocketChannel)key.channel();
        				ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        				channel.read(byteBuffer);
        				byteBuffer.flip();
        				byte[] bytes = new byte[byteBuffer.remaining()];
        				byteBuffer.get(bytes);
         				System.out.println(channel.socket().getPort() + ":" + new String(bytes));
         				it.remove();
        			}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
        	}
        }
    }
    
 
    public static void main(String[] args) throws Exception {
		final SimpleChatServer simpleChatServer = new SimpleChatServer();
		Runnable wrie = new Runnable(){
			public void run(){
				simpleChatServer.write();
			}
		};
		Runnable read = new Runnable(){
			public void run(){
				simpleChatServer.read();
			}
		};

		Thread w = new Thread(wrie);
		Thread r = new Thread(read);
		w.start();
		r.start();
        simpleChatServer.service();
    }
}
 


