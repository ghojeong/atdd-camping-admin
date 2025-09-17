package com.camping.admin.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessSaleRequest {
    private List<SaleItemResponse> items;
}