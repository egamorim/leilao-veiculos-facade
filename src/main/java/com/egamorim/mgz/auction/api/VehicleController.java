package com.egamorim.mgz.auction.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.egamorim.mgz.auction.api.request.VehicleRequest;
import com.egamorim.mgz.auction.api.response.ErrorResponse;
import com.egamorim.mgz.auction.api.response.VehicleCollectionResponse;
import com.egamorim.mgz.auction.api.response.VehicleResponse;
import com.egamorim.mgz.auction.api.response.VehicleResultPage;
import com.egamorim.mgz.auction.commons.AuctionException;
import com.egamorim.mgz.auction.commons.ResourceNotFoundException;
import com.egamorim.mgz.auction.vehicle.VehicleService;
import com.egamorim.mgz.auction.vehicle.dto.VehicleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/vehicle")
@Api(value = "Vehicle auction")
public class VehicleController {

    @Value("${auction.search.page.limit:3}")
    private Integer pageLimit;
    private final VehicleService service;
    private final VehicleRequestMapper mapper;

    public VehicleController(VehicleService service, VehicleRequestMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Create a new vehicle")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 201, message = "Created", response = VehicleResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VehicleResponse create(@RequestBody @Valid VehicleRequest vehicle) throws NoSuchMethodException {
        this.service.checkDuplicatedLotControlCode(vehicle.getLot().getControlCode());
        VehicleDTO vehicleDTO = this.service.create(mapper.vehicleRequestToDTO(vehicle));
        VehicleResponse response = mapper.vehicleRequestToDTO(vehicleDTO);

        Method method = VehicleController.class.getMethod("get", Long.class);
        response.add(linkTo(method, response.getId()).withSelfRel());

        return response;
    }

    @ApiOperation(value = "Update a vehicle")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleResponse.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    public VehicleResponse update(@RequestBody @Valid VehicleRequest vehicle, @PathVariable(value = "id") Long id) throws NoSuchMethodException {
        this.service.checkDuplicatedLotControlCode(vehicle.getLot().getControlCode());
        vehicle.setId(id);
        VehicleDTO vehicleDTO = this.service.update(mapper.vehicleRequestToDTO(vehicle));
        VehicleResponse response = mapper.vehicleRequestToDTO(vehicleDTO);

        Method method = VehicleController.class.getMethod("get", Long.class);
        response.add(linkTo(method, response.getId()).withSelfRel());

        return response;
    }

