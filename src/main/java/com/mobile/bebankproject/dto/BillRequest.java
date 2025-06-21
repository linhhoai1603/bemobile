package com.mobile.bebankproject.dto;

import lombok.Data;

@Data
public class BillRequest {
    private String billCode;
    private Integer userId;
    private String pin;
}