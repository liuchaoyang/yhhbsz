package com.yzxt.tran;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import com.yzxt.yh.module.sys.bean.Order;
import com.yzxt.yh.module.sys.service.OrderService;
/**
 * Timer定时器
 * @Description TODO
 * 2017年12月11日 上午9:43:08
 * @version V1.0
 */
public class CommTimer {
    private static final Logger out = Logger.getLogger(CommTimer.class);
    
    private static OrderService orderService;
    public OrderService getOrderService() {
		return orderService;
	}
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	/**
     * 订单关闭(设置指定24小时后，修改订单状态为已关闭)
     * @param bolId  订单号
     */
    public static void orderClose(final String orderId) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               try {
            	   System.out.println("60秒后执行此方法");
            	   Order order = orderService.findByOid(orderId);
            	   order.setState(2);
            	   orderService.update(order);
			} catch (Exception e) {
				// TODO: handle exception
			}
                // 中断线程
                timer.cancel();
            }
        },60*1000);
    }
}

