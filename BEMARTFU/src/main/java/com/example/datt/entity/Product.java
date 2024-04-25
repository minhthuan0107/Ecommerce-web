package com.example.datt.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String name;
	private int quantity;
	private Double price;
	private int discount;
	private String image;
	private String description;
	private LocalDate enteredDate;
	private Boolean status;
	private int sold;

	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", quantity=" + quantity + ", price=" + price
				+ ", discount=" + discount + ", image=" + image + ", description=" + description + ", enteredDate="
				+ enteredDate + ", status=" + status + ", sold=" + sold + ", category=" + category + "]";
	}

}
