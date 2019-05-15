package com.svintsov.accounts;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svintsov.accounts.service.AccountsRepository;
import com.svintsov.accounts.service.TransferService;
import com.svintsov.accounts.validator.Validator;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.SQLException;

@SpringBootApplication
@EnableConfigurationProperties(AccountsProperties.class)
public class App implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    Validator validator(AccountsProperties accountsProperties) {
        return new Validator(accountsProperties);
    }

    @Bean
    TransferService transferService(AccountsRepository accountsRepository) {
        return new TransferService(accountsRepository);
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
