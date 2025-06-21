package com.mobile.bebankproject.service.impl;

import com.mobile.bebankproject.model.Bill;
import com.mobile.bebankproject.model.BillStatus;
import com.mobile.bebankproject.model.TransactionBillPayment;
import com.mobile.bebankproject.model.TransactionStatus;
import com.mobile.bebankproject.repository.AccountRepository;
import com.mobile.bebankproject.repository.BillRepository;
import com.mobile.bebankproject.repository.TransactionRepository;
import com.mobile.bebankproject.service.AccountService;
import com.mobile.bebankproject.service.BillService;
import com.mobile.bebankproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Bill> findAllByOrderByCreatedDateDesc() {
        return billRepository.findAllByOrderByCreatedDateDesc();
    }

    @Override
    public List<Bill> findByUserId(Integer userId) {
        return billRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }

    @Override
    public List<Bill> findByBillStatus(BillStatus billStatus, int userId) {
        return billRepository.findByBillStatusAndUserIdOrderByCreatedDateDesc(billStatus, userId);
    }

    @Override
    public Bill findByBillCode(String billCode) {
        return billRepository.findByBillCode(billCode);
    }

    @Override
    @Transactional
    public Bill paymentByBillCode(String billCode, Integer userId, String pin) {
        Bill bill = billRepository.findByBillCode(billCode);
        if (bill == null) {
            throw new RuntimeException("Bill not found");
        }
        if(bill.getBillStatus() == BillStatus.PAID) {
            throw new RuntimeException("Bill already paid");
        }
        if(bill.getBillStatus() == BillStatus.OVERDUE) {
            throw new RuntimeException("Bill already overdue");
        }

        boolean isPayment = accountService.paymentBill(bill, userId, pin);
        if(!isPayment) {
            throw new RuntimeException("Payment failed");
        }

        TransactionBillPayment transaction = new TransactionBillPayment();
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount(bill.getTotalAmount());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAccount(accountRepository.findByUserId(userId));
        transaction.setBillCode(bill.getBillCode());
        transaction.setBillType(bill.getBillType());
        transaction.setDescription("Bill "+bill.getBillType() + " " + bill.getBillingPeriod());
        transactionRepository.save(transaction);

        // đã thanh toán
        bill.setBillStatus(BillStatus.PAID);
        return billRepository.save(bill);
    }
}