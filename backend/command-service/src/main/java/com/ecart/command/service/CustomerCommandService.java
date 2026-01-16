package com.ecart.command.service;

import com.ecart.command.entity.Customer;
import com.ecart.command.repository.CustomerRepository;
import com.ecart.shared.dto.CustomerDTO;
import com.ecart.shared.events.CustomerCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerCommandService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setCreatedAt(System.currentTimeMillis());
        
        Customer saved = customerRepository.save(customer);
        
        // Publish event to Kafka
        CustomerCreatedEvent event = new CustomerCreatedEvent(
            saved.getId(), saved.getName(), saved.getEmail(), saved.getCreatedAt()
        );
        kafkaTemplate.send("customer-events", event);
        
        dto.setId(saved.getId());
        return dto;
    }
    
    public CustomerDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        return convertToDTO(customer);
    }
    
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        Customer updated = customerRepository.save(customer);
        return convertToDTO(updated);
    }
    
    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail());
    }
}
