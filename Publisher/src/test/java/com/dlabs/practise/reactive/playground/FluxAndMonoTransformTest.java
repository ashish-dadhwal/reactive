package com.dlabs.practise.reactive.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformTest {
	List<String> names = Arrays.asList("Ashish", "Isha", "Siddy", "Nans");

	
	@Test
	public void transformUsingMap() {
		
		Flux<String> namesFlux = Flux.fromIterable(names)
				.map(s -> s.toUpperCase())
				.log();
		
		StepVerifier.create(namesFlux)
		.expectNext("ASHISH", "ISHA", "SIDDY", "NANS")
		.verifyComplete();
		
	}
	
	@Test
	public void transformUsingMap_Length() {
		
		Flux<Integer> namesFlux = Flux.fromIterable(names)
				.map(s -> s.length())
				.log();
		
		StepVerifier.create(namesFlux)
		.expectNext(6,4,5,4)
		.verifyComplete();
		
	}
	
	
	@Test
	public void transformUsingMap_Length_Repeat() {
		
		Flux<Integer> namesFlux = Flux.fromIterable(names)
				.map(s -> s.length())
				.repeat(1)
				.log();
		
		StepVerifier.create(namesFlux)
		.expectNext(6,4,5,4,6,4,5,4)
		.verifyComplete();
		
	}
	
	@Test
	public void transformUsingFlatMap() {
		
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
				.flatMap(s -> {
					return Flux.fromIterable(convertToList(s));
				}).log();
				
		StepVerifier.create(stringFlux)
		.expectNextCount(12)
		.verifyComplete();
		
	}

	private List<String> convertToList(String s) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Arrays.asList(s,"newValue");
	}
	
	
	@Test
	public void transformUsingFlatMap_UsingParallel() {
		
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
				.window(2)
				.flatMap((s) -> 
				s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
				.flatMap(s -> Flux.fromIterable(s))
				.log();
				
				
		StepVerifier.create(stringFlux)
		.expectNextCount(12)
		.verifyComplete();
		
	}
	
	@Test
	public void transformUsingFlatMap_UsingParallel_order() {
		
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
				.window(2)
				.flatMapSequential((s) -> 
				s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
				.flatMap(s -> Flux.fromIterable(s))
				.log();
				
				
		StepVerifier.create(stringFlux)
		.expectNextCount(12)
		.verifyComplete();
		
	}

}
