package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.mystoreapidev.VO.AddressVO;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Address;
import com.example.mystoreapidev.persistence.AddressMapper;
import com.example.mystoreapidev.service.AddressService;
import com.example.mystoreapidev.utils.DateTimeFormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public CommonResponse<Address> add(Integer userId, Address address){
        if(userId==null){
            return CommonResponse.createForError("user id can't be null");
        }
        if (address == null){
            return CommonResponse.createForError("address entity can't be null");
        }
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
        if(userId==null){
            return CommonResponse.createForError("user id can't be null");
        }
        if (addressId == null){
            return CommonResponse.createForError("address id can't be null");
        }
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("id", addressId);

        int result = addressMapper.delete(queryWrapper);
        if(result!=1){
            return CommonResponse.createForError("delete address failed");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<Address> update(Integer userId, Address address){
        if(userId==null){
            return CommonResponse.createForError("user id can't be null");
        }
        if (address == null){
            return CommonResponse.createForError("address entity can't be null");
        }
        UpdateWrapper<Address> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId).eq("id", address.getId());
        int result = addressMapper.update(address, updateWrapper);

        if(result != 1){
            return CommonResponse.createForError("update address failed");
        }
        return CommonResponse.createForSuccess(address);
    }

    @Override
    public CommonResponse<Address> findById(Integer userId, Integer addressId){
        if(userId==null){
            return CommonResponse.createForError("user id can't be null");
        }
        if (addressId == null){
            return CommonResponse.createForError("address id can't be null");
        }
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("id", addressId);
        Address address = addressMapper.selectOne(queryWrapper);
        if(address == null){
            return CommonResponse.createForError("get address information failed");
        }
        return CommonResponse.createForSuccess(address);
    }

    @Override
    public CommonResponse<List<Address>> findAll(Integer userId){
        if(userId==null){
            return CommonResponse.createForError("user id can't be null");
        }
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Address> addressList = addressMapper.selectList(queryWrapper);
        return CommonResponse.createForSuccess(addressList);
    }

    @Override
    public AddressVO addressToAddressVO(Address address){
        AddressVO addressVO = new AddressVO();
        addressVO.setId(address.getId());
        addressVO.setUserId(address.getUserId());
        addressVO.setAddressName(address.getAddressName());
        addressVO.setAddressPhone(address.getAddressPhone());
        addressVO.setAddressMobile(address.getAddressMobile());
        addressVO.setAddressProvince(address.getAddressProvince());
        addressVO.setAddressCity(address.getAddressCity());
        addressVO.setAddressDistrict(address.getAddressDistrict());
        addressVO.setAddressDetail(address.getAddressDetail());
        addressVO.setAddressZip(address.getAddressZip());
        addressVO.setCreateTime(DateTimeFormatterUtil.format(address.getCreateTime()));
        addressVO.setUpdateTime(DateTimeFormatterUtil.format(address.getUpdateTime()));
        return addressVO;
    }
}
