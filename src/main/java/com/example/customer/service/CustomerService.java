package com.example.customer.service;

import com.example.customer.dto.CustomerRequest;
import com.example.customer.entity.Customer;
import com.example.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Customer createCustomer(CustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail());
        return repository.save(customer);
    }

    public Customer getCustomer(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    /**
     * Training purpose:
     * Simulates a slow DB-backed path during peak traffic.
     */
    public List<Customer> getCustomersWithArtificialDelay() {
        try {
            int delay = ThreadLocalRandom.current().nextInt(200, 1200);
            log.info("Applying artificial delay of {} ms before DB call", delay);
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return repository.findAll();
    }

    /**
     * Extra slow endpoint used during incident drill.
     */
    public List<Customer> getCustomersVerySlow() {
        try {
            int delay = ThreadLocalRandom.current().nextInt(3000, 8000);
            log.warn("Simulated severe latency path triggered, sleeping {} ms", delay);
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return repository.findAll();
    }
}