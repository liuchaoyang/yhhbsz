package com.yzxt.yh.module.chk.util;

import org.apache.log4j.Logger;

import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.util.DecimalUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 采集数据分析类。
 * 告警等级，小于0表示正常，大于0表示异常，等于0或为空为未分析
 *
 */
public class AnalysisCheckData
{
    private static Logger logger = Logger.getLogger(AnalysisCheckData.class);

    /**
     * 医学数值处理
     * +1(+),+2,...返回后面的数值
     * +-返回0
     * -1(-1),-2,...返回后面值的负数
     * @param flag
     * @return
     */
    private static Integer getFlagNum(String flagVal)
    {
        if (StringUtil.isEmpty(flagVal))
        {
            return null;
        }
        Integer v = null;
        if ("+-".equals(flagVal))
        {
            v = 0;
        }
        else if ("+".equals(flagVal))
        {
            v = 1;
        }
        else if ("-".equals(flagVal))
        {
            v = -1;
        }
        else if (DecimalUtil.isInteger(flagVal))
        {
            v = DecimalUtil.toInteger(flagVal);
        }
        else
        {
            logger.error("Unknow analysis uric flag value: " + flagVal);
        }
        return v;
    }

    /**
     * 血压诊断标准：
     * 
     * 正常血压，收缩压<120，舒张压<80。
     * 正常高值，收缩压120～139，舒张压80～89。
     * 高血压，收缩压≥140，舒张压≥90。
     * 1级高血压（轻度），收缩压140～159 ，舒张压90～99。
     * 2级高血压（中度），收缩压160～179 ，舒张压100～109。
     * 3级高血压（重度），收缩压≥180，舒张压≥110。
     * 单纯收缩期高血压，收缩压≥140，舒张压<90。
     * 如患者的收缩压与舒张压分属不同的级别时，则以较高的分级标准为准。单纯收缩期高血压也可按照收缩压水平分为1、2、3级。
     * 
     * 状况分类，level：-1-正常，1-轻度，2-中度，3-重度。
     */
    public static AnalysisResult bloodPressure(Double dbp, Double sbp)
    {
        // 收缩压等级
        int sbpLevel = 0;
        if (sbp < 140)
        {
            sbpLevel = -1;
        }
        else if (sbp <= 159)
        {
            sbpLevel = 1;
        }
        else if (sbp <= 179)
        {
            sbpLevel = 2;
        }
        else
        {
            sbpLevel = 3;
        }
        // 舒张压等级
        int dbpLevel = 0;
        if (dbp < 90)
        {
            dbpLevel = -1;
        }
        else if (dbp <= 99)
        {
            dbpLevel = 1;
        }
        else if (dbp <= 109)
        {
            dbpLevel = 2;
        }
        else
        {
            dbpLevel = 3;
        }
        String desc = "";
        // 取较高的分级标准为准
        int level = sbpLevel >= dbpLevel ? sbpLevel : dbpLevel;
        if (level <= 0)
        {
            //desc = "舒张压:" + DecimalUtil.toString(dbp) + "mmHg,收缩压:" + DecimalUtil.toString(sbp) + "mmHg，正常血压区间！";
        	desc += "正常血压";
        }
        else
        {
            if (level == 1)
            {
                desc += "轻度高血压";
            }
            else if (level == 2)
            {
                desc += "中度高血压";
            }
            else
            {
                desc += "重度高血压";
            }
            /*if (dbpLevel > 0)
            {
                desc += "，其中舒张压为" + DecimalUtil.toString(dbp) + "mmHg，超出正常高值达" + DecimalUtil.toString(dbp - 89)
                        + "mmHg";
            }
            if (sbpLevel > 0)
            {
                desc += "，其中收缩压为" + DecimalUtil.toString(sbp) + "mmHg，超出正常高值达" + DecimalUtil.toString(sbp - 139)
                        + "mmHg";
            }*/
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 血糖类型,1：空腹血糖,2：餐前血糖,3：餐后血糖,4：服糖2小时血糖。
     * 
     * 糖尿病判断：
     *     1.有糖尿病的症状，任何时间的静脉血浆葡萄糖浓度≥ 11.1 mmol/L （ 200mg/dl ）。
     *     2.空腹静脉血浆葡萄糖浓度≥ 7.0 mmol/L （ 126mg/dl ）。
     *     3.糖耐量试验（ OGTT ）口服 75g 葡萄糖后 2 小时静脉血浆葡萄糖浓度≥ 11.1 mmol/L 。
     *     
     * 状况分类，level -1-正常 2-疑似糖尿病 3-疑似低血糖
     */
    public static AnalysisResult bloodSugar(int type, Double bloodSugar)
    {
        int level = 0;
        String desc = "";
        BloodSugar xtBean = new BloodSugar();
        // 餐前血糖当做空腹血糖
        int bloodSugarType = type;
        if (bloodSugarType == 1)
        {
            xtBean.setBloodSugarType(1);
            if (bloodSugar > 7.0)
            {
                level = 2;
                desc = "疑似糖尿病，空腹血糖值：" + bloodSugar + "mmol/L，超出正常值：" + DecimalUtil.toString(bloodSugar - 7) + "mmol/L!";
            }
            else if (bloodSugar < 2.8)
            {
                level = 3;
                desc = "疑似低血糖，空腹血糖值：" + bloodSugar + "mmol/L，低于正常值：" + DecimalUtil.toString(2.8 - bloodSugar)
                        + "mmol/L!";
            }
            else
            {
                level = -1;
                desc = "血糖值正常";
            }
        }
        else if (bloodSugarType == 2)
        {
            xtBean.setBloodSugarType(1);
            if (bloodSugar > 7.0)
            {
                level = 2;
                desc = "疑似糖尿病，餐前血糖值：" + bloodSugar + "mmol/L，超出正常值：" + DecimalUtil.toString(bloodSugar - 7) + "mmol/L!";
            }
            else if (bloodSugar < 2.8)
            {
                level = 3;
                desc = "疑似低血糖，餐前血糖值：" + bloodSugar + "mmol/L，低于正常值：" + DecimalUtil.toString(2.8 - bloodSugar)
                        + "mmol/L!";
            }
            else
            {
                level = -1;
                desc = "血糖值正常";
            }
        }
        else if (bloodSugarType == 3)
        {
            if (bloodSugar > 11.1)
            {
                level = 2;
                desc = "疑似糖尿病，餐后血糖值：" + bloodSugar + "mmol/L，超出正常值：" + DecimalUtil.toString(bloodSugar - 11.1)
                        + "mmol/L!";
            }
            else
            {
                level = -1;
                desc = "血糖值正常";
            }
        }
        else if (bloodSugarType == 4)
        {
            if (bloodSugar > 11.1)
            {
                level = 2;
                desc = "疑似糖尿病，服糖2小时后血糖值：" + bloodSugar + "mmol/L，超出正常值：" + DecimalUtil.toString(bloodSugar - 11.1)
                        + "mmol/L!";
            }
            else
            {
                level = -1;
                desc = "血糖值正常";
            }
        }
        else
        {
            level = -1;
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 当心率≥160bpm及心率<40bpm
     * @return
     */
    public static AnalysisResult pulse(Integer pulse)
    {
        Integer level = 0;
        String desc = "";
        if (pulse >= 40 && pulse < 160)
        {
            level = -1;
            desc = "脉搏值正常";
        }
        else if (pulse < 40)
        {
            level = 1;
            desc = "脉搏值偏低，低于正常低值（40）达" + (40 - pulse);
        }
        else
        {
            level = 2;
            desc = "脉搏值高于偏高";
            desc = "脉搏值偏高，高于正常高值（159）达" + (pulse - 159);
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 血氧，正常值[94%-99%],1表示低于94%
     * @return
     */
    public static AnalysisResult bloodOxygen(Integer bo)
    {
        Integer level = 0;
        String desc = "";
        if (bo >= 94)
        {
            level = -1;
            desc = "血氧值正常";
        }
        else
        {
            level = 1;
            desc = "脉搏值偏低，低于正常低值达" + (94 - bo) + "%";
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 体温，正常值[35.5℃-37.5℃],1表示体温值偏低，2表示体温值偏高
     * @return
     */
    public static AnalysisResult temperature(Double tempe)
    {
        Integer level = 0;
        String desc = "";
        if (tempe >= 35.5 && tempe <= 37.5)
        {
            level = -1;
            desc = "体温值正常";
        }
        else if (tempe < 35.5)
        {
            level = 1;
            desc = "体温氧值偏低，低于正常低值达" + DecimalUtil.toString(35.5 - tempe) + "℃";
        }
        else
        {
            level = 2;
            desc = "体温值偏高，高于正常低值达" + DecimalUtil.toString(tempe - 37.5) + "℃";
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 总胆固醇，正常值[2.8mmol/L-5.2mmol/L],1表示总胆固醇值偏低，2表示总胆固醇值偏高
     * @return
     */
    public static AnalysisResult totalCholesterol(Double totalCholesterol)
    {
        Integer level = 0;
        String desc = "";
        if (totalCholesterol >= 2.8 && totalCholesterol <= 5.2)
        {
            level = -1;
            desc = "总胆固醇值正常";
        }
        else if (totalCholesterol < 2.8)
        {
            level = 1;
            desc = "总胆固醇值偏低，低于正常低值达" + DecimalUtil.toString(2.8 - totalCholesterol) + "mmol/L";
        }
        else
        {
            level = 2;
            desc = "总胆固醇值偏高，高于正常低值达" + DecimalUtil.toString(totalCholesterol - 5.2) + "mmol/L";
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 尿酸，正常值[150umol/L-360umol/L],1表示尿酸值偏低，2表示尿酸值偏高
     * @return
     */
    public static AnalysisResult uricAcid(Double uricAcid)
    {
        Integer level = 0;
        String desc = "";
        if (uricAcid >= 150.0 && uricAcid <= 360.0)
        {
            level = -1;
            desc = "尿酸值正常";
        }
        else if (uricAcid < 150.0)
        {
            level = 1;
            desc = "尿酸值偏低，低于正常低值达" + DecimalUtil.toString(150.0 - uricAcid) + "umol/L";
        }
        else
        {
            level = 2;
            desc = "尿酸值偏高，高于正常低值达" + DecimalUtil.toString(uricAcid - 360.0) + "umol/L";
        }
        return new AnalysisResult(level, desc);
    }

    /**
     * 尿常规,1表示异常
     * 1、尿白细胞（LEU）→标准值或现象：-（此项目无病变的情况下一般不予筛查）
     * 2、亚硝酸盐（NIT）→标准值或现象：-（此项目普通情况下一般不予筛查）
     * 3、尿蛋白（PRO）→标准值或现象：+-（+表示轻度白色混浊，-表示清淡无混浊，两者或介于两者之间为正常）
     * 4、葡萄糖（GLU-U）→标准值或现象：-（蓝色溶液）
     * 5、酮体（KET）→标准值或现象：-（此项目一般不予筛查或者筛查时呈紫红色为正常）
     * 6、尿胆原（URO）→标准值或现象：-（此项目普通情况下一般不予筛查）
     * 7、胆红素（BIL）→标准值或现象：-（此项目无病变的情况下一般不予筛查）
     * 8、PH值（PH-U）[尿液酸碱度]→标准值或现象：5．5～7．5
     * 9、比重（SG）→标准值或现象：1．010～1．030
     * 10、隐血（BLU）→标准值或现象：-（无隐血）
     * 11、维生素C（VC）→标准值或现象：-（此项目普通情况下一般不予筛）
     * @return
     */
    public static AnalysisResult analysisUricAcid(AnalysisUricAcid analysisUricAcid)
    {
        Integer level = -1;
        String desc = "";
        boolean isEmpty = true;
        // 尿蛋白
        Integer pro = getFlagNum(analysisUricAcid.getPro());
        if (pro != null && pro.intValue() > 1)
        {
            if (!isEmpty)
            {
                desc += ",";
            }
            desc += "尿蛋白白色混浊";
            level = 1;
            isEmpty = false;
        }
        Double ph = analysisUricAcid.getPh();
        if (ph != null)
        {
            if (ph < 5.5)
            {
                if (!isEmpty)
                {
                    desc += ",";
                }
                desc += "PH值过低";
                level = 1;
                isEmpty = false;
            }
            else if (ph > 7.5)
            {
                if (!isEmpty)
                {
                    desc += ",";
                }
                desc += "PH值过高";
                level = 1;
                isEmpty = false;
            }
        }
        Double sg = analysisUricAcid.getSg();
        if (sg != null)
        {
            if (sg < 1.010)
            {
                if (!isEmpty)
                {
                    desc += ",";
                }
                desc += "比重值过低";
                level = 1;
                isEmpty = false;
            }
            else if (sg > 1.030)
            {
                if (!isEmpty)
                {
                    desc += ",";
                }
                desc += "比重值过高低";
                level = 1;
                isEmpty = false;
            }
        }
        // 隐血
        Integer bld = getFlagNum(analysisUricAcid.getBld());
        if (bld != null && bld > 0)
        {
            if (!isEmpty)
            {
                desc += ",";
            }
            desc += "有隐血";
            level = 1;
            isEmpty = false;
        }
        return new AnalysisResult(level, level < 0 ? "尿常规数据正常" : desc);
    }

    /**
     * 体脂率的判断条件，男女性别还不一样。
     * @author h
     * 2015.8.12
     * @return
     */
    public static AnalysisResult bodyFat(Double BFR, Integer sex)
    {
        Integer level = 0;
        String desc = "";
        /** 1:代表男 2：代表女
         * （17%~25%为女子的理想型体脂率）（10%~18%为男子的理想型体脂率）
         */
        if (sex == null)
        {
            if (BFR > 10 && BFR < 25)
            {
                level = -1;
                desc = "体脂率是在10%~25%之间！男女体脂率理想型的比率不同，请您在您的用户信息中添加性别信息。17%~25%为女子的理想型体脂率，10%~18%为男子的理想型体脂率。";
            }
            else if (BFR < 10)
            {
                level = 1;
                desc = "体脂率低于10%以下，男女体脂率理想型的比率不同，请您在您的用户信息中添加性别信息。17%~25%为女子的理想型体脂率，10%~18%为男子的理想型体脂率。";
            }
            else if (BFR > 20)
            {
                level = 2;
                desc = "体脂率超过25%以上，男女体脂率理想型的比率不同，请您在您的用户信息中添加性别信息。17%~25%为女子的理想型体脂率，10%~18%为男子的理想型体脂率。";
            }
        }
        else if (sex == 1)
        {
            if (BFR > 10 && BFR < 18)
            {
                level = -1;
                desc = "体脂率是理想型的，请继续保持！";
            }
            else if (BFR < 10)
            {
                level = 1;
                desc = "体脂率低于10%以下，您看起来偏瘦。如果想达到理想状态，希望你能改变饮食习惯。努力达到理想状态。";
            }
            else if (BFR > 18 && BFR < 30)
            {
                level = 2;
                desc = "体脂率在18-30%之间，您看起来虽然不胖，但其实很结实，虽然体重重并不完全等于肥胖，但你正一步步向胖胖族们靠近，赶快改变饮食方式与生活习惯，并开始做运动。";
            }
            else if (BFR > 30)
            {
                level = 3;
                desc = "体脂率超过30%以上，你体内已经囤积了许多多余的脂肪，再不采取行动改善的话，你会越来越胖，体脂肪率超过30%算是肥胖，不仅外表看起来臃肿，也容易患各种疾病，赶快下定决心开始减肥大战，重新做回低脂男子汉。";
            }
        }
        else if (sex == 2)
        {
            if (BFR > 17 && BFR < 25)
            {
                level = -1;
                desc = "体脂率是理想型的，请继续保持！";
            }
            else if (BFR < 17)
            {
                level = 1;
                desc = "体脂率有点低，太低会引起闭经、月经紊乱等症状，希望你能调整调整。详情请咨询医生。";
            }
            else if (BFR > 25 && BFR < 30)
            {
                level = 2;
                desc = "体脂率在25-30%之间，您看起来虽然不胖，但其实很结实，虽然体重重并不完全等于肥胖，但你正一步步向胖胖族们靠近，赶快改变饮食方式与生活习惯，并开始做运动。";
            }
            else if (BFR > 30)
            {
                level = 3;
                desc = "体脂率超过30%以上，你体内已经囤积了许多多余的脂肪，再不采取行动改善的话，你会越来越胖，体脂肪率超过30%算是肥胖，不仅外表看起来臃肿，也容易患各种疾病，赶快下定决心开始减肥大战，重新做回低脂美人。";
            }
        }
        return new AnalysisResult(level, desc);
    }
}