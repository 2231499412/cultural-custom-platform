package com.custom.service;

import com.custom.common.PageResult;
import com.custom.dto.OrderCreateDTO;
import com.custom.entity.OrderInfo;

public interface OrderService {

    String createOrder(Long userId, OrderCreateDTO dto);

    PageResult<OrderInfo> getMyOrders(Long userId, String status, Integer current, Integer size);

    OrderInfo getOrderDetail(Long id, Long userId);

    void cancelOrder(Long id, Long userId);

    void updateOrderStatus(Long orderId, String status, String remark);
}
