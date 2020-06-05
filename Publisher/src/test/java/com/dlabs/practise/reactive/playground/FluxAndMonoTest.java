package com.dlabs.practise.reactive.playground;

import org.junit.Test;
import org.junit.jupiter.api.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
	
	@Test
	public void fluxTest() {
	
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				//.concatWith(Flux.error(new RuntimeException("My Exception")))
				.concatWith(Flux.just("After error"))
				.log();
		
		stringFlux
		.subscribe(System.out::println,
				(e) -> System.err.println(e),
				() -> System.out.println("Completed") );
		
	}
	
	
	@Test
	public void fluxTestElements_WithoutError() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNext("Spring")
		.expectNext("Spring Boot")
		.expectNext("Reactive Spring")
		.verifyComplete();
		
	}
	
	
	@Test
	public void fluxTestElements_WithError() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("My Exception")))
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNext("Spring")
		.expectNext("Spring Boot")
		.expectNext("Reactive Spring")
		//.expectError(RuntimeException.class)
		.expectErrorMessage("My Exception")
		.verify();
		
	}
	
	@Test
	public void fluxTestElementCount_withError() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("My Exception")))
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNextCount(3)
		.expectErrorMessage("My Exception")
		.verify();
		
	}
	
	@Test
	public void fluxTestElements_WithError2() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("My Exception 2")))
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNext("Spring", "Spring Boot", "Reactive Spring")
		//.expectError(RuntimeException.class)
		.expectErrorMessage("My Exception 2")
		.verify();
		
	}

}
