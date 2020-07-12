package example.pattern.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by shy on 7/12/20.
 */
public class NioClientDemo {
    private Selector selector;

    {
        try {
            selector = SelectorProvider.provider().openSelector();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 8000));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void working() throws IOException {
        while (true) {
            if (!selector.isOpen()) {
                System.out.println("selector is not open");
                break;
            }
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                it.remove();

                if (sk.isConnectable()) {
                    connect(sk);
                } else if (sk.isReadable()) {
                    read(sk);
                }
            }
        }
    }

    private void connect(SelectionKey sk) throws IOException {
        SocketChannel socketChannel = (SocketChannel) sk.channel();
        socketChannel.configureBlocking(false);
        //如果连接还没建立完成，帮助完成连接建立
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();
        }
        //写数据
        socketChannel.write(ByteBuffer.wrap("Hello".getBytes()));
        //注册读事件
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey sk) throws IOException {
        SocketChannel socketChannel = (SocketChannel) sk.channel();
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        byte[] bytes = buffer.array();
        System.out.println(new String(bytes));
        socketChannel.close();
        sk.selector().close();
    }

    public static void main(String[] args) throws IOException {
        NioClientDemo client = new NioClientDemo();
        client.working();
    }
}
