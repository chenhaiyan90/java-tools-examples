package com.dova.dev.netIO.echo;

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
public class EchoServer {
    protected int port = 8018;
    protected ServerSocketChannel serverSocketChannel = null;
    protected Selector selector = null;
	protected ByteBuffer shareBuff = null;


    public EchoServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.configureBlocking(false);
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		shareBuff = ByteBuffer.allocate(1024);
		printf("Echo Server Init Succ At %d, selector:%s",port, selector.getClass());
	}
    
    
    public void service(){
		//event loop
    	while (true) {
    		try {
    			int selectNum = selector.select();
                //System.out.println("selectNum:" + selectNum);
                if(selectNum == 0){
        			continue;
        		}
        		Set<SelectionKey> channelSet = selector.selectedKeys();
                Iterator it = channelSet.iterator();
                while (it.hasNext()){
                    SelectionKey key = (SelectionKey) it.next();
					try {
						if(key.isAcceptable()){
							SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
							if(socketChannel != null){
								System.out.println("ACCEPT_NEW_SOCKET:" + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
								socketChannel.configureBlocking(false);
                                socketChannel.register(selector, SelectionKey.OP_READ);
							}
						}else if(key.isReadable()){
							SocketChannel socketChannel = (SocketChannel)key.channel();
							shareBuff.clear();
							int readNum = 0;
							while (true){
                                //TODO shareBuff 已经读满会有什么问题
                                //如果之前往一个已经关闭的通道写过数据,则这里会收到reset报文
                                try {
                                    readNum = socketChannel.read(shareBuff);
                                    if(readNum == -1 || readNum == 0) {
                                        break;
                                    }
                                }catch (IOException e){
                                    System.out.println("Read Exception:" + e.getMessage());
                                    readNum = -2;
                                    break;
                                }
							}
							if(readNum < 0){
								System.out.println("SOCKET_IS_CLOSED_BY_CLIENT:" + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
                                key.cancel();
								if(socketChannel.isOpen()){
									socketChannel.close();
								}
							}else {
								shareBuff.flip();
                                if(shareBuff.remaining() > 0){
                                    byte[] bytes = new byte[shareBuff.remaining()];
                                    shareBuff.get(bytes);
                                    printf("READ From %s:%s {%s}", socketChannel.socket().getInetAddress(), socketChannel.socket().getPort(),
                                            new String(bytes));
                                    shareBuff.rewind();
                                    int lastWriteNum = 0;
                                    while (shareBuff.hasRemaining()){
                                        try {
                                            //TODO 如果对方不读,一直写会有什么问题
                                            lastWriteNum = socketChannel.write(shareBuff); //如果对方已经关闭, 初次写入, 会返回reset报文
                                            //shareBuff.rewind();socketChannel.write(shareBuff); //如果对方已经关闭,再次写入, 就会报broken pipe
                                        }catch (IOException ioe){
                                            printf("WRITE To %s:%s Error LastWriteNum:%d",socketChannel.socket().getInetAddress(), socketChannel.socket().getPort(), lastWriteNum);
                                            ioe.printStackTrace();
                                            break;
                                        }
                                    }
                                }
							}
						}else {
                            System.out.println("unknown key:" + key.readyOps());
                        }
					}catch (Exception e){
                        System.out.println("A:" + e.getMessage());
					}finally {
                        it.remove();
                    }
                }
			} catch (Exception e) {
				// TODO: handle exception
                System.out.println("B:" + e.getMessage());
			}
    	}
    }


	public static void printf(String format,Object... objects){
		System.out.println(String.format(format,objects));
	}
 
    public static void main(String[] args) throws Exception {
		final EchoServer echoServer = new EchoServer();
		echoServer.service();
    }
}
 


