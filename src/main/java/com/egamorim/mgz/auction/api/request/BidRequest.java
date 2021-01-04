package com.egamorim.mgz.auction.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidRequest {

    @NotNull(message = "Bid date is required.")
    @JsonFormat(pattern="dd/MM/yyyy - HH:mm")
    private LocalDateTime date;

    @NotNull(message = "Bid value is required.")
    private BigDecimal value;

    @Valid
    @NotNull(message = "Bid user is required.")
    private UserRequest user;
}
