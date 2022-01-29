package com.example.deliveryAppServer.utils;

import com.example.deliveryAppServer.mapper.ModelMapperDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ModelMapperDto modelMapperDto() {
        return  new ModelMapperDto();
    }
}
