package com.example.mystoreapidev.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.example.mystoreapidev.VO.QRCodeVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.MyAlipayService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping("/pay/")
@Slf4j
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

    @PostMapping("callback")
    public Object callback(HttpServletRequest request){
        Map<String, String[]> requestParams = request.getParameterMap();

        Map<String, String> params = Maps.newHashMap();
        //Map<String, String[]> -> Map<String, String>
        for(String paramName: requestParams.keySet()){
            String [] values = requestParams.get(paramName);
            String paramValue = "";
            for(int i = 0; i < values.length; i++){
                paramValue = (i < values.length-1) ? (values[i] + ","):(paramValue + values[i]);
            }
            params.put(paramName, paramValue);
        }

        params.remove("sign_type");
        try {
            boolean alipayRSACheck = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(), "utf-8", Configs.getSignType());
            if(!alipayRSACheck){
                return CommonResponse.createForError("invalid request");
            }
        }catch (AlipayApiException e){
            log.error("alipay signature check error",e);
        }

        CommonResponse result = myAlipayService.alipayCallback(params);

        if(result.isSuccess())
            return CONSTANT.AlipayCallbackResponse.RESPONSE_SUCCESS;
        return CONSTANT.AlipayCallbackResponse.RESPONSE_FAILED;
    }
}
