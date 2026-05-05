package com.custom.controller;

import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.dto.OrderCreateDTO;
import com.custom.entity.OrderInfo;
import com.custom.entity.ProductionStatus;
import com.custom.mapper.ProductionStatusMapper;
import com.custom.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "订单接口")
public class OrderController {

    private final OrderService orderService;
    private final ProductionStatusMapper productionStatusMapper;

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<String> create(@Valid @RequestBody OrderCreateDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String orderNo = orderService.createOrder(userId, dto);
        return Result.success(orderNo);
    }

    @GetMapping("/list")
    @Operation(summary = "我的订单列表")
    public Result<PageResult<OrderInfo>> list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getMyOrders(userId, status, current, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "订单详情")
    public Result<OrderInfo> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getOrderDetail(id, userId));
    }

    @GetMapping("/{id}/status-log")
    @Operation(summary = "订单状态记录")
    public Result<List<ProductionStatus>> statusLog(@PathVariable Long id) {
        List<ProductionStatus> list = productionStatusMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductionStatus>()
                        .eq(ProductionStatus::getOrderId, id)
                        .orderByAsc(ProductionStatus::getCreateTime)
        );
        return Result.success(list);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消订单")
    public Result<Void> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        orderService.cancelOrder(id, userId);
        return Result.success();
    }
}
