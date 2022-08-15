package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Address;
import com.example.mystoreapidev.persistence.AddressMapper;
import com.example.mystoreapidev.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public CommonResponse<Address> add(Integer userId, Address address){
        address.setUserId(userId);
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());

        int result = addressMapper.insert(address);
        if(result != 1){
            return CommonResponse.createForError("add new address failed");
        }
        return CommonResponse.createForSuccess(address); //id has been put back into address by mybatis plus
    }

    @Override
    public CommonResponse<Object> delete(Integer userId, Integer addressId){
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("id", addressId);

        int result = addressMapper.delete(queryWrapper);
        if(result!=1){
            return CommonResponse.createForError("delete address failed");
        }
        return CommonResponse.createForSuccess();
    }
}
