package org.omnera.picbloom.util.constants;

public enum Authority {

    READ,
    WRITE,
    UPDATE,
    USER, //can update delete self objects,read anything
    ADMIN ////can update delete any objects,read anything
    
}
