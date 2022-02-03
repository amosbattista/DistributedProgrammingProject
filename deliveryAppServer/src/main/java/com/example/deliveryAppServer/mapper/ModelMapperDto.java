package com.example.deliveryAppServer.mapper;

import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dto.order.OrderDto;
import com.example.deliveryAppServer.model.dto.user.ProviderDto;
import lombok.Data;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Data
public class ModelMapperDto {

    @Autowired
    private ModelMapper modelMapper;

    public ProviderDto convertToDto(ProviderEntity provider) {
        ProviderDto postDto = modelMapper.map(provider, ProviderDto.class);
        return postDto;
    }

    public List<ProviderDto> convertProviderListToDto(List<ProviderEntity> providerList){
        List<ProviderDto> postDtoList = Arrays.asList(modelMapper.map(providerList, ProviderDto[].class));
        return postDtoList;
    }

    public List<OrderDto> convertOrderListToDto(List<OrderEntity> OrderList){


        List<OrderDto> orderDtoList = Arrays.asList(modelMapper.map(OrderList, OrderDto[].class));
        for (OrderDto orderDto: orderDtoList) {
           orderDto.getProvider().setMenu(null);
        }
        return orderDtoList;
    }

    public OrderDto convertOrderToDtoForCustomer(OrderEntity order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.getProvider().setMenu(null);
        return orderDto;
    }

    public OrderEntity convertOrderDtoToDao(OrderDto orderDto) {
        OrderEntity order = modelMapper.map(orderDto, OrderEntity.class);

        return order;
    }

    public OrderDto convertOrderToDtoForRider(OrderEntity order) {
        OrderDto postDto = modelMapper.map(order, OrderDto.class);
        postDto.getProvider().setMenu(null);
        postDto.setRider(null);
        return postDto;
    }

    public OrderDto convertOrderToDtoForProvider(OrderEntity order) {
        OrderDto postDto = modelMapper.map(order, OrderDto.class);
        postDto.getProvider().setMenu(null);
        postDto.setProvider(null);
        return postDto;
    }


}
