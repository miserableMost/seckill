package com.itlaoqi.babyseckill.dao;

import com.itlaoqi.babyseckill.entity.PromotionSeckill;

import java.util.List;

/**
 * @author 李聪
 * @date 2020/4/20 9:54
 */
public interface PromotionSeckillDAO {
    public List<PromotionSeckill> findUnstartSeckill();
    public void update(PromotionSeckill promotionSeckill);
    PromotionSeckill findById(Long psId);
    List<PromotionSeckill> findExpireSeckill();
}
