package com.egamorim.mgz.auction.vehicle.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.egamorim.mgz.auction.legacy.VehicleLegacyDTO;
import com.egamorim.mgz.auction.vehicle.VehicleMapper;
import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class VehicleMapperTest {

    @Test
    void vehicleDTOToLegacyDTO_Success() throws JsonProcessingException {

        EasyRandom easyRandom = new EasyRandom();
        VehicleMapper mapper = Mappers.getMapper(VehicleMapper.class);

        VehicleDTO vehicleDTO = easyRandom.nextObject(VehicleDTO.class);
        VehicleLegacyDTO vehicleLegacyDTORequest = mapper.vehicleDTOToVehicleLegacyDTO(vehicleDTO);

        assertThat(vehicleLegacyDTORequest.getId()).isEqualTo(vehicleDTO.getId());
        assertThat(vehicleLegacyDTORequest.getDataLance()).isEqualTo(vehicleDTO.getBid().getDate());
        assertThat(vehicleLegacyDTORequest.getValorLance()).isEqualTo(vehicleDTO.getBid().getValue());
        assertThat(vehicleLegacyDTORequest.getUsuarioLance()).isEqualTo(vehicleDTO.getBid().getUser().getName());
        assertThat(vehicleLegacyDTORequest.getLote()).isEqualTo(vehicleDTO.getLot().getName());
        assertThat(vehicleLegacyDTORequest.getMarca()).isEqualTo(vehicleDTO.getManufacturer().getName());
        assertThat(vehicleLegacyDTORequest.getModelo()).isEqualTo(vehicleDTO.getModel().getName());
        assertThat(vehicleLegacyDTORequest.getAnoModelo()).isEqualTo(vehicleDTO.getModel().getYear());
        assertThat(vehicleLegacyDTORequest.getAnoFabricacao()).isEqualTo(vehicleDTO.getManufactureYear());
    }
}
