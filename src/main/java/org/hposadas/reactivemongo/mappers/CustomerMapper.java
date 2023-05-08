package org.hposadas.reactivemongo.mappers;

import org.hposadas.reactivemongo.domain.Customer;
import org.hposadas.reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDto(Customer customer);
}
