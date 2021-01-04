package com.egamorim.mgz.auction.vehicle.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VehicleDTO {

    private Long id;
    private BidDTO bid;
    private LotDTO lot;
    private ManufacturerDTO manufacturer;
    private ModelDTO model;
    private Integer manufactureYear;
}
