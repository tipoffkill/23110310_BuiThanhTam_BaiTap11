package Demo1.controller;

import Demo1.model.Customer; // Thay thế bằng package model của bạn
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@EnableMethodSecurity
public class CustomerController {

    final private List<Customer> customers = List.of(
            Customer.builder().id("001").name("Nguyễn Hữu Trung").email("trungnhspkt@gmail.com").build(),
            Customer.builder().id("002").name("Hữu Trung").email("trunghuu@gmail.com").build()
    );

   
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("hello is Guest");
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is Guest");
    }

    
    @GetMapping("/customer/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Customer>> getCustomerList(){
        return ResponseEntity.ok(this.customers);
    }

    
    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id) {
        // Tìm khách hàng theo ID trong danh sách có sẵn
        List<Customer> customers = this.customers.stream().filter(customer ->
                customer.getId().equals(id)).toList();
        return ResponseEntity.ok(customers.get(0));
    }
}