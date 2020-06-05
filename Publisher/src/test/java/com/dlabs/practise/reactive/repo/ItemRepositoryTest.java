package com.dlabs.practise.reactive.repo;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlabs.practise.reactive.model.Item;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemRepositoryTest {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	List<Item> itemList = Arrays.asList(
			new Item("1", "Soap", 40.0),
			new Item("2", "Shampoo", 40.0),
			new Item("3", "Detergent", 40.0),
			new Item("4", "Brush", 40.0));
	
	@Before
	public void setup() {
		itemReactiveRepository.deleteAll()
		.thenMany(Flux.fromIterable(itemList))
		.flatMap(itemReactiveRepository::save)
		.doOnNext((item) -> {
			System.out.println("Inserted Item: " + item);
		}).blockLast();
		
	}
	
	@Test
	public void findAll() {
		StepVerifier.create(itemReactiveRepository.findAll())
		.expectSubscription()
		.expectNextCount(4)
		.verifyComplete();
	}
	
	@Test
	public void findById() {
		StepVerifier.create(itemReactiveRepository.findById("4"))
		.expectSubscription()
		.expectNextMatches(item -> item.getDescription().equals("Brush"));

	}
	
	@Test
	public void findByDescription() {
		StepVerifier.create(itemReactiveRepository.findByDescription("Brush").log("findByDescription : "))
		.expectSubscription()
		.expectNextMatches(item -> item.getId().equals("4"));

	}
	
	
}
