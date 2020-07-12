package example.pattern.nio;

import example.basic.RndTask;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shy on 7/12/20.
 */
public class NioMultiThreadEchoServer {
    private Selector selector;//NIO selector
    private static Map<Socket, Long> timeStat = new HashMap<>(10240);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    private void startServer() throws Exception {
        selector = SelectorProvider.provider().openSelector();
        //创建一个新的Channel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        //绑定Channel的Socket到指定地址和端口
        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.socket().bind(isa);

        //为Channel注册感兴趣的事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        for (; ; ) {
            //阻塞等待数据准备好
            selector.select();
            //获取准备就绪的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                it.remove();

                if (sk.isAcceptable()) {
                    doAccept(sk);
                } else if (sk.isValid() && sk.isReadable()) {
                    if (!timeStat.containsKey(((SocketChannel) sk.channel()).socket())) {
                        timeStat.put(((SocketChannel) sk.channel()).socket(),
                                System.currentTimeMillis());
                        doRead(sk);
                    }
                } else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    long e = System.currentTimeMillis();
                    long b = timeStat.get(((SocketChannel) sk.channel()).socket());
                    System.out.println("server process spend:" + (e - b) + "ms");
                }
            }
        }
    }

    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;
        try {
            //新的连接建立，获取和客户端通信的Channel
            clientChannel = server.accept();
            //配置连接为非阻塞模式
            clientChannel.configureBlocking(false);

            //绑定Channel到Selector，监听读事件
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            //绑定附件到SelectionKey上
            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);

            //获取并打印客户端地址
            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doRead(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;

        try {
            len = channel.read(bb);
            if (len < 0) {
                disconnect(sk);
                return;
            }
        } catch (IOException e) {
            System.out.println("failed to read from client");
            e.printStackTrace();
            disconnect(sk);
            return;
        }
        //重置缓冲区，为数据处理做准备
        bb.flip();
        //在线程池中处理读到的数据
        exec.execute(new HandleMsg(sk, bb));
    }

    private void doWrite(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();

        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(sk);
                return;
            }

            if (bb.remaining() == 0) {
                outq.removeLast();
            }
        } catch (IOException e) {
            System.out.println("failed to write to client");
            e.printStackTrace();
            disconnect(sk);
        }

        //所有数据发送完毕，不再监听写事件，避免无谓的调用
        if (outq.size() == 0) {
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void disconnect(SelectionKey sk) {
        sk.cancel();
    }

    class HandleMsg implements Runnable {
        SelectionKey sk;
        ByteBuffer bb;

        public HandleMsg(SelectionKey sk, ByteBuffer bb) {
            this.sk = sk;
            this.bb = bb;
        }

        @Override
        public void run() {
            //把数据绑定到SelectionKey的EchoClient对象上
            EchoClient echoClient = (EchoClient) sk.attachment();
            echoClient.enqueue(bb);
            //同时监听读写事件
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            //强迫Selector#select()方法立刻返回
            //应该是为了让修改后的监听配置立刻生效
            selector.wakeup();
        }
    }

    static class EchoClient {
        private LinkedList<ByteBuffer> linkedList;

        public EchoClient() {
            this.linkedList = new LinkedList<>();
        }

        public LinkedList<ByteBuffer> getOutputQueue() {
            return linkedList;
        }

        public void enqueue(ByteBuffer byteBuffer) {
            linkedList.addFirst(byteBuffer);
        }
    }

    public static void main(String[] args) throws Exception {
        new NioMultiThreadEchoServer().startServer();
    }
}
