package com.egamorim.mgz.auction.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class VehicleResponse extends RepresentationModel<VehicleResponse> {

    private Long id;
    private BidResponse bid;
    private LotResponse lot;
    private ManufacturerResponse manufacturer;
    private ModelResponse model;
    private Integer manufactureYear;

}
