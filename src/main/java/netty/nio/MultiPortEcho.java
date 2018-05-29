package netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * java nio
 */
public class MultiPortEcho {

    private int ports[];
    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public MultiPortEcho(int ports[]) throws IOException {
        this.ports = ports;
        go();
    }

    private void go() throws IOException {
        // 1. 创建一个selector，select是NIO中的核心对象
        // 它用来监听各种感兴趣的IO事件
        Selector selector = Selector.open();
        // 为每个端口打开一个监听, 并把这些监听注册到selector中
        for (int i = 0; i < ports.length; ++i) {
            //2. 打开一个ServerSocketChannel
            //其实我们没监听一个端口就需要一个channel
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);//设置为非阻塞
            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);//监听一个端口
            //3. 注册到selector
            //register的第一个参数永远都是selector
            //第二个参数是我们要监听的事件
            //OP_ACCEPT是新建立连接的事件
            //也是适用于ServerSocketChannel的唯一事件类型
            SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Going to listen on " + ports[i]);
        }
        //4. 开始循环，我们已经注册了一些IO兴趣事件
        while (true) {
            //这个方法会阻塞，直到至少有一个已注册的事件发生。当一个或者更多的事件发生时
            // select() 方法将返回所发生的事件的数量。
            int num = selector.select();
            //返回发生了事件的 SelectionKey 对象的一个 集合
            Set selectedKeys = selector.selectedKeys();
            //我们通过迭代 SelectionKeys 并依次处理每个 SelectionKey 来处理事件
            //对于每一个 SelectionKey，您必须确定发生的是什么 I/O 事件，以及这个事件影响哪些 I/O 对象。
            Iterator it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                //5. 监听新连接。程序执行到这里，我们仅注册了 ServerSocketChannel
                //并且仅注册它们“接收”事件。为确认这一点
                //我们对 SelectionKey 调用 readyOps() 方法，并检查发生了什么类型的事件
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    //6. 接收了一个新连接。因为我们知道这个服务器套接字上有一个传入连接在等待
                    //所以可以安全地接受它；也就是说，不用担心 accept() 操作会阻塞
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 7. 讲新连接注册到selector。将新连接的 SocketChannel 配置为非阻塞的
                    //而且由于接受这个连接的目的是为了读取来自套接字的数据，所以我们还必须将 SocketChannel 注册到 Selector上
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
                    it.remove();
                    System.out.println("Got connection from " + sc);
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();
                    // Echo data
                    int bytesEchoed = 0;
                    while (true) {
                        echoBuffer.clear();
                        int r = sc.read(echoBuffer);
                        if (r <= 0) {
                            break;
                        }
                        echoBuffer.flip();
                        sc.write(echoBuffer);
                        bytesEchoed += r;
                    }
                    System.out.println("Echoed " + bytesEchoed + " from " + sc);
                    it.remove();
                }
            }
            // System.out.println( "going to clear" );
            // selectedKeys.clear();
            // System.out.println( "cleared" );
        }
    }

    static public void main(String args2[]) throws Exception {
        String args[] = {"8082", "9002", "9003"};
        if (args.length <= 0) {
            System.err.println("Usage: java MultiPortEcho port [port port ...]");
            System.exit(1);
        }
        int ports[] = new int[args.length];
        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }
        new MultiPortEcho(ports);
    }
}