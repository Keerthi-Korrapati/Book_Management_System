package com.capg.bms.entity;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="BookData")
public class BookData {
	
	@Transient
    public static final  String SEQUENCE_NAME="user_sequence";
	
	@Id
	private int id;
	
	@NotNull
	private String title;
	@NotNull
	private String author;
	@NotNull
	private String imageUrl;
	@NotNull
	private int price;
	@NotNull
	private boolean featured;
	
}
