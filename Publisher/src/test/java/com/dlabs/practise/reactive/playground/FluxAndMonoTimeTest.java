package com.dlabs.practise.reactive.playground;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoTimeTest {

	@Test
	public void fluxWithInfiniteSequence() {

		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)).log();

		infiniteFlux.subscribe((element) -> System.out.println("Value is: " + element));

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void fluxWithInfiniteSequenceTest() {

		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200)).delayElements(Duration.ofSeconds(1))
				.map(l -> new Integer(l.intValue())).take(3).log();

		StepVerifier.create(finiteFlux).expectSubscription().expectNext(0, 1, 2).verifyComplete();

	}

}
