package com.dlabs.practise.reactive.initialize;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.dlabs.practise.reactive.model.Item;
import com.dlabs.practise.reactive.repo.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@Component
@Profile("local")
public class ItemDataInitializer implements CommandLineRunner {

	@Autowired
	ItemReactiveRepository itemReactiveRepo;
	
	@Override
	public void run(String... args) throws Exception {
		initializeItemData();
	}
	
	public List<Item> data(){
		List<Item> itemList = Arrays.asList(
				new Item("1", "Soap", 40.0),
				new Item("2", "Shampoo", 40.0),
				new Item("3", "Detergent", 40.0),
				new Item("4", "Brush", 40.0));
		
		return itemList;
	}

	private void initializeItemData() {
		itemReactiveRepo.deleteAll()
		.thenMany(Flux.fromIterable(data()))
		.flatMap(itemReactiveRepo::save)
		.thenMany(itemReactiveRepo.findAll())
		.subscribe(item ->
		{
			System.out.println("Item inserted" + item);
		});
	}

}
