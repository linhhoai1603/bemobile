package com.mobile.bebankproject.service;

import com.mobile.bebankproject.model.Bill;
import com.mobile.bebankproject.model.BillStatus;

import java.util.List;

public interface BillService {
    List<Bill> findAllByOrderByCreatedDateDesc();
    List<Bill> findByUserId(Integer userId);
    List<Bill> findByBillStatus(BillStatus billStatus, int userId);
    Bill findByBillCode(String billCode);
    Bill paymentByBillCode(String billCode, Integer userId, String pin);
}