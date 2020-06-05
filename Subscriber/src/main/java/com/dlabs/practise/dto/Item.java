package com.dlabs.practise.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
public class Item {

	private String id;
	private String description;
	private Double price;
}
