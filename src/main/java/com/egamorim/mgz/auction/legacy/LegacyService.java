package com.egamorim.mgz.auction.legacy;

import com.egamorim.mgz.auction.commons.AuctionException;
import com.egamorim.mgz.auction.vehicle.VehicleMapper;
import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import static  java.util.Comparator.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LegacyService {

    private static final String SUCCESS_MESSAGE = "sucesso";

    private final LegacyGateway gateway;
    private final VehicleMapper vehicleMapper;

    public LegacyService(LegacyGateway gateway, VehicleMapper vehicleMapper) {
        this.gateway = gateway;
        this.vehicleMapper = vehicleMapper;
    }

    public Optional<VehicleDTO> create(VehicleDTO vehicle) {

        VehicleLegacyDTO response = this.callLegacyAPI(vehicle, LegacyOperationEnum.CREATE);
        if(response != null && !StringUtils.isEmpty(response.getMensagem())) {
            return Optional.empty();
        }
        return Optional.of(vehicleMapper.vehicleDTOToVehicleLegacyDTO(response));
    }

    public Optional<VehicleDTO> update(VehicleDTO vehicle) {

        VehicleLegacyDTO response = this.callLegacyAPI(vehicle, LegacyOperationEnum.UPDATE);
        if(response != null && !StringUtils.isEmpty(response.getMensagem())) {
            return Optional.empty();
        }
        return Optional.of(vehicleMapper.vehicleDTOToVehicleLegacyDTO(response));
    }

    public List<VehicleDTO> getAll(String sort) {
        VehicleLegacyDTORequest request = VehicleLegacyDTORequest.builder()
            .operation(LegacyOperationEnum.GET)
            .build();

        List<VehicleLegacyDTO> vehicles = gateway.callLegacyAPIGet(request);
        this.sortResult(vehicles, sort);
        return vehicles.stream()
            .map(i -> vehicleMapper.vehicleDTOToVehicleLegacyDTO(i))
            .collect(Collectors.toList());
    }

    public void delete(Long id) {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setId(id);
        VehicleLegacyDTO response = this.callLegacyAPI(vehicle, LegacyOperationEnum.DELETE);

        if(!SUCCESS_MESSAGE.equalsIgnoreCase(response.getMensagem())) {
            throw new AuctionException("Delete operation got an error. Message: " + response.getMensagem());
        }
    }

    private VehicleLegacyDTO callLegacyAPI(VehicleDTO vehicleDTO, LegacyOperationEnum operation) {
        VehicleLegacyDTORequest request = VehicleLegacyDTORequest.builder()
            .operation(operation)
            .vehicle(this.vehicleMapper.vehicleDTOToVehicleLegacyDTO(vehicleDTO))
            .build();
        return this.gateway.callLegacyAPI(request);

    }

    private void sortResult(List<VehicleLegacyDTO> vehicles, String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            vehicles.sort(comparing(VehicleLegacyDTO::getDataLance).reversed());
            return;
        }
        vehicles.sort(comparing(VehicleLegacyDTO::getDataLance));
    }

}
