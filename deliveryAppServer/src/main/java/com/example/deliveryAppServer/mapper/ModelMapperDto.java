package com.example.deliveryAppServer.mapper;

import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dto.order.OrderDto;
import com.example.deliveryAppServer.model.dto.user.ProviderDto;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * It defines and builds the methods needed to convert entities of type dao to dto, and vice-versa.
 * The conversion takes place thanks to the ModelMapper, which automatically recognizes the
 * fields of an entity and translates them into the other.
 */
@Data
public class ModelMapperDto {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * It converts a given ProviderEntity in a ProviderDto
     * @param provider is the ProviderEntity to be converted
     * @return the ProviderDto just converted
     */
    public ProviderDto convertToDto(ProviderEntity provider) {
        return modelMapper.map(provider, ProviderDto.class);
    }

    /**
     * It converts a given ProviderEntity list in a ProviderDto list
     * @param providerList is the ProviderEntity list to be converted in dto
     * @return the List<ProviderDto> just converted
     */
    public List<ProviderDto> convertProviderListToDto(List<ProviderEntity> providerList){
        return Arrays.asList(modelMapper.map(providerList, ProviderDto[].class));
    }

    /**
     * It converts a given OrderEntity list in a OrderDto list
     * @param OrderList is the OrderEntity list to be converted in dto
     * @return the List<OrderDto> just converted
     */
    public List<OrderDto> convertOrderListToDto(List<OrderEntity> OrderList){

        List<OrderDto> orderDtoList = Arrays.asList(modelMapper.map(OrderList, OrderDto[].class));
        for (OrderDto orderDto: orderDtoList) {
           orderDto.getProvider().setMenu(null);
        }
        return orderDtoList;
    }

    /**
     * It converts a given OrderEntity in a OrderDto
     * @param order is the OrderEntity to be converted
     * @return the OrderDto just converted
     */
    public OrderDto convertOrderToDto(OrderEntity order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.getProvider().setMenu(null);
        return orderDto;
    }

    /**
     * It converts a given OrderDto in a OrderEntity
     * @param orderDto is the OrderDto to be converted
     * @return the OrderEntity just converted
     */
    public OrderEntity convertOrderDtoToDao(OrderDto orderDto) {

        return modelMapper.map(orderDto, OrderEntity.class);
    }

}
