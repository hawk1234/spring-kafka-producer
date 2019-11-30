package com.mz.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new ValueTypeDescriptorConverter());
    }

    @Value("${kafka.properties.file}")
    private Resource kafkaPropertiesFile;

    @Bean
    public PropertiesFactoryBean kafkaProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(kafkaPropertiesFile);
        return bean;
    }
}
