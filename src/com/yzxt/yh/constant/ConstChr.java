package com.yzxt.yh.constant;

/**
 * 随访常量类
 * 
 */
public class ConstChr
{

    // 随访类型：1：血压
    public static final Integer CHR_TYPE_XY = 1;
    
    // 随访类型：2：血糖
    public static final Integer CHR_TYPE_XT = 2;
    
    // 随访类型:3：心脑血管
    public static final Integer CHR_TYPE_XNXG = 3;
    
    // 危险分级：-1：正常 
    public static final Integer CHR_DANGER_GRADE_NORMAL = -1;
    
    // 危险分级:1：低危险
    public static final Integer CHR_DANGER_GRADE_LOW = 1;
    
    // 危险分级：2：中
    public static final Integer CHR_DANGER_GRADE_MIDDER = 2;
    
    // 危险分级:3：高
    public static final Integer CHR_DANGER_GRADE_HIGH = 3;
    
    // 危险分级:4：极高
    public static final Integer CHR_DANGER_GRADE_VERYHIGH = 4;
    
    // 是否随访：1：未随访
    public static final Integer CHR_ISHANDLED_NOT = 1;
    
    // 是否随访：2：已随访
    public static final Integer CHR_ISHANDLED_YES = 2;

    // 是否有数据：1：存在
    public static final Integer CHR_STATUS_YES = 1;
    
    // 是否有数据：0:删除
    public static final Integer CHR_STATUS_NOT = 0;
    
    // 随访分类:1：控制满意
    public static final Integer CHR_FLUP_GRADE_SATISFY = 1;
    
    // 随访分类：2：控制不满意
    public static final Integer CHR_FLUP_GRADE_NOT_SATISFY = 2;
    
    // 随访分类:3：不良反应
    public static final Integer CHR_FLUP_GRADE_BAD = 3;
    
    // 随访分类:4：并发症
    public static final Integer CHR_FLUP_GRADE_COMPLICE = 4;
}
