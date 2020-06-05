package com.dlabs.practise.reactive.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoFilterTest {
	
	
	List<String> names = Arrays.asList("Ashish", "Isha", "Siddy", "Nans");

	
	@Test
	public void filterTest() {
		Flux<String> stringFlux = Flux.fromIterable(names)
				.filter(s -> s.contains("sh"))
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNext("Ashish", "Isha")
		.verifyComplete();
		
	}

}
