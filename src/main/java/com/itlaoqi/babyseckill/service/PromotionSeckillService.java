package com.itlaoqi.babyseckill.service;

import com.itlaoqi.babyseckill.dao.OrderDAO;
import com.itlaoqi.babyseckill.dao.PromotionSeckillDAO;
import com.itlaoqi.babyseckill.entity.Order;
import com.itlaoqi.babyseckill.entity.PromotionSeckill;
import com.itlaoqi.babyseckill.exception.SeckillException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 李聪
 * @date 2020/4/20 10:46
 */
@Service
public class PromotionSeckillService {
    @Resource
    private PromotionSeckillDAO promotionSeckillDAO;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private OrderDAO orderDAO;
    @Resource
    private RedisTemplate redisTemplate;
    public void processSeckill(Long psId,String userid,Integer num) throws SeckillException {
        PromotionSeckill ps = promotionSeckillDAO.findById(psId);
        if(ps == null) {
            //秒杀活动不存在
            throw new SeckillException("秒杀活动不存在");
        }
        if(ps.getStatus() == 0) {
            throw new SeckillException("秒杀活动未开始");
        }else if(ps.getStatus() == 2) {
            throw  new SeckillException("秒杀活动已结束");
        }
        Integer goodsId = (Integer) redisTemplate.opsForList().leftPop("seckill:count:" + ps.getPsId());
        if(goodsId != null) {
            //判断是否抢购过
            Boolean isExisted = redisTemplate.opsForSet().isMember("seckill:users:" + ps.getPsId(), userid);
            if(!isExisted) {
                System.out.println("恭喜您抢到商品了");
                redisTemplate.opsForSet().add("seckill:users:" + ps.getPsId(),userid);
            }else {
                //补偿，将弹出的商品添加回去
                redisTemplate.opsForList().rightPush("seckill:count:" + ps.getPsId(),ps.getGoodsId());
                throw new SeckillException("抱歉，您已经参加过此活动,请勿重复抢购");
            }
        }else {
            throw  new SeckillException("抱歉，该商品已被抢光,下次再来吧");
        }

    }
    public String sendOrderToQueue(String userid) {
        System.out.println("准备向队列发送信息");
        //订单基本信息
        Map data = new HashMap<>();
        data.put("userid",userid);
        String orderNo = UUID.randomUUID().toString();
        data.put("orderNo",orderNo);
        //附加额外的订单信息
        rabbitTemplate.convertAndSend("exchange-order",null,data);
        return orderNo;
    }

    public Order checkOrder(String orderNo) {
        Order order = orderDAO.findByOrderNo(orderNo);
        return order;
    }
}
