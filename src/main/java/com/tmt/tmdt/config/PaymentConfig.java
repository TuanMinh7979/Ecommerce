package com.tmt.tmdt.config;

public class PaymentConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    //on production
    public static String vnp_Returnurl = "https://tmt-ecommerce.herokuapp.com/payment/vnpay-checkout-result";


//    public static String vnp_Returnurl = "http://localhost:5000/payment/vnpay-checkout-result";
    public static String vnp_TmnCode = "UFWSKFT5";
    public static String vnp_HashSecret = "NNNHHXBFYQZTYWLLTYIKMZZHRTOBYQFM";
    public static String vnp_apiUrl = "http://sandbox.vnpayment.vn/merchant_webapi/merchant.html";




}
