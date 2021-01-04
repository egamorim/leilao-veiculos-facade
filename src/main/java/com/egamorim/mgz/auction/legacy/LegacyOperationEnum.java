package com.egamorim.mgz.auction.legacy;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LegacyOperationEnum {

    CREATE("criar"),
    GET("consultar"),
    UPDATE("alterar"),
    DELETE("apagar");

    String command;

    LegacyOperationEnum(String command) {
        this.command = command;
    }

    @JsonValue
    public String getCommand() {
        return command;
    }
}
