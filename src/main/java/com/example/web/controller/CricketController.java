package com.example.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.service.CricketerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Cricketer;
import com.example.repository.CricketerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class CricketController {
	@Autowired
	CricketerService cricketerService;
	
	@Autowired
	CricketerRepository cricketerRepository;
	
	@GetMapping("/api/cricketer/{id}")
	public ResponseEntity<?> getCricketer(@PathVariable("id") Long id) {
		Cricketer cricketer = cricketerRepository.findById(id).orElse(null);


		if (cricketer == null) {
			// Jika data tidak ditemukan, kirim respons 404 NOT FOUND
			ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND.value(), "Cricketer not found.","");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			// Jika data ditemukan, kirim respons dengan data cricketer
			Map<String, Object> response = new HashMap<>();
			// Add data to the Map (optional)
			response.put("data", cricketer);
			response.put("status", 200);
			response.put("message", "Success");

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}



	}

	@GetMapping("/api/cricketers/")
	public ResponseEntity<?> getAllCricketers() {
		ArrayList<Cricketer> cricketersList = (ArrayList<Cricketer>) cricketerService.getAllPlayers();

		// Initialize ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

			if (cricketersList == null || cricketersList.isEmpty()) {
				String message = "Data is empty.";
//				String jsonResponse = objectMapper.writeValueAsString(new Message(message));
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);

			} else {
				// Jika data ditemukan, kirim respons dengan data cricketer
				Map<String, Object> response = new HashMap<>();
				// Add data to the Map (optional)
				response.put("data", cricketersList);
				response.put("status", 200);
				response.put("message", "Success");

				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

	}


	@PostMapping("/api/cricketer/")
	@CacheEvict(value = "cricketers", allEntries=true)
	public ResponseEntity<?> addCricketer(@RequestBody Cricketer cricketer) {
		System.out.print(cricketer);
		Cricketer cCricketer = new Cricketer();
		cCricketer.setCountry(cricketer.getCountry());
		cCricketer.setName(cricketer.getName());
		cCricketer.setHighestScore(cricketer.getHighestScore());

		List<Cricketer> existingCricketers = cricketerRepository.findByName(cricketer.getName());

		if (!existingCricketers.isEmpty()) {
			Map<String, Object> response = new HashMap<>();

			response.put("status", 404);
			response.put("message", "Already name");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			cricketerRepository.save(cCricketer);
			return new ResponseEntity<Cricketer>(cricketer, HttpStatus.OK);
		}


	}
	
	@PutMapping("/api/cricketer/{id}")
	@CacheEvict(value = "cricketers", allEntries=true)
	public ResponseEntity<Cricketer> updateCricketer(@PathVariable("id") Long id, @RequestBody Cricketer cricketer) {
		Cricketer cCricketer = cricketerService.findById(id);
		cCricketer.setCountry(cricketer.getCountry());
		cCricketer.setName(cricketer.getName());
		cCricketer.setHighestScore(cricketer.getHighestScore());
		cricketerRepository.save(cCricketer);
		return new ResponseEntity<Cricketer>(cricketer, HttpStatus.OK);
	}

	@CacheEvict(value = "cricketers", allEntries=true)
	@DeleteMapping("/api/cricketer/{id}")
	public ResponseEntity<String> deleteCricketer(@PathVariable("id") Long id) {
		Cricketer cCricketer = cricketerService.findById(id);
		cricketerRepository.delete(cCricketer);
		return new ResponseEntity<String>("cricketer removed", HttpStatus.OK);
	}
}
