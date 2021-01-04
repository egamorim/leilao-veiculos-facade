package com.egamorim.mgz.auction.api;

import com.egamorim.mgz.auction.api.request.VehicleRequest;
import com.egamorim.mgz.auction.api.response.VehicleResponse;
import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import org.mapstruct.Mapper;

@Mapper
public interface VehicleRequestMapper {

    VehicleDTO vehicleRequestToDTO(VehicleRequest request);
    VehicleResponse vehicleRequestToDTO(VehicleDTO dto);
}
