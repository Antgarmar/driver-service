package com.driver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverLocationEvent {

    private String driverId;
    private double latitude;
    private double longitude;
    private long timestamp;
}
