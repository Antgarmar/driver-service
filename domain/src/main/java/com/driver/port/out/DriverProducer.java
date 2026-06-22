package com.driver.port.out;

import com.driver.model.DriverLocationEvent;

public interface DriverProducer {
     void sendDriverLocation(DriverLocationEvent event) ;
}
