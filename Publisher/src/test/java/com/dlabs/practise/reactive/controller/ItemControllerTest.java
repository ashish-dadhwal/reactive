package com.dlabs.practise.reactive.controller;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.dlabs.practise.reactive.model.Item;
import com.dlabs.practise.reactive.repo.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ItemControllerTest {

	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	ItemReactiveRepository itemReactiveRepo;
	
	public List<Item> data(){
		List<Item> itemList = Arrays.asList(
				new Item("1", "Soap", 40.0),
				new Item("2", "Shampoo", 40.0),
				new Item("3", "Detergent", 40.0),
				new Item("4", "Brush", 40.0));
		
		return itemList;
	}
	
	@Before
	public void setup() {
		itemReactiveRepo.deleteAll()
		.thenMany(Flux.fromIterable(data()))
		.flatMap(itemReactiveRepo::save)
		.doOnNext(item ->
		{
			System.out.println("Item inserted: " + item);
		})
		.blockLast();
		
	}
	
	@Test
	public void getAllItems() {
		webTestClient.get().uri("/items/v1")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Item.class)
		.hasSize(4);
	}
	
	@Test
	public void getAllItems2() {
		webTestClient.get().uri("/items/v1")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Item.class)
		.hasSize(4)
		.consumeWith((resp) -> {
			List<Item> items = resp.getResponseBody();
			items.forEach((item) -> {
				assertTrue(item.getId()!=null);
			});
		});
	}
	
	@Test
	public void getAllItems3() {
		Flux<Item> itemFlux = webTestClient.get().uri("/items/v1")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.returnResult(Item.class)
		.getResponseBody();
		
		StepVerifier.create(itemFlux.log())
		.expectNextCount(4)
		.verifyComplete();

	}
	
	@Test
	public void getItem() {
		webTestClient.get().uri("/items/v1".concat("/{id}"),"1")
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.price", 40.0);
	}
	
	@Test
	public void getItemNotFound() {
		webTestClient.get().uri("/items/v1".concat("/{id}"),"11")
		.exchange()
		.expectStatus().isNotFound();
	}
	
	@Test
	public void createItem() {
		
		Item item = new Item(null,"Daal", 102.0);
		webTestClient.post().uri("/items/v1")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(item),Item.class)
		.exchange()
		.expectStatus().isCreated()
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.description").isEqualTo("Daal")
		.jsonPath("$.price", 102.0);
	}
	
	
	@Test
	public void deleteItem() {
		webTestClient.delete().uri("/items/v1".concat("/{id}"),"1")
		.exchange()
		.expectStatus().isOk()
		.expectBody(Void.class);
	}
	
	@Test
	public void updateItem() {
		Double newPrice = 42.0;
		Item item = new Item("1", "Soap", newPrice);
		webTestClient.put().uri("/items/v1".concat("/{id}"),"1")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(item),Item.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.price", newPrice);
	}
	
	@Test
	public void updateItemNotFound() {
		Double newPrice = 42.0;
		Item item = new Item("11", "Soap", newPrice);
		webTestClient.put().uri("/items/v1".concat("/{id}"),"11")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(item),Item.class)
		.exchange()
		.expectStatus().isNotFound();
	}
	
	@Test
	public void runtimeException() {
		webTestClient.get().uri("/items/v1/runtimeException")
		.exchange()
		.expectStatus().is5xxServerError()
		.expectBody(String.class)
		.isEqualTo("Custom Exception");

		
	}
	
	
	
	
	
}
