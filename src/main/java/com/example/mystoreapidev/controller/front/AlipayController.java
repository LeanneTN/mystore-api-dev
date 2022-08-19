package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.VO.QRCodeVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.MyAlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/pay/")
public class AlipayController {

    @Autowired
    private MyAlipayService myAlipayService;

    @GetMapping("get_qr_code")
    public CommonResponse<QRCodeVO> getQRCode(
          @RequestParam @NotNull(message = "order no can't be empty") Long orderNo,
          HttpSession session
    ){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return myAlipayService.getQRCode(login.getId(), orderNo);
    }
}
