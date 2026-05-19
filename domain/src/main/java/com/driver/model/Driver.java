package com.driver.model;

import com.driver.enums.DriverStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Driver {

    private String id;
    private String name;
    private String carModel;
    private String licensePlate;
    private DriverStatus status;
    private Location location;

}