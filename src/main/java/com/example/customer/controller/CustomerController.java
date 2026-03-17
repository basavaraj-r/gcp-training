package com.example.customer.controller;

import com.example.customer.dto.CustomerRequest;
import com.example.customer.entity.Customer;
import com.example.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequest request) {
        Customer saved = service.createCustomer(request);
        return ResponseEntity.created(URI.create("/customers/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @GetMapping("/slow")
    public ResponseEntity<List<Customer>> getCustomersSlow() {
        return ResponseEntity.ok(service.getCustomersWithArtificialDelay());
    }

    @GetMapping("/veryslow")
    public ResponseEntity<List<Customer>> getCustomersVerySlow() {
        return ResponseEntity.ok(service.getCustomersVerySlow());
    }

    @GetMapping("/healthz")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }
}