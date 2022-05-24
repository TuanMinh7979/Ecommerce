package com.tmt.tmdt.controller.home;

import com.tmt.tmdt.entities.Order;
import com.tmt.tmdt.entities.Transaction;
import com.tmt.tmdt.service.OrderService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("transaction")
@RequiredArgsConstructor
public class HomeTransactionController {
    private final TransactionService transactionService;
    private final ProductService productService;
    private final OrderService orderService;

    @PostMapping("add")
    @ResponseBody
    @Transactional
    public String add(@RequestBody Map<String, String> transactionAndItsOrders) {
        String transactionStr = transactionAndItsOrders.get("transaction");
        String orderItemListStr = transactionAndItsOrders.get("orderItemList");
        JSONObject transactionJObj = new JSONObject(transactionStr);
        JSONArray orderItemListJArr = new JSONArray(orderItemListStr);
        Transaction transaction = new Transaction();
        transaction.setCustomerName((String) transactionJObj.get("customerName"));
        transaction.setCustomerPhoneNumber((String) transactionJObj.get("customerPhoneNumber"));
        transaction.setCustomerGender((String) transactionJObj.get("customerGender"));
        transaction.setCustomerAddress((String) transactionJObj.get("customerAddress"));
        transaction.setTotalPrice(new BigDecimal((String) transactionJObj.get("totalPrice")));

        Transaction savedTrans = transactionService.save(transaction);

        for (int i = 0; i < orderItemListJArr.length(); i++) {
            JSONObject orderi = orderItemListJArr.getJSONObject(i);
            Order newOrder = new Order();
            newOrder.setTransaction(savedTrans);
            newOrder.setProduct(productService.getProduct(Long.valueOf((String) orderi.get("product_id"))));

            newOrder.setUnitPrice(new BigDecimal((String) orderi.get("unitPrice")));
            newOrder.setQty(Integer.parseInt((String) orderi.get("qty")));
            newOrder.setOptions((String) orderi.get("options"));
            newOrder.setAvatar((String) orderi.get("avatar"));

            orderService.save(newOrder);

        }

        return String.valueOf(savedTrans.getId());
    }
}
