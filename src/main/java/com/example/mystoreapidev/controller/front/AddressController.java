package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.Address;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/address/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("add")
    public CommonResponse<Address> add(@RequestBody @Valid Address address, HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.add(login.getId(), address);
    }

    @PostMapping("delete")
    public CommonResponse<Object> delete(@RequestParam @NotNull(message = "address id can't be empty") Integer addressId,
                                         HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.delete(login.getId(), addressId);
    }

    @PostMapping("update")
    public CommonResponse<Address> update(@RequestBody @Valid Address address, HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.update(login.getId(), address);
    }

    @GetMapping("find")
    public CommonResponse<Address> findById(@RequestParam @NotNull(message = "address id can't be null") Integer addressId,
                                            HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.findById(login.getId(), addressId);
    }

    @GetMapping("list")
    public CommonResponse<List<Address>> findAll(HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.findAll(login.getId());
    }
}
