package com.example.deliveryAppServer.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * This is a utility class used to store rider information that must be sent or received by the server.
 * This class should be used by someone who is not a rider given that there is no sensitive information on it such as username and password
 */
public class RiderDto {

    private String vehicleType;
    private String name;
    private String telephoneNumber;
    private Long id;
}
