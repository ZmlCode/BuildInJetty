package com.zml.buildInJetty.Main;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 内置jetty启动控制 https结合证书ssl
 * Created by zml on 2018/4/26.
 */
public class JettyHttpsRun {
    private static int PORT = 8081;

    private static int IDLE_THREAD_NUM = 20;

    private static String HTTPS_SCHEME = "https";

    public static void main(String[] args) {
        new JettyHttpsRun().startJetty();
    }

    // 启动jetty https服务
    public void startJetty() {
        // 实例化Server 配置线程池参数
        Server server = new Server();

        HttpConfiguration httpsConfig = new HttpConfiguration();
        httpsConfig.setSecureScheme(HTTPS_SCHEME);
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpsConfig);

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("webapp/keystore");//私钥
        //使用jdk自带加密生成密钥 keytool -keystore keystore -alias jetty -genkey -keyalg RSA
        sslContextFactory.setKeyStorePassword("zmlpwd");//公钥 密钥库口令
        sslContextFactory.setKeyManagerPassword("zmljettypwd"); //jetty的密钥口令
        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, "http/1.1");

        ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
        httpsConnector.setPort(PORT);
        httpsConnector.setIdleTimeout(IDLE_THREAD_NUM);

        // 加载XML web配置文件
        WebAppContext webContext = new WebAppContext();
        webContext.setContextPath("/");
        webContext.setResourceBase("webapp");

        server.setConnectors(new Connector[] { httpsConnector });
        server.setHandler(webContext);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭通道r
            httpsConnector.close();
        }
    }
}
