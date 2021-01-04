package com.egamorim.mgz.auction.legacy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLegacyDTO {

    @JsonProperty("ID")
    private Long id;
    @JsonProperty("DATALANCE")
    @JsonFormat(pattern="dd/MM/yyyy - HH:mm")
    private LocalDateTime dataLance;
    @JsonProperty("LOTE")
    private String lote;
    @JsonProperty("CODIGOCONTROLE")
    private String codigoControle;
    @JsonProperty("MARCA")
    private String marca;
    @JsonProperty("MODELO")
    private String modelo;
    @JsonProperty("ANOFABRICACAO")
    private Integer anoFabricacao;
    @JsonProperty("ANOMODELO")
    private Integer anoModelo;
    @JsonProperty("VALORLANCE")
    private BigDecimal valorLance;
    @JsonProperty("USUARIOLANCE")
    private String usuarioLance;
    private String mensagem;

}
