package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bean.User;
import config.UserConfig;

public class UserTest {
    public static void main(String[] args) {
        // 获取容器
        ApplicationContext ac = new AnnotationConfigApplicationContext(UserConfig.class);
        // 获取 bean
        User user = (User)ac.getBean("user");
        System.out.println(user.toString());
    }
}
