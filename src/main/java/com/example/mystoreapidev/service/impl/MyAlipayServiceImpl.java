package com.example.mystoreapidev.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mystoreapidev.VO.QRCodeVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Order;
import com.example.mystoreapidev.domain.OrderItem;
import com.example.mystoreapidev.domain.PayInfo;
import com.example.mystoreapidev.persistence.OrderItemMapper;
import com.example.mystoreapidev.persistence.OrderMapper;
import com.example.mystoreapidev.persistence.PayInfoMapper;
import com.example.mystoreapidev.service.MyAlipayService;
import com.example.mystoreapidev.utils.DateTimeFormatterUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Service("myAlipayService")
@Slf4j
public class MyAlipayServiceImpl implements MyAlipayService {

    //todo: rewrite ali pay interface without using third party packages

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Override
    public CommonResponse<QRCodeVO> getQRCode(Integer userId, Long orderNo){

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId).eq("order_no", orderNo);

        Order order = orderMapper.selectOne(orderQueryWrapper);
        if(order == null){
            return CommonResponse.createForError("order is not exists");
        }

        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.eq("user_id", userId).eq("order_no", orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);

        String outTradeNo = orderNo.toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "My Store Mall, order No: " + orderNo;

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPaymentPrice().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "total price is : " + order.getPaymentPrice().toString() + "yuan";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        //todo: put orderItemList into goodsDetailList

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl("https://5714a17e18.zicp.fun/pay/callback")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        Configs.init("zfbinfo.properties");
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);

        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
//                String filePath = String.format("/Users/sudo/Desktop/qr-%s.png",
//                        response.getOutTradeNo());
//                log.info("filePath:" + filePath);
                //                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                String qrCodeBase64 = getQRImageInBase64(response.getQrCode());
                if(qrCodeBase64==null){
                    return CommonResponse.createForError("QR code generate failed");
                }

                QRCodeVO codeVO = new QRCodeVO();
                codeVO.setOrderNo(orderNo);
                codeVO.setQrCodeBase64(qrCodeBase64);

                return CommonResponse.createForSuccess(codeVO);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return CommonResponse.createForError("Alipay trade pre-create failed");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return CommonResponse.createForError("System error, status of pre-create trade is unknown");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return CommonResponse.createForError("not a supported trade state");
        }
    }

    @Override
    public CommonResponse<Object> alipayCallback(Map<String, String> params){
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(queryWrapper);

        if(order.getStatus() >= CONSTANT.OrderStatus.PAID.getCode()){
            return CommonResponse.createForSuccess();
        }

        if(tradeStatus.equals(CONSTANT.AlipayTradeStatus.TRADE_SUCCESS)){
            order.setStatus(CONSTANT.OrderStatus.PAID.getCode());
            order.setPaymentTime(DateTimeFormatterUtil.parseGMT(params.get("gmt_payment")));
            orderMapper.updateById(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(orderNo);
        payInfo.setPaymentType(CONSTANT.PayType.ALIPAY.getCode());
        payInfo.setTradeNo(tradeNo);
        payInfo.setTradeStatus(tradeStatus);
        payInfo.setCreateTime(LocalDateTime.now());
        payInfo.setUpdateTime(LocalDateTime.now());
        payInfoMapper.insert(payInfo);
        return CommonResponse.createForSuccess();
    }

    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    private String getQRImageInBase64(String QRImageCode){
        String base64Image = "";
        try{
            Map<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(QRImageCode, BarcodeFormat.QR_CODE, 256, 256, hints);
            BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);

            for(int x = 0; x < 256; x++){
                for (int y = 0; y < 256; y++){
                    image.setRGB(x, y, bitMatrix.get(x, y)? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            //change image into a byte stream and put it into os
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);

            //change byte stream into base64 code
            base64Image = new String(new Base64().encode(os.toByteArray()));
            return base64Image;
        }catch (Exception e){
            log.error("create QR image error", e);
        }
        return null;
    }
}
