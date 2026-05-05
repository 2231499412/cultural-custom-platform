package com.custom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.custom.common.PageResult;
import com.custom.dto.OrderCreateDTO;
import com.custom.entity.Customization;
import com.custom.entity.OrderInfo;
import com.custom.entity.ProductionStatus;
import com.custom.mapper.CustomizationMapper;
import com.custom.mapper.OrderInfoMapper;
import com.custom.mapper.ProductionStatusMapper;
import com.custom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderInfoMapper orderInfoMapper;
    private final CustomizationMapper customizationMapper;
    private final ProductionStatusMapper productionStatusMapper;

    @Override
    @Transactional
    public String createOrder(Long userId, OrderCreateDTO dto) {
        // 验证定制记录属于当前用户
        Customization customization = customizationMapper.selectById(dto.getCustomizationId());
        if (customization == null || !customization.getUserId().equals(userId)) {
            throw new RuntimeException("定制记录不存在");
        }

        // 生成订单号: yyyyMMddHHmmss + 4位随机数
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

        OrderInfo order = new OrderInfo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setCustomizationId(dto.getCustomizationId());
        order.setTotalPrice(dto.getTotalPrice());
        order.setQuantity(dto.getQuantity());
        order.setStatus("pending");
        order.setReceiverName(dto.getReceiverName());
        order.setReceiverPhone(dto.getReceiverPhone());
        order.setReceiverAddress(dto.getReceiverAddress());
        order.setRemark(dto.getRemark());
        orderInfoMapper.insert(order);

        // 记录初始状态
        ProductionStatus status = new ProductionStatus();
        status.setOrderId(order.getId());
        status.setStatusNode("created");
        status.setRemark("订单已创建");
        productionStatusMapper.insert(status);

        return orderNo;
    }

    @Override
    public PageResult<OrderInfo> getMyOrders(Long userId, String status, Integer current, Integer size) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderInfo::getStatus, status);
        }
        wrapper.orderByDesc(OrderInfo::getCreateTime);

        Page<OrderInfo> page = orderInfoMapper.selectPage(new Page<>(current, size), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public OrderInfo getOrderDetail(Long id, Long userId) {
        OrderInfo order = orderInfoMapper.selectById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    @Override
    public void cancelOrder(Long id, Long userId) {
        OrderInfo order = orderInfoMapper.selectById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("当前状态不可取消");
        }
        order.setStatus("cancelled");
        orderInfoMapper.updateById(order);

        ProductionStatus status = new ProductionStatus();
        status.setOrderId(id);
        status.setStatusNode("cancelled");
        status.setRemark("用户取消订单");
        productionStatusMapper.insert(status);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status, String remark) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        order.setStatus(status);
        orderInfoMapper.updateById(order);

        ProductionStatus ps = new ProductionStatus();
        ps.setOrderId(orderId);
        ps.setStatusNode(status);
        ps.setRemark(remark);
        productionStatusMapper.insert(ps);
    }
}
