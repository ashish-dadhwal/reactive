package com.dlabs.practise.reactive.playground;

import org.junit.Test;
import org.junit.jupiter.api.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

	@Test
	@Order(1)
	public void monoTest() {
		Mono<String> stringMono = Mono.just("Spring");

		StepVerifier.create(stringMono.log())
		.expectNext("Spring").verifyComplete();

	}

	@Test
	public void monoTest_WithError() {
		Mono<String> stringMono = Mono.just("Spring");

		StepVerifier.create(Mono.error(new RuntimeException("RE")).log())
		.expectError(RuntimeException.class)
				.verify();

	}

}
