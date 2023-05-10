package org.hposadas.reactivemongo.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.reactivemongo.mappers.CustomerMapper;
import org.hposadas.reactivemongo.model.CustomerDTO;
import org.hposadas.reactivemongo.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    //atributos
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO) {
        return customerRepository
                .save(customerMapper.customerDtoToCustomer(customerDTO))
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO) {
        return customerDTO.map(customerDTO1 -> customerMapper.customerDtoToCustomer(customerDTO1))
                .flatMap(customer -> customerRepository.save(customer))
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(customerfound -> {
                    customerfound.setCustomerName(customerDTO.getCustomerName());
                    return customerfound;
                })
                .flatMap(customer -> customerRepository.save(customer))
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    if(StringUtils.hasText(customerDTO.getCustomerName())){
                        foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    }
                    return  foundCustomer;
                })
                .flatMap(customer -> customerRepository.save(customer))
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public Mono<Void> deleteCustomer(String customerId) {
        return customerRepository.deleteById(customerId);
    }

    @Override
    public Mono<CustomerDTO> findFirstByCustomerName(String customerName) {
        return customerRepository.findFirstByCustomerName(customerName)
                .map(customer -> customerMapper.customerToCustomerDto(customer));
    }
}
