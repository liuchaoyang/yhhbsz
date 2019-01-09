package com.yzxt.yh.module.his.dao;

import java.io.Serializable;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.his.bean.ProsItem;

public class ProItemDao extends HibernateSupport<ProsItem> implements BaseDao<ProsItem>{

	@Override
	public ProsItem get(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}
	public String insert(ProsItem prosItem) throws Exception {
		 super.insert(prosItem);
	     return prosItem.getItemid();
	}

}
