package com.driver.adapter.in.rest;

import jakarta.validation.constraints.NotNull;

public record UpdateDriverLocationRequest(
        @NotNull Double lat,
        @NotNull Double lng
) {
}

