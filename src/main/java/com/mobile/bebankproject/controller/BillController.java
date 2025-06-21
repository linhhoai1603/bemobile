package com.mobile.bebankproject.controller;

import com.mobile.bebankproject.dto.BillRequest;
import com.mobile.bebankproject.dto.BillResponse;
import com.mobile.bebankproject.model.Bill;
import com.mobile.bebankproject.model.BillStatus;
import com.mobile.bebankproject.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @GetMapping("list")
    public ResponseEntity<List<BillResponse>> list() {
        List<BillResponse> bills = billService.findAllByOrderByCreatedDateDesc().stream().map(Bill::convertToResponse).collect(Collectors.toList());;
        return ResponseEntity.ok(bills);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillResponse>> getBillsByUserId(@PathVariable Integer userId) {
        List<Bill> bills = billService.findByUserId(userId);

        List<BillResponse> responseList = bills.stream()
                .map(Bill::convertToResponse) // hoặc dùng 1 hàm mapper riêng
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<BillResponse>> getBillsByStatusAndUser(
            @PathVariable int userId,
            @PathVariable BillStatus status) {

        List<Bill> bills = billService.findByBillStatus(status, userId);

        List<BillResponse> responseList = bills.stream()
                .map(Bill::convertToResponse) // hoặc dùng mapper class
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    @GetMapping("/code/{billCode}")
    public ResponseEntity<BillResponse> getBillByCode(@PathVariable String billCode) {
        Bill bill = billService.findByBillCode(billCode);
        BillResponse response = bill.convertToResponse(); // hoặc dùng mapper
        return ResponseEntity.ok(response);
    }
    @PostMapping("/payment")
    public ResponseEntity<?> payment(@RequestBody BillRequest request) {
        try {
            Bill paidBill = billService.paymentByBillCode(request.getBillCode(), request.getUserId(), request.getPin());
            BillResponse response = paidBill.convertToResponse();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}