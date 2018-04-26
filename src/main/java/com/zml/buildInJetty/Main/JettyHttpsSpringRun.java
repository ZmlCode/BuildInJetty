package com.zml.buildInJetty.Main;

import org.eclipse.jetty.server.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 内置jetty启动控制 https结合证书ssl spring配置方式
 * Created by zhangmeili on 2018/4/26.
 */
public class JettyHttpsSpringRun {

    public static void main(String[] args) {
        new JettyHttpsSpringRun().startJetty();
    }

    // 启动jetty https服务
    public void startJetty() {

        System.out.println("读取关于内置jetty的spring配置");
        //读取关于内置jetty的spring配置
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring-jetty.xml");
        classPathXmlApplicationContext.refresh();

        //加载上下文
        ApplicationContext applicationContext = classPathXmlApplicationContext;

        // 实例化Server 配置线程池参数
        Server server = applicationContext.getBean(Server.class);
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭通道r
        }
    }
}
