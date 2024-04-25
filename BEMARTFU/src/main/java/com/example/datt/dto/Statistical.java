package com.example.datt.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistical {

	private int month;
	private Date date;
	private Double amount;
	private int count;

}
