package com.dlabs.practise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.dlabs.practise.dto.Item;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ItemController {

	WebClient webClient = WebClient.create("http://localhost:8080");
	
	@GetMapping("/client/retrieve/items")
	public Flux<Item> getAllItemsRetrieve(){
		
		return webClient.get().uri("/items/v1")
		.retrieve()
		.bodyToFlux(Item.class)
		.log("Retrieve");
	}
	
	@GetMapping("/client/exchange/items")
	public Flux<Item> getAllItemsExchange(){
		
		return webClient.get().uri("/items/v1")
		.exchange()
		.flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
		.log("Exchange");
	}
	
	
	@GetMapping("/client/retrieve/item")
	public Mono<Item> getItemRetrieve(){
		
		String id = "4";
		return webClient.get().uri("/items/v1/{id}",id)
				.retrieve()
				.bodyToMono(Item.class)
				.log("Retrieve 1");
	}
	
	@GetMapping("/client/exchange/item")
	public Mono<Item> getItemExchange(){

		String id = "4";
		return webClient.get().uri("/items/v1/{id}",id)
		.exchange()
		.flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
		.log("Exchange 1");
	}

	@GetMapping("/client/retrieve/items/error")
	public Flux<Item> getAllItemsRetrieveError(){
		
		return webClient.get().uri("/items/v1/runtimeException")
				.retrieve()
				.onStatus(HttpStatus::is5xxServerError, response -> {
					Mono<String> errorMono = response.bodyToMono(String.class);
					return errorMono.flatMap(errorMessage -> {
						log.error("Error Message Retrieve: " + errorMessage);
						throw new RuntimeException(errorMessage);
					});
				})
				.bodyToFlux(Item.class)
				.log("Retrieve Exception");
	}
	
	@GetMapping("/client/exchange/items/error")
	public Flux<Item> getAllItemsExchangeError(){
		
		return webClient.get().uri("/items/v1/runtimeException")
				.exchange()
				.flatMapMany((clientResponse) ->{
					if(clientResponse.statusCode().is5xxServerError()){
						
						return clientResponse.bodyToMono(String.class)
								.flatMap(errorMessage -> {
									log.error("Error Message Exchange: " + errorMessage);
									throw new RuntimeException(errorMessage);
								});
					}else {
						return clientResponse.bodyToFlux(Item.class);
					}
						
				});
	}
	
	
	@PostMapping("/client/create")
	public Mono<Item> createItem(@RequestBody Item item){
		
		Mono<Item> monoItem = Mono.just(item);
		return webClient.post().uri("/items/v1")
		.contentType(MediaType.APPLICATION_JSON)
		.body(monoItem, Item.class)
		.retrieve()
		.bodyToMono(Item.class)
		.log("Created");
		
		
	}
	
	
}
