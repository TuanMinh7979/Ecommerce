package com.tmt.tmdt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmt.tmdt.constant.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String customerGender;


    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "transaction")
    Set<Order> orderItemList;

    public Transaction() {
        this.status = TransactionStatus.INIT;
    }
}
