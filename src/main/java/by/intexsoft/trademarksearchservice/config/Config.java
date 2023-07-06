package by.intexsoft.trademarksearchservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableSpringDataWebSupport
@EnableMongock
public class Config {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public XmlMapper xmlMapper() {
    return XmlMapper.builder()
            .defaultUseWrapper(false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .addModule(new JavaTimeModule())
            .build();
  }

  @Bean
  public MessageSource validationMessageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages/validation");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean localValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(validationMessageSource());
    return bean;
  }

}
