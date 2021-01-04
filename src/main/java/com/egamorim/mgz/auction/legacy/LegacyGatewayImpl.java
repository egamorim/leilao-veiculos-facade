package com.egamorim.mgz.auction.legacy;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LegacyGatewayImpl implements LegacyGateway{

    private final String legacyUrl;

    private final  RestTemplate restTemplate;

    public LegacyGatewayImpl(@Value("${auction.legacy.url}") String legacyUrl, RestTemplate restTemplate) {
        this.legacyUrl = legacyUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public VehicleLegacyDTO callLegacyAPI(VehicleLegacyDTORequest vehicle) {
        VehicleLegacyDTO response = restTemplate.postForObject(legacyUrl, vehicle, VehicleLegacyDTO.class);
        return response;
    }

    @Override
    public List<VehicleLegacyDTO> callLegacyAPIGet(VehicleLegacyDTORequest vehicle) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(vehicle,headers);
        ResponseEntity<List<VehicleLegacyDTO>> exchange = restTemplate
            .exchange(legacyUrl, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<VehicleLegacyDTO>>() {
                });
        return exchange.getBody();
    }
}
