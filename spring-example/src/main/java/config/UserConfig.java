package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bean.User;

@Configuration
public class UserConfig {
    @Bean
    public User user() {
        return new User("zhang", "ada");
    }
}
