package com.egamorim.mgz.auction.commons;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
