package com.dlabs.practise.reactive.playground;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {
	
	@Test
	public void coldPublisherTest(){
		Flux<String> stringFlux = Flux.just("A", "B","C","D", "E","F")
				.delayElements(Duration.ofSeconds(1));
		
		stringFlux.subscribe(s -> System.out.println("S1: " + s));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stringFlux.subscribe(s -> System.out.println("S2: " + s));

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void hotPublisherTest(){
		Flux<String> stringFlux = Flux.just("A", "B","C","D", "E","F")
				.delayElements(Duration.ofSeconds(1));
		

		ConnectableFlux<String> connectableFlux = stringFlux.publish();
		connectableFlux.connect();
		
		connectableFlux.subscribe(s -> System.out.println("S1: " + s));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectableFlux.subscribe(s -> System.out.println("S2: " + s));

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
