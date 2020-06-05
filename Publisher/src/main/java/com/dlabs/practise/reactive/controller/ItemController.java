package com.dlabs.practise.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dlabs.practise.reactive.model.Item;
import com.dlabs.practise.reactive.repo.ItemReactiveRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ItemController {
	


	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	@GetMapping(value="/items/v1")
	public Flux<Item> getAllItems(){
		
		return itemReactiveRepository.findAll();
		
		
	}
	
	@GetMapping(value="/items/v1"+"/{id}")
	public Mono<ResponseEntity<Item>> getItem(@PathVariable String id){
		
		return itemReactiveRepository.findById(id)
				.map((item) -> new ResponseEntity<>(item,HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
	}
	
	@GetMapping(value="/items/v1/runtimeException")
	public Flux<Item> runTimeException(){
		return itemReactiveRepository.findAll()
				.concatWith(Mono.error(new RuntimeException("Custom Exception")));
	}

	
	@DeleteMapping(value="/items/v1"+"/{id}")
	public Mono<Void> deleteItem(@PathVariable String id){
		
		return itemReactiveRepository.deleteById(id);
		
	}
	
	@PostMapping(value="/items/v1")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Item> createItem(@RequestBody Item item){
		
		return itemReactiveRepository.save(item);
	}
	
	@PutMapping(value="/items/v1"+"/{id}")
	public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, 
												@RequestBody Item item){
		
		return itemReactiveRepository.findById(id)
				.flatMap((currentItem) -> {
					currentItem.setPrice(item.getPrice());
					currentItem.setDescription(item.getDescription());
					
					return itemReactiveRepository.save(currentItem);
				})
				.map(updatedItem -> new ResponseEntity<>(updatedItem,HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}
	
}
