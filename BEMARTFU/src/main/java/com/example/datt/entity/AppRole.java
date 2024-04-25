
package com.example.datt.entity;
import com.example.datt.enumn.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appRoles")
public class AppRole implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	public ERole name;

	@SpringBootApplication
	public static class DattApplication {

		public static void main(String[] args) {
			SpringApplication.run(DattApplication.class, args);
		}

	}
}
