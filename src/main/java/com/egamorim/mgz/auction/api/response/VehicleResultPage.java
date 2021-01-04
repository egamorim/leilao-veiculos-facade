package com.egamorim.mgz.auction.api.response;

import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleResultPage {

    private List<VehicleDTO> vehicles;
    private boolean next;
    private boolean previous;
    private int nextOffset;
    private int previousOffset;

}
