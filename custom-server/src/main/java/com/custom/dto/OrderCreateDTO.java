package com.custom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateDTO {

    @NotNull(message = "定制记录ID不能为空")
    private Long customizationId;

    @NotNull(message = "总价不能为空")
    private BigDecimal totalPrice;

    private Integer quantity = 1;

    @NotBlank(message = "收货人不能为空")
    private String receiverName;

    @NotBlank(message = "收货电话不能为空")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    private String remark;
}
