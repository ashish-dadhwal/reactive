package com.dlabs.practise.reactive.handler;

import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dlabs.practise.reactive.repo.ItemReactiveRepository;

public class ItemsHandler {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	public RouterFunction<ServerResponse> itemsRoute(ItemHandler itemHandler){
		return null;
	}
}
