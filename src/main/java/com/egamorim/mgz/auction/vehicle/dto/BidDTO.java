package com.egamorim.mgz.auction.vehicle.dto;

import com.egamorim.mgz.auction.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BidDTO {

    @JsonFormat(pattern="dd/MM/yyyy - HH:mm")
    private LocalDateTime date;
    private BigDecimal value;
    private UserDTO user;
}
