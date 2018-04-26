package com.zml.buildInJetty.Main;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 内置jetty启动控制
 * Created by zml on 2018/4/26.
 */
public class JettyMainRun {
    private static int PORT = 8080;

    private static int MAX_THREAD_NUM = 100;

    private static int MIN_THREAD_NUM = 50;

    private static int IDLE_THREAD_NUM = 20;

    public static void main(String[] args) {
        new JettyMainRun().startJetty();
    }

    // 启动jetty https服务
    public void startJetty() {
        // 实例化Server 配置线程池参数
        Server server = new Server(new QueuedThreadPool(MAX_THREAD_NUM, MIN_THREAD_NUM, IDLE_THREAD_NUM));

        // 加载XML web配置文件
        WebAppContext webContext = new WebAppContext();
        server.setHandler(webContext);
        webContext.setContextPath("/");
        webContext.setResourceBase("./webapp");
        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());

        // 配置通信通道
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        server.setConnectors(new Connector[] { connector });
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭通道
            connector.close();
        }
    }
}
