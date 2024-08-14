package com.example.demo.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name must not be empty")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Price must not be null")

    private double price;

    @NotNull(message = "Quantity must not be null")

    private int quantity;
    private String location;

}

