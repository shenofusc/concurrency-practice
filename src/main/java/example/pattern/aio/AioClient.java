package example.pattern.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by shy on 7/13/20.
 */
public class AioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        final AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
        clientChannel.connect(new InetSocketAddress("localhost", 8000), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                clientChannel.write(ByteBuffer.wrap("Hello".getBytes()), null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        final ByteBuffer buffer = ByteBuffer.allocate(1024);
                        clientChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                buffer.flip();
                                System.out.println(new String(buffer.array()));
                                try {
                                    clientChannel.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {

                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {

                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
        Thread.sleep(1000);
    }
}
