package com.yzxt.yh.module.rgm.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.rgm.bean.Sport;
import com.yzxt.yh.module.rgm.dao.SportDao;

@Transactional(ConstTM.DEFAULT)
public class SportService
{
    private SportDao sportDao;

    public SportDao getSportDao()
    {
        return sportDao;
    }

    public void setSportDao(SportDao sportDao)
    {
        this.sportDao = sportDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addSport(Sport sport) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        sport.setCreateTime(now);
        sport.setUpdateTime(now);
        if (sportDao.isSportExist(sport.getName()))
        {
            return -11;
        }
        sportDao.insert(sport);
        return 1;
    }

    /**
     * 分页查询运动项目信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Sport> getSportByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return sportDao.findSportByPage(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateSport(Sport sport) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        sport.setUpdateTime(now);
        sportDao.update(sport);
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateSportState(Sport sport) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        sport.setUpdateTime(now);
        sport.setState(2);
        sportDao.updateState(sport);
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Sport> getSport(String sportName)
    {
        return sportDao.getSportByName(sportName);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Sport> getSports(String sportId, String sportLevel) throws Exception
    {
        return sportDao.getSports(sportId, sportLevel);
    }

    /*
     * 根据运动类型查询运动名称
     * @author huangGang
     * 2014.10.15
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Sport> getChildrenSports(String sportType)
    {
        return sportDao.getChildrenSports(sportType);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Sport> getTypeList(Map<String, Object> filter)
    {
        return sportDao.getTypeList(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Double getEnergyById(String sportId)
    {
        return sportDao.getEnergyById(sportId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Sport> getSportForClient()
    {
        return sportDao.getSportForClient();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Sport> getSportInfo(Map<String, Object> filter)
    {
        return sportDao.getSportInfo(filter);
    }

}
