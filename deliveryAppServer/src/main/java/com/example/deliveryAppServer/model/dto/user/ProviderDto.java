package com.example.deliveryAppServer.model.dto.user;

import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProviderDto {

    private String id;
    private String telephoneNumber;
    private String providerName;
    private String cuisine;
    private String address;
    private Boolean doDelivering;
    private Boolean doTakeAway;
    private MenuEntity menu;
}
