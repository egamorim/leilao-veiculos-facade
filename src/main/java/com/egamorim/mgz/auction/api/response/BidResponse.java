package com.egamorim.mgz.auction.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidResponse {

    @JsonFormat(pattern="dd/MM/yyyy - HH:mm")
    private LocalDateTime date;
    private BigDecimal value;
    private UserResponse user;
}
