package com.tmt.tmdt.controller.home;

import com.tmt.tmdt.config.PaymentConfig;
import com.tmt.tmdt.constant.TransactionStatus;
import com.tmt.tmdt.entities.Transaction;
import com.tmt.tmdt.service.TransactionService;
import com.tmt.tmdt.util.PaymentHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Controller
public class PaymentController {
    @Autowired
    private TransactionService transactionService;


    @PostMapping("/payment/redirect-vnpay-checkout")
    @ResponseBody
    public String redirectVnPayCheckout(HttpServletRequest req) throws UnsupportedEncodingException {
        int price = Integer.valueOf(req.getParameter("price"));
        int tranId = Integer.valueOf(req.getParameter("tranId"));
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Info";
        String vnp_TxnRef = PaymentHelper.getRandomNumber(5) + tranId;
        String vnp_IpAddr = PaymentHelper.getIpAddress(req);
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        int amount = price;
        Map vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", "topup");

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

//
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentHelper.hmacSHA512(PaymentConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
        System.out.println(paymentUrl);
        return paymentUrl;
//        return vnp_Params;
    }


    @GetMapping("/payment/vnpay-checkout-result")
    public String showVnpayCheckoutResult(Model model, @RequestParam Map<String, String> fields) {

        String vnpTxnRef = fields.get("vnp_TxnRef");

        String tranIdstr = vnpTxnRef.substring(5);
        Long tranId = Long.valueOf(tranIdstr);

        Transaction paidTransaction = transactionService.getTransactionWithOrders(tranId);
        paidTransaction.setStatus(TransactionStatus.SUCCESS);

        StringBuilder paidInfo = new StringBuilder();
        for (String key : fields.keySet()) {
            System.out.println("_____" + key + " : " + fields.get(key));
            paidInfo.append(key + ":" + fields.get(key));
            paidInfo.append(",");
        }

        paidInfo.deleteCharAt(paidInfo.length() - 1);
        paidTransaction.setPaidInfo(paidInfo.toString());
        paidTransaction.setPaidTime(LocalDateTime.now());
        transactionService.save(paidTransaction);

        model.addAttribute("payedTransaction", paidTransaction);


        return "home/payment/rspm";
    }
}







