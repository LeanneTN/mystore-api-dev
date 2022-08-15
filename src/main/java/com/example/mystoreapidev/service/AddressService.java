package com.example.mystoreapidev.service;

import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Address;

public interface AddressService {
    CommonResponse<Address> add(Integer userId, Address address);

    CommonResponse<Object> delete(Integer userId, Integer addressId);
}
