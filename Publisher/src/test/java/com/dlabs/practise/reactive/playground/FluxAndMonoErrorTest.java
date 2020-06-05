package com.dlabs.practise.reactive.playground;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

	@Test
	public void fluxErrorHandling() {
		
		Flux<String> stringFlux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("My Exception")))
				.concatWith(Flux.just("D"))
				.onErrorResume((e) -> {
					System.out.println("Exception is " + e);
					return Flux.just("Default", "Default Next");
				});
		
		StepVerifier.create(stringFlux.log())
		.expectSubscription()
		.expectNext("A","B","C")
		//.expectError(RuntimeException.class)
		//.verify();
		.expectNext("Default", "Default Next")
		.verifyComplete();
	}
	
	
	@Test
	public void fluxErrorHandling_OnErrorReturn() {
		
		Flux<String> stringFlux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("My Exception")))
				.concatWith(Flux.just("D"))
				.onErrorReturn("Default");
		
		StepVerifier.create(stringFlux.log())
		.expectSubscription()
		.expectNext("A","B","C")
		.expectNext("Default")
		.verifyComplete();
	}
	
	@Test
	public void fluxErrorHandling_OnErrorMap() {
		
		Flux<String> stringFlux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("My Exception")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e));
		
		StepVerifier.create(stringFlux.log())
		.expectSubscription()
		.expectNext("A","B","C")
		.expectError(CustomException.class)
		.verify();
	}
	
	@Test
	public void fluxErrorHandling_OnErrorMap_Retry() {
		
		Flux<String> stringFlux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("My Exception")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e))
				.retry(2);
		
		StepVerifier.create(stringFlux.log())
		.expectSubscription()
		.expectNext("A","B","C")
		.expectNext("A","B","C")
		.expectNext("A","B","C")
		.expectError(CustomException.class)
		.verify();
	}
	

}
