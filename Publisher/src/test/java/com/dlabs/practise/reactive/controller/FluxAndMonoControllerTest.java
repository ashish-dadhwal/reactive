package com.dlabs.practise.reactive.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class FluxAndMonoControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Test
	public void flux_approach1() {
		
		Flux<Integer> flux = webTestClient.get().uri("/flux")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.returnResult(Integer.class)
		.getResponseBody();
		
		StepVerifier.create(flux)
		.expectSubscription()
		.expectNext(1,2,3,4)
		.verifyComplete();
		
	}
	
	
	@Test
	public void fluxStream() {
		
		Flux<Long> flux = webTestClient.get().uri("/flux/stream")
		.accept(MediaType.APPLICATION_STREAM_JSON)
		.exchange()
		.expectStatus().isOk()
		.returnResult(Long.class)
		.getResponseBody();
		
		StepVerifier.create(flux)
		.expectSubscription()
		.expectNext(0L,1L,2L,3L,4L)
		.thenCancel()
		.verify();
		
	}
	
	@Test
	public void mono() {
		
		Integer expValue = new Integer(1);
		webTestClient.get().uri("/mono")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Integer.class)
				.consumeWith(response -> {
					assertEquals(expValue, response.getResponseBody());
				});

		
		
	}
}
