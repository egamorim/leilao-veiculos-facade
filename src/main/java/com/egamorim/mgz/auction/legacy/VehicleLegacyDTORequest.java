package com.egamorim.mgz.auction.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleLegacyDTORequest {

    @JsonProperty("OPERACAO")
    private LegacyOperationEnum operation;

    @JsonProperty("VEICULO")
    private VehicleLegacyDTO vehicle;
}
