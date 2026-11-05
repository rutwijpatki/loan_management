package com.fintech.loan.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRegistry{
    private final Map<String, Customer> customersByPan = new  HashMap<>();

    public void register(Customer customer){
        if(customersByPan.containsKey(customer.getPan())){
            throw new IllegalArgumentException("Customer already exists with pan: " + customer.getPan());
        }
        customersByPan.put(customer.getPan(), customer);
    }
    public Customer findByPan(String pan){
        return customersByPan.get(pan);
    }
    public List<Customer> findAll(){
        return new ArrayList<>(customersByPan.values());
    }
    public int count(){
        return customersByPan.size();
    }
}