package com.driver.adapter.in.rest;

import com.driver.model.Location;
import com.driver.model.UpdateDriverLocationCommand;
import com.driver.port.UpdateDriverLocationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class DriverController {

	private final UpdateDriverLocationUseCase updateDriverLocationUseCase;

	@PostMapping("/{driverId}/location")
	public ResponseEntity<Void> updateLocation(@PathVariable String driverId,
			@Valid @RequestBody UpdateDriverLocationRequest request) {
		this.updateDriverLocationUseCase.execute(new UpdateDriverLocationCommand(driverId,
				new Location(request.lat(), request.lng()), System.currentTimeMillis()));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/location")
	public ResponseEntity<String> get() {
		return ResponseEntity.ok().body("Hola");
	}

}