    @ApiOperation(value = "Get a vehicle by its id")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleResponse.class)
    })
    @GetMapping(value = "/{id}")
    public VehicleResponse get(@PathVariable Long id) throws NoSuchMethodException {
        VehicleResponse response = this.service.find(id)
            .map(mapper::vehicleRequestToDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

        Method method = VehicleController.class.getMethod("get", Long.class);
        response.add(linkTo(method, response.getId()).withSelfRel());
        return response;
    }

    @ApiOperation(value = "Get all vehicles. The result is sorted and the page size is fixed.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleCollectionResponse.class)
    })
    @GetMapping
    public VehicleCollectionResponse getAll(
        @RequestParam(value = "offset", defaultValue = "0") Integer offset,
        @RequestParam(value = "sort", defaultValue = "desc") String sort
    ){

        VehicleResultPage page = this.service.getAll(pageLimit, offset, sort);
        List<VehicleResponse> all = page.getVehicles()
            .stream().map(i -> mapper.vehicleRequestToDTO(i))
            .collect(Collectors.toList());

        VehicleCollectionResponse vehicles = VehicleCollectionResponse.builder()
            .vehicles(all)
            .build();

        if(page.isNext()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .getAll(page.getNextOffset(), sort)
                ).withRel(IanaLinkRelations.NEXT));
        }
        if(page.isPrevious()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .getAll(page.getPreviousOffset(), sort)
                ).withRel(IanaLinkRelations.PREVIOUS));
        }

        return vehicles;
    }

    @ApiOperation(value = "Delete a vehicle by its id")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 204, message = "No content")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }


    @ApiOperation(value = "Get all vehicles from a specific lot. The result is sorted and the page size is fixed.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleCollectionResponse.class)
    })
    @GetMapping(value = "/lot/{lot}")
    public VehicleCollectionResponse findByLot(
        @PathVariable String lot,
        @RequestParam(value = "offset", defaultValue = "0") Integer offset,
        @RequestParam(value = "sort", defaultValue = "desc") String sort){

        VehicleResultPage page = this.service.findByLot(lot, pageLimit, offset, sort);
        List<VehicleResponse> all =
            page.getVehicles()
                .stream()
                .map(i -> mapper.vehicleRequestToDTO(i))
            .collect(Collectors.toList());

        VehicleCollectionResponse vehicles = VehicleCollectionResponse.builder()
            .vehicles(all)
            .build();

        if(page.isNext()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .findByLot(lot, page.getNextOffset(), sort)
                ).withRel(IanaLinkRelations.NEXT));
        }

        if(page.isPrevious()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .findByLot(lot, page.getPreviousOffset(), sort)
                ).withRel(IanaLinkRelations.PREVIOUS));
        }
        return vehicles;
    }

    @ApiOperation(value = "Get all vehicles from a specific manufacturer. The result is sorted and the page size is fixed.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleCollectionResponse.class)
    })
    @GetMapping(value = "/manufacturer/{manufacturer}")
    public VehicleCollectionResponse findByManufacturer(
        @PathVariable String manufacturer,
        @RequestParam(value = "offset", defaultValue = "0") Integer offset,
        @RequestParam(value = "sort", defaultValue = "desc") String sort
        ){

        VehicleResultPage page = this.service.findByManufacturer(manufacturer, pageLimit, offset, sort);
        List<VehicleResponse> all =
            page.getVehicles()
                .stream()
                .map(i -> mapper.vehicleRequestToDTO(i))
                .collect(Collectors.toList());

        VehicleCollectionResponse vehicles = VehicleCollectionResponse.builder()
            .vehicles(all)
            .build();

        if(page.isNext()) {
            vehicles.add(
                linkTo(methodOn(VehicleController.class)
                    .findByManufacturer(manufacturer, page.getNextOffset(), sort)
                ).withRel(IanaLinkRelations.NEXT));
        }

        if(page.isPrevious()) {
            vehicles.add(
                linkTo(methodOn(VehicleController.class)
                    .findByManufacturer(manufacturer, page.getPreviousOffset(), sort)
                ).withRel(IanaLinkRelations.PREVIOUS));
        }
        return vehicles;
    }

    @ApiOperation(value = "Get all vehicles which the name of its model starts with a provided string. The result is sorted and the page size is fixed.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleCollectionResponse.class)
    })
    @GetMapping(value = "/model")
    public VehicleCollectionResponse findByModelStartsWith(
        @RequestParam String startsWith,
        @RequestParam(value = "offset", defaultValue = "0") Integer offset,
        @RequestParam(value = "sort", defaultValue = "desc") String sort
        ){

        VehicleResultPage page = this.service.findByModelStartsWith(startsWith, pageLimit, offset, sort);
        List<VehicleResponse> all =
                page.getVehicles()
                .stream()
                .map(i -> mapper.vehicleRequestToDTO(i))
                .collect(Collectors.toList());

        VehicleCollectionResponse vehicles = VehicleCollectionResponse.builder()
            .vehicles(all)
            .build();

        if(page.isNext()) {
            vehicles.add(
                linkTo(methodOn(VehicleController.class)
                    .findByModelStartsWith(startsWith, page.getNextOffset(), sort))
                .withRel(IanaLinkRelations.NEXT));
        }

        if(page.isPrevious()) {
            vehicles.add(
                linkTo(methodOn(VehicleController.class)
                    .findByModelStartsWith(startsWith, page.getPreviousOffset(), sort))
                    .withRel(IanaLinkRelations.PREVIOUS));
        }
        return vehicles;
    }

    @ApiOperation(value = "Get all vehicles by a specific manufacture year and model year. The result is sorted and the page size is fixed.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleCollectionResponse.class)
    })
    @GetMapping(value = "/manufacture/{manufactureYear}/model/{modelYear}")
    public VehicleCollectionResponse findByManufactureYearAndModelYear(
        @PathVariable Integer manufactureYear,
        @PathVariable Integer modelYear,
        @RequestParam(value = "offset", defaultValue = "0") Integer offset,
        @RequestParam(value = "sort", defaultValue = "desc") String sort){

        VehicleResultPage page = this.service
            .findByManufactureYearAndModelYear(manufactureYear, modelYear, pageLimit, offset, sort);
        List<VehicleResponse> all =
                page.getVehicles()
                .stream()
                .map(i -> mapper.vehicleRequestToDTO(i))
                .collect(Collectors.toList());

        VehicleCollectionResponse vehicles = VehicleCollectionResponse.builder()
            .vehicles(all)
            .build();

        if(page.isNext()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .findByManufactureYearAndModelYear(manufactureYear, modelYear, page.getNextOffset(), sort)
                ).withRel(IanaLinkRelations.NEXT));
        }

        if(page.isPrevious()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .findByManufactureYearAndModelYear(manufactureYear, modelYear, page.getPreviousOffset(), sort)
                ).withRel(IanaLinkRelations.PREVIOUS));
        }
        return vehicles;
    }

    @ApiOperation(value = "Get all vehicles by a specific manufacture period. The result is sorted and the page size is fixed.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 200, message = "Ok", response = VehicleCollectionResponse.class)
    })
    @GetMapping(value = "/manufacture")
    public VehicleCollectionResponse findByManufactureYearPeriod(
        @RequestParam Integer yearFrom,
        @RequestParam Integer yearTo,
        @RequestParam(value = "offset", defaultValue = "0") Integer offset,
        @RequestParam(value = "sort", defaultValue = "desc") String sort){

        VehicleResultPage page = this.service
            .findByManufacturePeriod(yearFrom, yearTo, pageLimit, offset, sort);

        List<VehicleResponse> all =
                page.getVehicles()
                .stream()
                .map(i -> mapper.vehicleRequestToDTO(i))
                .collect(Collectors.toList());

        VehicleCollectionResponse vehicles = VehicleCollectionResponse.builder()
            .vehicles(all)
            .build();

        if(page.isNext()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .findByManufactureYearPeriod(yearFrom, yearTo, page.getNextOffset(), sort)
                ).withRel(IanaLinkRelations.NEXT));
        }

        if(page.isPrevious()) {
            vehicles.add(
                linkTo(
                    methodOn(VehicleController.class)
                        .findByManufactureYearPeriod(yearFrom, yearTo, page.getPreviousOffset(), sort)
                ).withRel(IanaLinkRelations.PREVIOUS));
        }
        return vehicles;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity handleNotFoundException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorMessage(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuctionException.class})
    public ResponseEntity handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorMessage(ex.getMessage()).errorMessage(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
