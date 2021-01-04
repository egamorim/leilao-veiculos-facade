package com.egamorim.mgz.auction.api.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRequest {

    @NotNull(message = "Bid user name is required.")
    private String name;
}
