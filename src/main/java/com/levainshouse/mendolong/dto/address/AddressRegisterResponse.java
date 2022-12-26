package com.levainshouse.mendolong.dto.address;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.entity.Address;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRegisterResponse implements Serializable {

    private final Long addressId;
    private final Double latitude;
    private final Double longitude;

    public AddressRegisterResponse(Address address){
        this.addressId = address.getId();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
    }
}

