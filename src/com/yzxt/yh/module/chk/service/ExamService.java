package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.IdGenerator;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.Exam;
import com.yzxt.yh.module.chk.bean.ExamFamilyHosHis;
import com.yzxt.yh.module.chk.bean.ExamHospitalHis;
import com.yzxt.yh.module.chk.bean.ExamInoculate;
import com.yzxt.yh.module.chk.bean.ExamMedic;
import com.yzxt.yh.module.chk.bean.ExamPosion;
import com.yzxt.yh.module.chk.dao.ExamDao;
import com.yzxt.yh.module.chk.dao.ExamFamilyHosHisDao;
import com.yzxt.yh.module.chk.dao.ExamHospitalHisDao;
import com.yzxt.yh.module.chk.dao.ExamInoculateDao;
import com.yzxt.yh.module.chk.dao.ExamMedicDao;
import com.yzxt.yh.module.chk.dao.ExamPosionDao;

@Transactional(ConstTM.DEFAULT)
public class ExamService
{
    private ExamDao examDao;

    private ExamHospitalHisDao examHospitalHisDao;

    private ExamFamilyHosHisDao examFamilyHosHisDao;

    private ExamInoculateDao examInoculateDao;

    private ExamPosionDao examPosionDao;

    private ExamMedicDao examMedicDao;

    public ExamDao getExamDao()
    {
        return examDao;
    }

    public void setExamDao(ExamDao examDao)
    {
        this.examDao = examDao;
    }

    public ExamHospitalHisDao getExamHospitalHisDao()
    {
        return examHospitalHisDao;
    }

    public void setExamHospitalHisDao(ExamHospitalHisDao examHospitalHisDao)
    {
        this.examHospitalHisDao = examHospitalHisDao;
    }

    public ExamFamilyHosHisDao getExamFamilyHosHisDao()
    {
        return examFamilyHosHisDao;
    }

    public void setExamFamilyHosHisDao(ExamFamilyHosHisDao examFamilyHosHisDao)
    {
        this.examFamilyHosHisDao = examFamilyHosHisDao;
    }

    public ExamInoculateDao getExamInoculateDao()
    {
        return examInoculateDao;
    }

    public void setExamInoculateDao(ExamInoculateDao examInoculateDao)
    {
        this.examInoculateDao = examInoculateDao;
    }

    public ExamPosionDao getExamPosionDao()
    {
        return examPosionDao;
    }

    public void setExamPosionDao(ExamPosionDao examPosionDao)
    {
        this.examPosionDao = examPosionDao;
    }

    public ExamMedicDao getExamMedicDao()
    {
        return examMedicDao;
    }

    public void setExamMedicDao(ExamMedicDao examMedicDao)
    {
        this.examMedicDao = examMedicDao;
    }

