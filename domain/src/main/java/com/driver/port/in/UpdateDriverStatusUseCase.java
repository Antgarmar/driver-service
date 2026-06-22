package com.driver.port.in;

import com.driver.model.UpdateDriverStatusCommand;

public interface UpdateDriverStatusUseCase {
    void execute(UpdateDriverStatusCommand command);
}
