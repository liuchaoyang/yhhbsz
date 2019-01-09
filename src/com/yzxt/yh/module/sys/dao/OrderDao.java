/**
 * 
 */
package com.yzxt.yh.module.sys.dao;

import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.yh.module.sys.bean.Order;
import com.yzxt.yh.util.PageHibernateCallback;
import com.yzxt.yh.util.StringUtil;



/**
 * <p>
 * Title: OrderDao
 * </p>
 * <p>
 * @function:
 * </p>
 * 
 * @author Codeagles ,
 * @date 下午7:42:08
 */
public class OrderDao extends HibernateDaoSupport {

	/**
	 * order订单操作保存
	 */
	public void save(Order order) {
		this.getHibernateTemplate().save(order);

	}

	/**
	 * @param uid
	 *            我的订单：通过UID查询总记录数
	 */
	public Integer findByCountUid(String uid) {
		String hql = "select count(*) from Order o where o.user.uid=?";
		List<Long> list = this.getHibernateTemplate().find(hql, uid);
		if (list != null && list.size() > 0) {
			return list.get(0).intValue();
		}
		return null;
	}

	// 我的订单的查询
	public List<Order> queryOrderTran(Map<String, Object> filter) {
		String hql = "from Order o where o.uid=? and o.state=? order by ordertime desc";
		String uid = (String) filter.get("userId");
		String stateStr= (String) filter.get("state");
		Integer state=0;
		if(StringUtil.isNotEmpty(stateStr)){
			state = Integer.parseInt(stateStr);
		}
		List<Order> list=this.getHibernateTemplate().find(hql, uid,state);
		if(list != null && list.size() > 0){
			return list;
		}
		return null;

	}

	/**
	 * @param oid
	 * @return
	 */
	public Order findByOid(String oid) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().get(Order.class, oid);
	}

	/**
	 * @param ourrOrder
	 */
	public void update(Order ourrOrder) {
		this.getHibernateTemplate().update(ourrOrder);

	}

	/**
	 * 查询订单总数
	 */
	public Integer findByCount() {
		String hql = "select count(*) from Order ";
		List<Long> list = this.getHibernateTemplate().find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0).intValue();
		}
		return 0;
	}

	/**
	 * 查询订单的集合
	 */
	public List<Order> findByPage(Integer begin, int limit) {
		String hql="from Order order by ordertime desc";
		List<Order>list=this.getHibernateTemplate().execute(new PageHibernateCallback<Order>(hql, null, begin, limit));
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
	}
	
	/**
	 * 
	 * @param order
	 */
	public void deleteOrder(Order order) {
		this.getHibernateTemplate().delete(order);
		
	}
	
	/**
	 * 获取订单详情
	 * @param orderId
	 * @return
	 */
	public Object[] getOrderByOrderId(String orderId) {
		HibernateParams params = new HibernateParams();
		StringBuilder mSql = new StringBuilder();
		mSql.append(" from sys_customer t");
		mSql.append(" inner join sys_user tu on tu.id = t.user_id");
		mSql.append(" inner join orders o on tu.id = o.uid");
		mSql.append(" inner join sys_org tor on tor.id = tu.org_id");
		mSql.append(" inner join sys_doctor sd on o.doctorId = sd.user_id");
		mSql.append(" left join svb_member_info tmi on tmi.state = 1 and tmi.doctor_id = sd.user_id");
		mSql.append(" where o.oid= '" + orderId + "'" );
		mSql.append(" GROUP BY sd.user_id" );
		/*mSql.append(" where tu.id= ?");
		// 查询条件
		if (StringUtil.isNotEmpty(doctorId)) {
			mSql.append(" and tu.id=? ");
		}
		params.add(doctorId, Hibernate.STRING);*/
		StringBuilder pSql = new StringBuilder();
		pSql.append(
				"select t.user_id,tu.sex,tu.name_,tmi.start_day,tmi.end_day,o.ordertime,o.total,sd.type,sd.dept_name,t.memo,tor.orgName,o.count,sd.profession_title");
		//pSql.append(",(select su.name_ from orders os inner join sys_user su on su.id = os.doctorId  where os.oid= '" + orderId + "') as doctorName");
		pSql.append(mSql);
		Object obj = this.getSession().createSQLQuery(pSql.toString()).uniqueResult();
		Object[] objs = (Object[]) obj;
		return objs;
	}
	/**
	 * 查询医生姓名
	 */
	
	public Object[] getDoctorNameByOrderId(String orderId){
		StringBuilder mSql = new StringBuilder();
		mSql.append(" select su.name_ ,su.id from orders os inner join sys_user su on su.id = os.doctorId  where os.oid= '" + orderId + "'");
		Object obj = this.getSession().createSQLQuery(mSql.toString()).uniqueResult();
		Object[] objs = (Object[]) obj;
		return objs;
		
	}

}
