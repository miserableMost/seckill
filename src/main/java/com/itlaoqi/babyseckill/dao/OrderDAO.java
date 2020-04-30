package com.itlaoqi.babyseckill.dao;

import com.itlaoqi.babyseckill.entity.Order;

/**
 * @author 李聪
 * @date 2020/4/23 20:51
 */
public interface OrderDAO {
    public void insert(Order order);
    public Order findByOrderNo(String orderNo);
}