    /**
     * 查询体检记录
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Exam> queryCustExam(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return examDao.queryCustExam(filter, page, pageSize);
    }

    /**
     * 加载体检信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Exam load(String id)
    {
        Exam exam = examDao.get(id);
        exam.setExamPosions(examPosionDao.getByExam(id));
        exam.setExamHospitalHiss(examHospitalHisDao.getByExam(id));
        exam.setExamFamilyHosHiss(examFamilyHosHisDao.getByExam(id));
        exam.setExamMedics(examMedicDao.getByExam(id));
        exam.setExamInoculates(examInoculateDao.getByExam(id));
        return exam;
    }

    /**
     * 新增体检表
     * @param exam
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String add(Exam exam) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String id = IdGenerator.getUUID();
        // 同样信息
        exam.setCreateTime(now);
        exam.setUpdateBy(exam.getCreateBy());
        exam.setUpdateTime(now);
        // 产生体检流水编号
        String examNo = examDao.getNextExamNo(now);
        exam.setExamNo(examNo);
        // 设置主键
        exam.setId(id);
        int seq = 0;
        List<ExamFamilyHosHis> list1 = exam.getExamFamilyHosHiss();
        if (list1 != null)
        {
            seq = 0;
            for (ExamFamilyHosHis family : list1)
            {
                family.setExamId(id);
                family.setSeq(seq++);
                examFamilyHosHisDao.insert(family);
            }
        }
        List<ExamHospitalHis> list2 = exam.getExamHospitalHiss();
        if (list2 != null)
        {
            seq = 0;
            for (ExamHospitalHis hosp : list2)
            {
                hosp.setExamId(id);
                hosp.setSeq(seq++);
                examHospitalHisDao.insert(hosp);
            }
        }
        List<ExamInoculate> list3 = exam.getExamInoculates();
        if (list3 != null)
        {
            seq = 0;
            for (ExamInoculate ino : list3)
            {
                ino.setExamId(id);
                ino.setSeq(seq++);
                examInoculateDao.insert(ino);
            }
        }
        List<ExamMedic> list4 = exam.getExamMedics();
        if (list4 != null)
        {
            seq = 0;
            for (ExamMedic med : list4)
            {
                med.setExamId(id);
                med.setSeq(seq++);
                examMedicDao.insert(med);
            }
        }
        List<ExamPosion> list5 = exam.getExamPosions();
        if (list5 != null)
        {
            seq = 0;
            for (ExamPosion pos : list5)
            {
                pos.setExamId(id);
                pos.setSeq(seq++);
                examPosionDao.insert(pos);
            }
        }
        examDao.insert(exam);
        return id;
    }

    /**
     * 修改健康体检表
     * @param healthyExam
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String update(Exam exam) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        exam.setUpdateTime(now);
        String id = exam.getId();
        Exam oldExam = examDao.get(id);
        if (oldExam != null)
        {
            // 字段太多，简单处理
            BeanUtils.copyProperties(exam, oldExam, new String[]
            {"id", "custId", "examNo", "name", "createBy", "createTime", "examFamilyHosHis", "examHospitalHis",
                    "examInoculate", "examMedic", "examPosion"});
            int seq = 0;
            List<ExamFamilyHosHis> fhs = exam.getExamFamilyHosHiss();
            examFamilyHosHisDao.deleteByExam(id);
            if (fhs != null)
            {
                seq = 0;
                for (ExamFamilyHosHis fh : fhs)
                {
                    fh.setExamId(id);
                    fh.setSeq(seq++);
                    examFamilyHosHisDao.insert(fh);
                }
            }
            List<ExamHospitalHis> ips = exam.getExamHospitalHiss();
            examHospitalHisDao.deleteByExam(id);
            if (ips != null)
            {
                seq = 0;
                for (ExamHospitalHis ip : ips)
                {
                    ip.setExamId(id);
                    ip.setSeq(seq++);
                    examHospitalHisDao.insert(ip);
                }
            }
            List<ExamInoculate> inos = exam.getExamInoculates();
            examInoculateDao.deleteByExam(id);
            if (inos != null)
            {
                seq = 0;
                for (ExamInoculate ino : inos)
                {
                    ino.setExamId(id);
                    ino.setSeq(seq++);
                    examInoculateDao.insert(ino);
                }
            }
            List<ExamMedic> meds = exam.getExamMedics();
            examMedicDao.deleteByExam(id);
            if (meds != null)
            {
                seq = 0;
                for (ExamMedic med : meds)
                {
                    med.setExamId(id);
                    med.setSeq(seq++);
                    examMedicDao.insert(med);
                }
            }
            List<ExamPosion> poss = exam.getExamPosions();
            examPosionDao.deleteByExam(id);
            if (poss != null)
            {
                seq = 0;
                for (ExamPosion pos : poss)
                {
                    pos.setExamId(id);
                    pos.setSeq(seq++);
                    examPosionDao.insert(pos);
                }
            }
            examDao.update(oldExam);
        }
        return id;
    }

    /**
     * 查询单个客户体检记录
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Exam> queryCustExamTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return examDao.queryCustExamTran(filter, sysTime, dir, count);
    }

}
