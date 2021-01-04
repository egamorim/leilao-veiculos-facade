package com.egamorim.mgz.auction.api.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VehicleRequest {

    private Long id;
    @Valid
    @NotNull(message = "Bid is required.")
    private BidRequest bid;

    @NotNull(message = "Lot is required.")
    private LotRequest lot;

    @NotNull(message = "Manufacturer is required.")
    private ManufacturerRequest manufacturer;

    @NotNull(message = "Model is required.")
    private ModelRequest model;

    @NotNull(message = "Manufacture year is required.")
    private Integer manufactureYear;

}
