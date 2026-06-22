package com.driver.port.out;

import com.driver.model.DriverLocationEvent;
import com.driver.model.DriverStatusChangedEvent;

public interface DriverProducer {
     void sendDriverLocation(DriverLocationEvent event);
     void sendDriverStatusChanged(DriverStatusChangedEvent event);
}
