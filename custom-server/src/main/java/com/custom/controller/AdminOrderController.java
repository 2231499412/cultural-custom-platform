package com.custom.controller;

import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.entity.OrderInfo;
import com.custom.mapper.OrderInfoMapper;
import com.custom.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Tag(name = "后台订单管理")
public class AdminOrderController {

    private final OrderInfoMapper orderInfoMapper;
    private final OrderService orderService;

    @GetMapping("/list")
    @Operation(summary = "订单列表（后台）")
    public Result<PageResult<OrderInfo>> list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderInfo::getStatus, status);
        }
        wrapper.orderByDesc(OrderInfo::getCreateTime);

        Page<OrderInfo> page = orderInfoMapper.selectPage(new Page<>(current, size), wrapper);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "确认订单")
    public Result<Void> confirm(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "confirmed", "商家已确认订单");
        return Result.success();
    }

    @PutMapping("/{id}/produce")
    @Operation(summary = "开始生产")
    public Result<Void> produce(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "producing", "订单已进入生产");
        return Result.success();
    }

    @PutMapping("/{id}/ship")
    @Operation(summary = "发货")
    public Result<Void> ship(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String remark = body.getOrDefault("remark", "商品已发货");
        orderService.updateOrderStatus(id, "shipped", remark);
        return Result.success();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成订单")
    public Result<Void> complete(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "completed", "订单已完成");
        return Result.success();
    }
}
