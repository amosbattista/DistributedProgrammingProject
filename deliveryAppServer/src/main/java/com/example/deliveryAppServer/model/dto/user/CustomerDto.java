package com.example.deliveryAppServer.model.dto.user;

import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CustomerDto {

    private Long id;
    private String address;
    private String name;
    private String surname;
    private String telephoneNumber;
}
