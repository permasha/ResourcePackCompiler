package us.fihgu.toolbox.web;

import ru.permasha.resourcepackcompiler.util.Debug;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A non block web server that handles the input and output of data though the
 * network. <br>
 */
public abstract class WebServer {
    protected boolean isRunning = false;
    protected InetSocketAddress address;
    protected ServerSocketChannel serverChannel;
    protected SelectorThread acceptSelectorThread;
    protected CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

    public WebServer(InetSocketAddress address) {
        this.address = address;
    }

    protected void init() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(address, address.getPort());
        serverChannel.configureBlocking(false);

        acceptSelectorThread = new SelectorThread(this.getAcceptHandler());
        acceptSelectorThread.register(this.serverChannel);
        acceptSelectorThread.start();
    }

    /**
     * @return a AcceptHandler that will handle {@link SelectionKey#OP_ACCEPT}.
     */
    public abstract SelectionHandler getAcceptHandler();

    public void startServer() {
        if (!this.isRunning) {
            this.isRunning = true;
            try {
                this.init();
            } catch (IOException e) {
                Debug.sayError("Could not start web server on: " + this.address.toString());
                Debug.sayError(
                        "Please check if the Port you have defined in the Config File is not in use and it has been forwarded in your router's settings. If you are running this plugin on a Server hosted by someone else, ask them which ports are open for plugins to use. Common ports are from 8123 and up as 8123 is used by the popular plugin Dynmap.");
                this.stopServer();
                e.printStackTrace();
            }
        } else {
            new Exception("this server is already running on: " + this.address.toString()).printStackTrace();
        }
    }

    public void stopServer() {
        if (this.acceptSelectorThread != null) {
            try {
                synchronized (acceptSelectorThread) {
                    if (!acceptSelectorThread.isStopped()) {
                        acceptSelectorThread.closeSelector();
                        acceptSelectorThread.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.acceptSelectorThread = null;
        }

        if (this.serverChannel != null) {
            try {
                this.serverChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.serverChannel = null;
        }

        this.isRunning = false;
    }

    public void onTimeOut(SelectionKey selectionKey) {
        selectionKey.cancel();
        try {
            selectionKey.channel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}