package com.egamorim.mgz.auction.vehicle;

import com.egamorim.mgz.auction.legacy.VehicleLegacyDTO;
import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface VehicleMapper {

    @Mappings({
        @Mapping(source = "bid.date", target = "dataLance"),
        @Mapping(source = "lot.name", target = "lote"),
        @Mapping(source = "lot.controlCode", target = "codigoControle"),
        @Mapping(source = "manufacturer.name", target = "marca"),
        @Mapping(source = "model.name", target = "modelo"),
        @Mapping(source = "manufactureYear", target = "anoFabricacao"),
        @Mapping(source = "model.year", target = "anoModelo"),
        @Mapping(source = "bid.value", target = "valorLance"),
        @Mapping(source = "bid.user.name", target = "usuarioLance")
    })
    VehicleLegacyDTO vehicleDTOToVehicleLegacyDTO(VehicleDTO vehicleDTO);

    @Mappings({
        @Mapping(target = "bid.date", source = "dataLance"),
        @Mapping(target = "lot.name", source = "lote"),
        @Mapping(target = "lot.controlCode", source = "codigoControle"),
        @Mapping(target = "manufacturer.name", source = "marca"),
        @Mapping(target = "model.name", source = "modelo"),
        @Mapping(target = "manufactureYear", source = "anoFabricacao"),
        @Mapping(target = "model.year", source = "anoModelo"),
        @Mapping(target = "bid.value", source = "valorLance"),
        @Mapping(target = "bid.user.name", source = "usuarioLance")
    })
    VehicleDTO vehicleDTOToVehicleLegacyDTO(VehicleLegacyDTO vehicleDTO);
}
