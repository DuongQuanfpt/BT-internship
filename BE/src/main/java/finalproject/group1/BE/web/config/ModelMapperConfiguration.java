package finalproject.group1.BE.web.config;

import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.web.dto.request.ProductRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
