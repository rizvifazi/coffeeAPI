package com.thehecklers.sbur_rest_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SburRestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SburRestDemoApplication.class, args);
	}

}

class Coffee {
    private String id;
    private String name;

    // 1. Default Constructor (Required for JSON/Jackson)
    public Coffee() {
        this.id = UUID.randomUUID().toString();
    }

    // 2. Name-only Constructor (Fixes your compilation errors)
    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    // 3. Full Constructor (For manual ID setting)
    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters...
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {
	private List<Coffee> coffees = new ArrayList<>();

	public RestApiDemoController() {
		coffees.addAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")));
	}

	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffees;
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		for (Coffee c : coffees) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}

		return Optional.empty();
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		Coffee coffeeToSave = (coffee.getId() == null) 
			? new Coffee(coffee.getName()) 
			: coffee;
		coffees.add(coffeeToSave);
		return coffeeToSave;
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		for (Coffee c : coffees) {
			if (c.getId().equals(id)) {
				c.setName(coffee.getName());
				return new ResponseEntity<>(c, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffees.removeIf(c -> c.getId().equals(id));
	}
}