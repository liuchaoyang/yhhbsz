package com.yzxt.yh.module.his.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.bridge.context.PinpointingMessageHandler;
import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.his.bean.Appoint;
import com.yzxt.yh.module.his.bean.HisPros;
import com.yzxt.yh.module.his.bean.HisYpkc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.PinYin4jUtils;
import com.yzxt.yh.util.StringUtil;
import com.yzxt.yh.util.ChineseCharToEn;


public class DzcfDao extends HibernateSupport<HisPros> implements BaseDao<HisPros>{
	@SuppressWarnings("unchecked")
	public PageModel<HisYpkc> getList(Map<String, Object> filter, int page, int pageSize) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append(" from his_ypkc t");
		sql.append(" where 1=1");
		// 查询条件
		String ypjx = "";
		if (filter != null) {
			ypjx = (String) filter.get("ypjx");
	        if (StringUtil.isNotEmpty(ypjx))
	        {   
	        	ChineseCharToEn car = new ChineseCharToEn();
	        	//String hanziToPinyin = PinYin4jUtils.hanziToPinyin(ypmc);
	            sql.append(" and t.ypjx like '%" + ypjx + "%'");
	        }
		}
     // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.sqid) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.sqid, t.ypmc, t.ypjx, t.ypgg, t.ypdj, t.ybflag,t.ypflag");
        pSql.append(sql);
//        pSql.append(" limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).list();
		List<HisYpkc> list = new ArrayList<>();
		if (objsList != null && objsList.size() > 0) {
			
			for (Object[] objs : objsList) {
				list.add(objToHisYpkc(objs));
			}
		}
		PageModel<HisYpkc> pageModel = new PageModel<HisYpkc>();
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
	}

	public String insert(HisPros hisPros) throws Exception {
		 super.insert(hisPros);
	     return hisPros.getProsid();
	}
	
	public HisYpkc getById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("select t.sqid, t.ypmc, t.ypjx, t.ypgg, t.ypdj, t.ybflag,t.ypflag");
		buffer.append(" from his_ypkc t");
		buffer.append(" where t.sqid = '" + id + "'");
		Object obj = this.getSession().createSQLQuery(buffer.toString()).uniqueResult();
		Object[] objs = (Object[]) obj;
		
		return objToHisYpkc(objs);
	}
	
	private static HisYpkc objToHisYpkc(Object[] objs) {
		if (objs == null || objs.length < 6) {
			return null;
		}
		HisYpkc bean = new HisYpkc();
		bean.setSqid((String) objs[0]);
		bean.setYpmc((String) objs[1]);
		bean.setYpjx((String) objs[2]);
		bean.setYpgg((String) objs[3]);
		if (objs[4] != null) {
			bean.setYpdj((new BigDecimal(objs[4].toString())));
		}
		if (objs[5] != null) {
			String ybflagjStr = String.valueOf(objs[5]);
			if (!"null".equals(ybflagjStr)) {
				bean.setYbflag(Integer.parseInt(ybflagjStr));
			}
		}
		if (objs[6] != null) {
			String ypflagjStr = String.valueOf(objs[5]);
			if (!"null".equals(ypflagjStr)) {
				bean.setYpflag(Integer.parseInt(ypflagjStr));
			}
		}
		return bean;
	}

	public List<HisPros> queryChufang(Map<String, Object> filter) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select p.prosid,tu.name_ as custName, d.ypgg,d.ypmc,i.price,i.num,i.userage,i.uselevel,i.unit");
		sql.append(" from his_pros p");
		sql.append(" inner join sys_user tu on tu.id = p.patientid");
		sql.append(" inner join his_ypkc d on d.sqid = p.ypid");
		sql.append(" inner join his_pros_item i on i.prosid=p.prosid");
		sql.append(" where 1=1");
		// 查询条件
		String status = (String) filter.get("status");
		if (StringUtil.isNotEmpty(status)) {
			sql.append(" and p.status = ?");
			Integer state = Integer.parseInt(status);
			params.add(state, Hibernate.INTEGER);
		}
		User user = (User) filter.get("operUser");
		sql.append(" and p.doctorid = '").append(user.getId()).append("'");
		@SuppressWarnings("unchecked")
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("prosid", Hibernate.STRING)
		.addScalar("custName", Hibernate.STRING).addScalar("ypgg", Hibernate.STRING).addScalar("ypmc", Hibernate.STRING)
		.addScalar("num", Hibernate.INTEGER).addScalar("userage", Hibernate.STRING)
		.addScalar("uselevel", Hibernate.STRING).addScalar("unit", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes())
		.list();
		List<HisPros> list = new ArrayList<HisPros>();
		int idx = 0;
		for (Object[] objs : objsList) {
			HisPros bean = new HisPros();
			idx = 0;
			bean.setProsid((String) objs[idx]);
			idx++;
			bean.setCustName((String) objs[idx]);
			idx++;
			bean.setYpgg((String) objs[idx]);
			idx++;
			bean.setYpmc((String) objs[idx]);
			idx++;
			/*bean.setPrice(new BigDecimal(objs[idx].toString()));
			idx++;*/
			bean.setNum(Integer.parseInt(objs[idx].toString()));
			idx++;
			bean.setUserage((String) objs[idx]);
			idx++;
			bean.setUselevel((String) objs[idx]);
			idx++;
			bean.setUnit(Integer.parseInt(objs[idx].toString()));
			idx++;
			list.add(bean);
		}
		return list;
	}
	
	/**
	 * 
	 * 客户端查询处方
	 * @param filter
	 * @return
	 */
	public List<HisPros> queryDzcf(Map<String, Object> filter) {
		HibernateParams params = new HibernateParams();
		StringBuilder sql = new StringBuilder();
		sql.append("select p.prosid,tu.name_ as custName, d.ypgg,d.ypmc,i.price,i.num,i.userage,i.uselevel,i.unit,p.status");
		sql.append(" from his_pros p");
		sql.append(" inner join sys_user tu on tu.id = p.patientid");
		sql.append(" inner join his_ypkc d on d.sqid = p.ypid");
		sql.append(" inner join his_pros_item i on i.prosid=p.prosid");
		sql.append(" where 1=1");
		// 查询条件
		/*String status = (String) filter.get("status");
		if (StringUtil.isNotEmpty(status)) {
			sql.append(" and t.status = ?");
			Integer state = Integer.parseInt(status);
			params.add(state, Hibernate.INTEGER);
		}*/
		Integer status = (Integer) filter.get("status");
		if(status!=null){
			sql.append(" and p.status = ?");
			params.add(status, Hibernate.INTEGER);
		}
		String custId = (String) filter.get("custId");
		if(StringUtil.isNotEmpty(custId)){
			sql.append(" and p.patientid = ?");
			params.add(custId, Hibernate.STRING);
		}
		@SuppressWarnings("unchecked")
		List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("prosid", Hibernate.STRING)
		.addScalar("custName", Hibernate.STRING).addScalar("ypgg", Hibernate.STRING).addScalar("ypmc", Hibernate.STRING)
		.addScalar("num", Hibernate.INTEGER).addScalar("userage", Hibernate.STRING)
		.addScalar("uselevel", Hibernate.STRING).addScalar("unit", Hibernate.INTEGER)
		.addScalar("status", Hibernate.INTEGER)
		.setParameters(params.getVals(), params.getTypes())
		.list();
		List<HisPros> list = new ArrayList<HisPros>();
		int idx = 0;
		for (Object[] objs : objsList) {
			HisPros bean = new HisPros();
			idx = 0;
			bean.setProsid((String) objs[idx]);
			idx++;
			bean.setCustName((String) objs[idx]);
			idx++;
			bean.setYpgg((String) objs[idx]);
			idx++;
			bean.setYpmc((String) objs[idx]);
			idx++;
			/*bean.setPrice(new BigDecimal(objs[idx].toString()));
			idx++;*/
			bean.setNum(Integer.parseInt(objs[idx].toString()));
			idx++;
			bean.setUserage((String) objs[idx]);
			idx++;
			bean.setUselevel((String) objs[idx]);
			idx++;
			bean.setUnit(Integer.parseInt(objs[idx].toString()));
			idx++;
			bean.setStatus(Integer.parseInt(objs[idx].toString()));
			idx++;
			list.add(bean);
		}
		return list;
	}
}
