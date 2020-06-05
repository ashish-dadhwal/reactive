package com.dlabs.practise.reactive.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.dlabs.practise.reactive.model.Item;

import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item,String>{
	
	Flux<Item> findByDescription(String descpription);

}
