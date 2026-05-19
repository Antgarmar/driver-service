package com.driver.port;

import com.driver.model.UpdateDriverLocationCommand;

public interface UpdateDriverLocationUseCase {

    void execute(UpdateDriverLocationCommand command);
}

