package com.driver;

import com.driver.model.DriverLocationEvent;

public interface DriverProducer {
     void sendDriverLocation(DriverLocationEvent event) ;
}
