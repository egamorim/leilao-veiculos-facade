package com.egamorim.mgz.auction.vehicle;

import com.egamorim.mgz.auction.api.response.VehicleResultPage;
import com.egamorim.mgz.auction.commons.AuctionException;
import com.egamorim.mgz.auction.commons.ResourceNotFoundException;
import com.egamorim.mgz.auction.legacy.LegacyService;
import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VehicleService {

    private final LegacyService legacyService;

    public VehicleService(LegacyService legacyService) {
        this.legacyService = legacyService;
    }

    public VehicleDTO create(VehicleDTO vehicleDTO) {
        return this.legacyService.create(vehicleDTO).orElseThrow(AuctionException::new);
    }

    public VehicleDTO update(VehicleDTO vehicleDTO) {
        return this.legacyService.update(vehicleDTO).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
    }

    public void delete(Long id) {
        this.legacyService.delete(id);
    }

    public Optional<VehicleDTO> find(Long id) {
        return this.legacyService.getAll(null)
            .stream()
            .filter(i -> i.getId().equals(id))
            .findAny();
    }

    public VehicleResultPage getAll(int limit, int offset, String sort) {
        List<VehicleDTO> all = this.legacyService.getAll(sort);
        return this.returnPage(all, limit, offset);
    }

    public VehicleResultPage findByLot(String lot, int limit, int offset, String sort) {
        List<VehicleDTO> all = this.legacyService.getAll(sort).stream()
            .filter(v -> v.getLot().getName().equalsIgnoreCase(lot))
            .collect(Collectors.toList());
        return this.returnPage(all, limit, offset);
    }

    public VehicleResultPage findByManufacturer(String manufacturer, int limit, int offset, String sort) {
        List<VehicleDTO> all = this.legacyService.getAll(sort).stream()
            .filter(v -> v.getManufacturer().getName().equalsIgnoreCase(manufacturer))
            .collect(Collectors.toList());
        return this.returnPage(all, limit, offset);
    }

    public VehicleResultPage findByModelStartsWith(String startWith, int limit, int offset, String sort) {
        List<VehicleDTO> all = this.legacyService.getAll(sort).stream()
            .filter(v -> {
                return v.getModel()!= null && v.getModel().getName() != null && v.getModel().getName().toLowerCase().startsWith(startWith.toLowerCase());
            })
            .collect(Collectors.toList());
        return this.returnPage(all, limit, offset);
    }

    public VehicleResultPage findByManufactureYearAndModelYear(Integer manufactureYear, Integer modelYear, int limit, int offset, String sort) {
        List<VehicleDTO> all = this.legacyService.getAll(sort).stream()
            .filter(v -> {
                return v.getModel() != null
                    && v.getModel().getYear() != null
                    && v.getModel().getYear().equals(modelYear)
                    && v.getManufactureYear() != null
                    && v.getManufactureYear().equals(manufactureYear);
            })
            .collect(Collectors.toList());
        return this.returnPage(all, limit, offset);
    }

    public VehicleResultPage findByManufacturePeriod(Integer yearFrom, Integer yearTo, int limit, int offset, String sort) {
        List<VehicleDTO> all = this.legacyService.getAll(sort).stream()
            .filter(v -> {
                return v.getManufactureYear() != null
                    && isBetween(v.getManufactureYear(), yearFrom, yearTo);
            })
            .collect(Collectors.toList());
        return this.returnPage(all, limit, offset);
    }

    private VehicleResultPage returnPage(List<VehicleDTO> vehicles, int limit, int offset) {
        int max = offset + limit;
        boolean hasNext = true;
        boolean hasPrev = false;

        if(offset >= vehicles.size()) {
            offset = vehicles.size() - 1;
        }

        if(vehicles.size() <= max) {
            max = vehicles.size();
            hasNext = false;
        }

        if((offset - limit) > 0) {
            hasPrev = true;
        }

        List<VehicleDTO> paginated = vehicles.subList(offset, max);
        return VehicleResultPage
            .builder()
            .vehicles(paginated)
            .next(hasNext)
            .nextOffset(max)
            .previous(hasPrev)
            .previousOffset(hasPrev? offset - limit : 0)
            .build();
    }

    public void checkDuplicatedLotControlCode(String controlCode) {
        List<VehicleDTO> filteredResult =
            legacyService
                .getAll(null).stream().filter(v -> v.getLot().getControlCode().equals(controlCode))
                .collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(filteredResult)) {
            throw new AuctionException("Duplicated control code.");
        }
    }

    boolean isBetween(int value, int minValueInclusive, int maxValueInclusive) {
        return value >= minValueInclusive && value <= maxValueInclusive;
    }
}
