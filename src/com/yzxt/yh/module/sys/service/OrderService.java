/**
 * 
 */
package com.yzxt.yh.module.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.Order;
import com.yzxt.yh.module.sys.dao.OrderDao;
import com.yzxt.yh.util.PageBean;



/**
 * <p>
 * Title: OrderService
 * </p>
 * <p>
 * @function:
 * </p>
 * 
 * @date 下午7:42:15
 */
@Transactional(ConstTM.DEFAULT)
public class OrderService {
	private OrderDao orderDao;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	/**
	 * 生成订单的方法
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Order order) {
		orderDao.save(order);
	}

	/**
	 * @param uid
	 * @param page
	 *            我的订单的业务成代码
	 *//*
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PageBean<Order> findByPageUid(String uid, Integer page) {
		PageBean<Order> pageBean = new PageBean<Order>();
		pageBean.setPage(page);

		int limit = 4;
		pageBean.setLimit(limit);
		Integer totalCount = null;

		totalCount = orderDao.findByCountUid(uid);
		pageBean.setTotalCount(totalCount);

		Integer totalPage = null;
		if (totalCount % limit == 0) {
			totalPage = totalCount / limit;
		} else {
			totalPage = totalCount / limit + 1;
		}
		pageBean.setTotalPage(totalPage);
		Integer begin = (page - 1) * limit;
		List<Order> list = orderDao.findByPageUid(uid, begin, limit);
		pageBean.setList(list);
		return pageBean;
	}*/
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	 public List<Order> queryOrderTran(Map<String, Object> filter) {
	    	
		return orderDao.queryOrderTran(filter);
	}
	/**
	 * @param oid
	 *            根据订单id 查询订单
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Order findByOid(String oid) {
		return orderDao.findByOid(oid);
	}

	/**
	 * @param ourrOrder
	 * @return 
	 */
	 @Transactional(propagation = Propagation.REQUIRED)
	public boolean update(Order ourrOrder) {
		orderDao.update(ourrOrder);
		return true;

	}

	/**
	 * 分页查询订单的方法
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PageBean<Order> findByPage(Integer page) {
		PageBean<Order> pageBean = new PageBean<Order>();
		pageBean.setPage(page);

		int limit = 10;
		pageBean.setLimit(limit);
		Integer totalCount = null;

		totalCount = orderDao.findByCount();
		pageBean.setTotalCount(totalCount);

		Integer totalPage = null;
		if (totalCount % limit == 0) {
			totalPage = totalCount / limit;
		} else {
			totalPage = totalCount / limit + 1;
		}
		pageBean.setTotalPage(totalPage);
		Integer begin = (page - 1) * limit;
		List<Order> list = orderDao.findByPage( begin, limit);
		pageBean.setList(list);
		return pageBean;
	}
	/**
	 * 删除订单
	 * @param ourrOrder
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteOrder(Order order) {
		orderDao.deleteOrder(order);

	}
	@Transactional(propagation = Propagation.REQUIRED,readOnly = true)
	public Object[] getOrderByOrderId(String orderId) {
		
		return orderDao.getOrderByOrderId(orderId);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Object[] getDoctorNameByOrderId(String orderId) {
		
		return orderDao.getDoctorNameByOrderId(orderId);
	}
	
}
