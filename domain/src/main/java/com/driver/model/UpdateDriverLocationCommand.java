package com.driver.model;

public record UpdateDriverLocationCommand(String driverId, Location location, long timestamp) {
}
