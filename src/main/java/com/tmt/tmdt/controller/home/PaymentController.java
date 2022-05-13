package com.tmt.tmdt.controller.home;

import com.tmt.tmdt.config.PaymentConfig;
import com.tmt.tmdt.util.PaymentHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
public class PaymentController {
    @PostMapping("/payment/redirectToBankForm")
    public String createQueryUrl(HttpServletRequest request) {
        String vnp_Version = "2.0.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = PaymentHelper.getRandomNumber(8);
        String vnp_IpAddr = request.getRemoteAddr();
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", "3000000");
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", "NCB");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "abc info");
        vnp_Params.put("vnp_OrderType", "other");


        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(dt);
        String vnp_CreateDate = dateString;

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);


        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(fieldValue);
                // Build query
                try {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentHelper.Sha256(PaymentConfig.vnp_HashSecret + hashData.toString());
        // System.out.println("HashData=" + hashData.toString());
        queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
        vnp_Params.put("redirect_url", paymentUrl);
        System.out.println("_________________________"+paymentUrl);
        return "redirect:" + paymentUrl;
//        return vnp_Params;
    }

    @GetMapping("/payment/showPmBtn")
    public String showPmbutton() {
        return "home/payment/showPmBtn";
    }


//    public String redirectToBankForm(HttpServletRequest request) throws UnsupportedEncodingException {
//        //create order
//        Integer amount = 1806000;
////        String ipAdd = request.getRemoteAddr();
////        log.info("ip :" + ipAdd);
//
//        String vnp_Version = "2.1.0";
//
//        String vnp_Command = "pay";
//
//        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;
//
//        String vnp_Amount = String.valueOf(amount);
//
//        String vnp_BankCode = "NCB";
//
////        LocalDateTime time = LocalDateTime.now();
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
////        String vnp_CreateDate = time.format(formatter);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//
//
//        String vnp_CurrCode = "VND";
//
//        String vnp_IpAddr = "127.0.0.1";
//
//        String vnp_Locale = "vn";
//
//        String vnp_OrderInfo = "Mo ta don hang 123";
//
//        String vnp_OrderType = "150000";
//
//        String vnp_ReturnUrl = "http://localhost:5000/payment/returnRs";
//
//        String vnp_TxnRef = "6";
//
//
//        Map<String, String> params = new HashMap<>();
//
//        params.put("vnp_Version", "2.1.0");
//        params.put("vnp_Command", "pay");
//        params.put("vnp_TmnCode", vnp_TmnCode);
//        params.put("vnp_Amount", "1806000");
////        params.put("vnp_BankCode", vnp_BankCode);
//        params.put("vnp_CreateDate", "20210801153333");
//        params.put("vnp_CurrCode", "VND");
//        params.put("vnp_IpAddr", "127.0.0.1");
//        params.put("vnp_Locale", "vn");
//        params.put("vnp_OrderInfo", vnp_OrderInfo);
//        params.put("vnp_OrderType", "other");
//        params.put("vnp_ReturnUrl", vnp_ReturnUrl);
//        params.put("vnp_TxnRef", "5");
//
//
//        List fieldNames = new ArrayList(params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(fieldValue);
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//
//
//        String vnp_SecureHash = PaymentHelper.hmacSHA512(PaymentConfig.vnp_HashSecret, hashData.toString());
//        log.info("Hash " + vnp_SecureHash);
//        queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
//
//        String paymentUrl = PaymentConfig.vnp_PayUrl + '?' + queryUrl;
//        log.info("Paymenr url: " + paymentUrl);
//
//        return "redirect:" + paymentUrl;
//
//
////        return "redirect:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=1806000&vnp_Command=pay&vnp_CreateDate=20210801153333&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang+%3A5&vnp_OrderType=other&vnp_ReturnUrl=https%3A%2F%2Fdomainmerchant.vn%2FReturnUrl&vnp_TmnCode=DEMOV210&vnp_TxnRef=5&vnp_Version=2.1.0&vnp_SecureHash=3e0d61a0c0534b2e36680b3f7277743e8784cc4e1d68fa7d276e79c23be7d6318d338b477910a27992f5057bb1582bd44bd82ae8009ffaf6d141219218625c42";
//
//    }


    @GetMapping("/payment/returnRS")
    public String returnPage(Model model,
                             HttpServletRequest request) {

        return "home/payment/rspm";
    }
}







