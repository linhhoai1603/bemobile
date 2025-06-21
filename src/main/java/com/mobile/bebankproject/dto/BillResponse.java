package com.mobile.bebankproject.dto;

import com.mobile.bebankproject.model.BillStatus;
import com.mobile.bebankproject.model.BillType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BillResponse {
    private int id;
    private String billCode;
    private BillType billType; // Ví dụ: "Electricity", "Water", "WiFi"
    private String billingPeriod; // Thời gian tính hóa đơn
    private double usageAmount;   // KWh, m3, hoặc GB
    private double unitPrice;     // Đơn giá / đơn vị sử dụng
    private double totalAmount;   // Tổng tiền
    private BillStatus billStatus;   // "Paid", "Unpaid", "Overdue"
    private LocalDate createdDate;
    private LocalDate dueDate;
}