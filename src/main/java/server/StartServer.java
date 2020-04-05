package server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        //ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        Server chatServer = context.getBean("server", Server.class);
        chatServer.startServer();
    }
}
