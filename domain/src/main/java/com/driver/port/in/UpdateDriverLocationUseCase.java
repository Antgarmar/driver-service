package com.driver.port.in;

import com.driver.model.UpdateDriverLocationCommand;

public interface UpdateDriverLocationUseCase {

    void execute(UpdateDriverLocationCommand command);
}

