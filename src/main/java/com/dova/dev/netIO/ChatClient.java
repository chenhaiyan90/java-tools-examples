package com.dova.dev.netIO;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;

//test
public class ChatClient {

	private SocketChannel socketChannel;
	
	public ChatClient()throws Exception{
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 8808));
		socketChannel.socket().setReuseAddress(true);
		System.out.println(socketChannel.socket().getLocalPort() + "->" + socketChannel.socket().getPort());
	}
	
	public void write(){
		ByteBuffer buff = ByteBuffer.allocate(100);
		String mess = String.format("from server:" + System.currentTimeMillis());
		System.out.println("start to write");
		for(int i = 0;i < Integer.MAX_VALUE;i++){
			try {
				if(!socketChannel.isConnected() || !socketChannel.isOpen()){
					break;
				}
				buff.put((i + ":" + mess).getBytes());
				buff.flip();
				socketChannel.write(buff);
				buff.clear();
				System.out.println("send:" + i +"-" + mess);
				 //Thread.sleep(100);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
	}
	
	public void read(){
		ByteBuffer buff = ByteBuffer.allocate(1024 * 4);
		System.out.println("start to read");

		for(int i = 0;i < Integer.MAX_VALUE;i++){
			try {
				if(!socketChannel.isConnected() || !socketChannel.isOpen()){
					break;
				}
				int res = socketChannel.read(buff);
				buff.flip();
				System.out.println("receive:" + res);
				buff.clear();
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String args[]) throws Exception{
		final ChatClient client = new ChatClient();
		Runnable wrie = new Runnable(){
			public void run(){
				client.write();
			}
		};
		Runnable read = new Runnable(){
			public void run(){
				client.read();
			}
		};
		
		Thread w = new Thread(wrie);
		Thread r = new Thread(read);
		r.start();
		Thread.sleep(5000);
		w.start();
		w.join();
		r.join();
	}
	
}
