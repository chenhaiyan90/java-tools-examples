package com.dova.dev.netIO.echo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

//test
public class EchoClient {

	public SocketChannel socketChannel;
    private Random random = new Random();

	public EchoClient(int port)throws IOException{
		this("127.0.0.1", port);
	}
	public EchoClient(String host,int port)throws IOException{
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(host, port));
		socketChannel.socket().setReuseAddress(true);
		printf("Connect Succ:%d -> %d",socketChannel.socket().getLocalPort(),socketChannel.socket().getPort());
	}

	public void echo(int num){
		ByteBuffer buff = ByteBuffer.allocate(1024);
		long tid = Thread.currentThread().getId();
		int localPort = socketChannel.socket().getLocalPort();
		for (int i=0; i < num;i++){
			try {
				if(!socketChannel.isConnected() || !socketChannel.isOpen()){
					break;
				}
				String mess = String.format("%d-%d echo %d at %d", tid, localPort, i, System.currentTimeMillis());
				buff.clear();
				buff.put(mess.getBytes());
				buff.flip();
				int writeNum = socketChannel.write(buff);
                buff.clear();
                Thread.sleep(random.nextInt(1000));
				int readNum = socketChannel.read(buff);
				if(readNum == -1){
					printf("%d-%d Closed By Server at %d", tid, localPort, System.currentTimeMillis());
                    break;
				}else {
					buff.flip();
					byte[] bytes = new byte[buff.remaining()];
					buff.get(bytes);
					printf("%d-%d Recv Echo:{%s}", tid, localPort, new String(bytes));
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
	public void close(){
		if(socketChannel != null && socketChannel.isOpen()){
			try {
				socketChannel.close();
			}catch (Exception ee){
				ee.printStackTrace();
			}
		}
	}
	public static void printf(String format,Object... objects){
		System.out.println(String.format(format,objects));
	}


    public void testMultiTs(int tsNum)throws IOException, InterruptedException{
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                echo(100);
            }
        };
        Thread[] ts = new Thread[tsNum];
        for (int i = 0; i < tsNum; i++) {
            ts[i] = new Thread(runnable);
        }
        for (int i = 0; i < tsNum; i++) {
            ts[i].start();
        }
        for (int i = 0; i < tsNum; i++) {
            ts[i].join();
        }
    }
	public static void main(String args[]) throws Exception{
		final EchoClient echoClient = new EchoClient(8018);
        echoClient.testMultiTs(2);
        //echoClient.echo(100);
        echoClient.close();

	}
	
}
