package com.dlabs.practise.reactive.playground;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoFactoryTest {

	List<String> names = Arrays.asList("Ashish", "Isha", "Siddy", "Nans");
	
	@Test
	public void fluxUsingIterable() {
		
		Flux namesFlux = Flux.fromIterable(names).log();
		
		StepVerifier.create(namesFlux)
		.expectNext("Ashish", "Isha", "Siddy", "Nans")
		.verifyComplete();
		
	}
	
	@Test
	public void fluxUsingArray() {
		
		String[] names2 = new String[] {"Ashish", "Isha", "Siddy", "Nans"};	
		Flux namesFlux = Flux.fromArray(names2).log();
		
		StepVerifier.create(namesFlux)
		.expectNext("Ashish", "Isha", "Siddy", "Nans")
		.verifyComplete();
		
	}
	
	@Test
	public void fluxUsingStream() {
		
		Flux namesFlux = Flux.fromStream(names.stream()).log();
		
		StepVerifier.create(namesFlux)
		.expectNext("Ashish", "Isha", "Siddy", "Nans")
		.verifyComplete();
		
	}
	
	@Test
	public void monoUsingJustOrEmpty() {
		Mono<String> mono = Mono.justOrEmpty(null);
		StepVerifier.create(mono.log())
		.verifyComplete();
	}
	
	@Test
	public void monoUsingSupplier() {
		
		Supplier<String> stringSupplier = () -> "Ashish"; 
		
		Mono<String> mono = Mono.fromSupplier(stringSupplier);
		StepVerifier.create(mono.log())
		.expectNext("Ashish")
		.verifyComplete();
	}
}
