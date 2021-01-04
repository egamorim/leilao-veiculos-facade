package com.egamorim.mgz.auction.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
public class VehicleCollectionResponse extends RepresentationModel {

    private List<VehicleResponse> vehicles;
}
