package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.AddressVO;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Address;

import java.util.List;

public interface AddressService {
    CommonResponse<Address> add(Integer userId, Address address);

    CommonResponse<Object> delete(Integer userId, Integer addressId);

    CommonResponse<Address> update(Integer userId, Address address);

    CommonResponse<Address> findById(Integer userId, Integer addressId);

    CommonResponse<List<Address>> findAll(Integer userId);

    AddressVO addressToAddressVO(Address address);
}
