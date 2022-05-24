package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.dto.response.ViewResponseApi;
import com.tmt.tmdt.entities.Transaction;
import com.tmt.tmdt.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/transaction")
public class TransactionController {

    private final TransactionRepo transactionRepo;

    @PersistenceContext
    private final EntityManager entityManager;


    @GetMapping("")
    public String index() {
        return "admin/transaction/index";
    }

    @GetMapping("api/viewApi")
    @ResponseBody
    public ViewResponseApi<List<Transaction>> getCategories(Model model,
                                                            @RequestParam(name = "page", required = false) String pageParam,
                                                            @RequestParam(name = "limit", required = false) String limitParam,
                                                            @RequestParam(name = "sortBy", required = false) String sortByPar,
                                                            @RequestParam(name = "sortDirection", required = false) String sortDirectionPar,
                                                            @RequestParam(name = "fromDate", required = false) String fromDate,
                                                            @RequestParam(name = "toDate", required = false) String toDate,
                                                            @RequestParam(name = "status", required = false) String status
    ) {
        String sortBy = sortByPar != null ? sortByPar : "id";
        String sortDirection = sortDirectionPar != null ? sortDirectionPar : "asc";
        int page = pageParam == null ? 0 : Integer.parseInt(pageParam) - 1;
        int limit = limitParam == null ? 5 : Integer.parseInt(limitParam);

        String query = "select * from transactions ";
        StringBuilder builder = new StringBuilder(query);
        int dateQueryFlag = 0;

        if (fromDate == null && toDate == null) {
        } else if (fromDate == null) {
            dateQueryFlag = 1;
            builder.append("where create_at ");
            builder.append("< " + "'" + toDate + "' ");

        } else if (toDate == null) {
            dateQueryFlag = 1;
            builder.append("where create_at ");
            builder.append("> " + "'" + fromDate + "' ");
        } else {
//            (fromDate != null && toDate != null)
            dateQueryFlag = 1;
            builder.append("where create_at ");
            builder.append("between " + "'" + fromDate + "' " + "and " + "'" + toDate + "' ");
        }

        if (dateQueryFlag == 1) {
            if (status != null && !status.equals("1")) {
                builder.append("and status = " + "'" + status + "' ");
            }
        } else {
            if (status != null && !status.equals("1")) {
                builder.append("where status = " + "'" + status + "' ");
            }
        }


        String countQuery = builder.toString();
        countQuery = countQuery.replace("*", "count(id)");
        Integer cnt = ((BigInteger) entityManager.createNativeQuery(countQuery).getSingleResult()).intValue();

        builder.append("order by " + sortBy + " " + sortDirection);

        int totalPage = (cnt % limit) == 0 ? (cnt / limit) : (cnt / limit + 1);
        int startIdx = page * limit;

//        System.out.println(cnt + " " + totalPage + " " + startIdx);
        builder.append(" offset " + startIdx + " limit " + limit);
        query = builder.toString();
        List<Transaction> transactions = entityManager.createNativeQuery(query, Transaction.class).getResultList();

        return new ViewResponseApi<>(totalPage, transactions);
    }
}
