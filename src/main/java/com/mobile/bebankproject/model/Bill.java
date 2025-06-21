package com.mobile.bebankproject.model;

import com.mobile.bebankproject.dto.BillResponse;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String billCode;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @Enumerated(EnumType.STRING)
    private BillType billType; // Ví dụ: "Electricity", "Water", "WiFi"
    private String billingPeriod; // Thời gian tính hóa đơn
    private double usageAmount;   // KWh, m3, hoặc GB
    private double unitPrice;     // Đơn giá / đơn vị sử dụng
    private double totalAmount;   // Tổng tiền
    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;   // "Paid", "Unpaid", "Overdue"
    private LocalDate createdDate;
    private LocalDate dueDate;

    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        if(billType == BillType.INTERNET) {
            this.totalAmount = this.unitPrice;
        }else {
            this.totalAmount = this.usageAmount * this.unitPrice;
        }
    }
    public BillResponse convertToResponse() {
        BillResponse billResponse = new BillResponse();
        billResponse.setId(this.id);
        billResponse.setBillCode(this.billCode);
        billResponse.setBillType(this.billType);
        billResponse.setBillingPeriod(this.billingPeriod);
        billResponse.setUsageAmount(this.usageAmount);
        billResponse.setUnitPrice(this.unitPrice);
        billResponse.setTotalAmount(this.totalAmount);
        billResponse.setBillStatus(this.billStatus);
        billResponse.setCreatedDate(this.createdDate);
        billResponse.setDueDate(this.dueDate);
        return billResponse;
    }
}