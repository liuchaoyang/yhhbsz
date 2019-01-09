package com.yzxt.yh.module.cli.service;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstMap;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.eif.mp.MpConst;
import com.yzxt.yh.eif.mp.MpSender;
import com.yzxt.yh.module.cli.bean.CustPosition;
import com.yzxt.yh.module.cli.dao.CustPositionDao;

@Transactional(ConstTM.DEFAULT)
public class CustPositionService
{
    private Logger logger = Logger.getLogger(CustPositionService.class);
    private CustPositionDao custPositionDao;

    public CustPositionDao getCustPositionDao()
    {
        return custPositionDao;
    }

    public void setCustPositionDao(CustPositionDao custPositionDao)
    {
        this.custPositionDao = custPositionDao;
    }

    /**
     * 保存客户位置信息
     * @param bean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(CustPosition bean) throws Exception
    {
        // 向地图服务推送位置信息
        if (ConstDevice.DEVICE_TYPE_WA_SUREZEN.equals(bean.getDeviceName()))
        {
            if (Constant.POSITION_MODE_LBS.equals(bean.getPositionMode()))
            {
                int mr = MpSender.sendLbs(ConstMap.CUST_CODE_PREFIX + bean.getCustId(), bean.getCheckTime(),
                        bean.getMnc(), bean.getLac(), bean.getCellid());
                if (mr != MpConst.OPER_STATE_SUCCESS)
                {
                    logger.debug("向地图服务发送LBS位置信息失败，错误码：" + mr);
                }
            }
            else if (Constant.POSITION_MODE_GPS.equals(bean.getPositionMode()))
            {
                int mr = MpSender.sendGps(ConstMap.CUST_CODE_PREFIX + bean.getCustId(), bean.getCheckTime(),
                        bean.getLongitude(), bean.getLatitude());
                if (mr != MpConst.OPER_STATE_SUCCESS)
                {
                    logger.debug("向地图服务发送GPS位置信息失败，错误码：" + mr);
                }
            }
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setCreateTime(now);
        custPositionDao.insert(bean);
    }

    /**
     * 查询客户位置信息记录列表
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CustPosition> query(Map<String, Object> filter, int page, int pageSize)
    {
        return custPositionDao.query(filter, page, pageSize);
    }

    /**
     * 查询客户位置信息记录列表
     * @param filter
     * @return
     * @author h
     * 2015.11.30
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<CustPosition> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return custPositionDao.queryTran(filter, sysTime, dir, count);
    }

    /**
     * 查询客户当前位置信息记录
     * @param filter
     * @return
     * @author h
     * 2015.11.30
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CustPosition queryCurrentPosition(Map<String, Object> filter)
    {
        return custPositionDao.queryCurrentPosition(filter);
    }

    /**
     * 查询客户最后一次的位置信息记录
     * @param filter
     * @return
     * @author h
     * 2015.11.30
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CustPosition queryLastPostion(Map<String, Object> filter)
    {
        return custPositionDao.queryLastPostion(filter);
    }

}
