package com.egamorim.mgz.auction.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private String errorMessage;
}
