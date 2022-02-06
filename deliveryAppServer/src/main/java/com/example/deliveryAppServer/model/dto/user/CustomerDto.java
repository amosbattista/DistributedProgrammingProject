package com.example.deliveryAppServer.model.dto.user;

import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
/**
 * This is a utility class used to store costumer information that must be sent or received by the server.
 * This class should be used by someone who is not a costumer given that there is no sensitive information on it such as username and password
 */
public class CustomerDto {

    private Long id;
    private String address;
    private String name;
    private String surname;
    private String telephoneNumber;
}
