package com.itlaoqi.babyseckill.controller;

import com.itlaoqi.babyseckill.entity.Order;
import com.itlaoqi.babyseckill.exception.SeckillException;
import com.itlaoqi.babyseckill.service.PromotionSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resources;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李聪
 * @date 2020/4/20 10:56
 */
@Controller
public class SeckillController {
    @Autowired
    PromotionSeckillService promotionSeckillService;

    @RequestMapping("/seckill")
    @ResponseBody
    public Map processSeckill(Long psid,String userid) {
        Map reslult = new HashMap();
        try {
            promotionSeckillService.processSeckill(psid,userid,1);
            String orderNo = promotionSeckillService.sendOrderToQueue(userid);
            Map data = new HashMap();
            data.put("orderNo",orderNo);
            reslult.put("code","0");
            reslult.put("message","success");
            reslult.put("data",data);
        } catch (SeckillException e) {
            reslult.put("code","500");
            reslult.put("message",e.getMessage());
            e.printStackTrace();
        }
        return reslult;
    }

    @GetMapping("/checkorder")
    public ModelAndView checkOrder(String orderNo) {
        Order order = promotionSeckillService.checkOrder(orderNo);
        ModelAndView mav = new ModelAndView();
        if(order != null) {
            mav.addObject("order",order);
            System.out.println("找到了订单=============");
            mav.setViewName("/order");
        }else {
            mav.addObject("orderNo",orderNo);
            mav.setViewName("/waiting");
        }
        return mav;
    }
}
