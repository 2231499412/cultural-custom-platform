package com.custom.controller;

import com.custom.common.Result;
import com.custom.entity.OrderInfo;
import com.custom.entity.Template;
import com.custom.entity.User;
import com.custom.mapper.OrderInfoMapper;
import com.custom.mapper.TemplateMapper;
import com.custom.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "后台统计")
public class AdminDashboardController {

    private final UserMapper userMapper;
    private final TemplateMapper templateMapper;
    private final OrderInfoMapper orderInfoMapper;

    @GetMapping("/stats")
    @Operation(summary = "统计数据")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> stats = new HashMap<>();

        // 用户总数
        long userCount = userMapper.selectCount(new LambdaQueryWrapper<>());
        stats.put("userCount", userCount);

        // 模板数量
        long templateCount = templateMapper.selectCount(
                new LambdaQueryWrapper<Template>().eq(Template::getStatus, 1));
        stats.put("templateCount", templateCount);

        // 订单总数
        long orderCount = orderInfoMapper.selectCount(new LambdaQueryWrapper<>());
        stats.put("orderCount", orderCount);

        // 总收入（已完成的订单）
        BigDecimal totalIncome = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getStatus, "completed")
        ).stream().map(OrderInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalIncome", totalIncome);

        // 今日订单数
        long todayOrders = orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>()
                        .ge(OrderInfo::getCreateTime, java.time.LocalDate.now().atStartOfDay()));
        stats.put("todayOrders", todayOrders);

        return Result.success(stats);
    }
}
