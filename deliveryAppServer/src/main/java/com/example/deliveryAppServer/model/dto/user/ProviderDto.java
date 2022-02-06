package com.example.deliveryAppServer.model.dto.user;

import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
/**
 * This is a utility class used to store provider information that must be sent or received by the server.
 * This class should be used by someone who is not a provider given that there is no sensitive information on it such as username and password
 */
public class ProviderDto {

    private Long id;
    private String telephoneNumber;
    private String providerName;
    private String cuisine;
    private String address;
    private Boolean doDelivering;
    private Boolean doTakeAway;
    private MenuEntity menu;
}
