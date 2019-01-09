package com.yzxt.yh.constant;

/**
 * 系统常量类
 * 
 */
public class Constant
{

    // 用户类型：管理员
    public static final Integer USER_TYPE_ADMIN = 1;

    // 用户类型：医生
    public static final Integer USER_TYPE_DOCTOR = 2;

    // 用户类型：居民
    public static final Integer USER_TYPE_CUSTOMER = 3;

    // 非会员
    public static final Integer CUSTOMER_MEMBER_STATUS_NO = 0;

    // 是会员
    public static final Integer CUSTOMER_MEMBER_STATUS_YES = 1;

    // 用户状态：有效
    public static final Integer USER_STATE_EFFECTIVE = 1;

    // 用户状态：冻结
    public static final Integer USER_STATE_FROZEN = 2;

    // 用户状态：有效
    public static final Integer USER_STATE_DELETED = 0;

    // 默认密码
    public static final String USER_DEFAULT_PASSWROD = "123456";

    // 通用状态，有效
    public static final Integer COMMON_STATE_ENABLE = 1;

    // 通用状态，无效
    public static final Integer COMMON_STATE_DISABLED = 0;

    // 数据检测类型，机器检测的数据
    public static final Integer DATA_CHECK_TYPE_DIVICE = 1;

    // 数据检测类型，手动录入的数据
    public static final Integer DATA_CHECK_TYPE_MANUAL = 2;

    // 数据检测类型，设备直接上传的数据
    public static final Integer DATA_CHECK_TYPE_DIRECT = 6;

    // 数据检测类型，个人版本机器检测的数据
    public static final Integer DATA_CHECK_TYPE_PERSONAL_DIVICE = 11;

    // 数据检测类型，个人版本手动输入的数据
    public static final Integer DATA_CHECK_TYPE_PERSONAL_MANUAL = 12;

    // 平板登录平台会话过期天数
    public static final int CLIENT_LOGIN_EXPIRY_DAYS = 30;

    // 字典编码：民族
    public static final String DICT_CODE_NATIONAL = "national";

    // 血压分析报告类型：血压
    public static final Integer ANALYSIS_REPORT_TYPE_BLOOD_PRESSURE = 1;

    // 血压分析报告类型：血糖
    public static final Integer ANALYSIS_REPORT_TYPE_BLOOD_SUGAR = 2;

    // 栏目中的热点ID
    public static final String INFO_COLUMN_HOT = "_rd";

    // 栏目中的专题ID
    public static final String INFO_COLUMN_TOPIC = "_zt";

    // 栏目我的发布栏目
    public static final String INFO_COLUMN_MY_PUBLISH = "_myPub";

    // 资讯目录类型，栏目
    public static final Integer INFO_CATALOG_TYPE_COLUMN = 1;

    // 资讯目录类型，专题
    public static final Integer INFO_CATALOG_TYPE_TOPIC = 2;

    // 资讯目录状态，使用中
    public static final Integer INFO_CATALOG_STATE_IN_USING = 1;

    // 资讯目录状态，已停用
    public static final Integer INFO_CATALOG_STATE_STOPPED = 2;

    // 资讯目录状态，使用中
    public static final Integer INFO_CATALOG_STATE_DISABLED = 0;

    // 资讯目录属于预定义目录
    public static final Integer INFO_CATALOG_IS_PREDEFINED = 1;

    // 资讯目录属于非预定义目录
    public static final Integer INFO_CATALOG_IS_NOT_PREDEFINED = 2;

    // 资讯级别：置顶
    public static final Integer INFO_LEVEL_TOP = 1;

    // 资讯级别：普通
    public static final Integer INFO_LEVEL_COMMON = 2;

    // 健康知识状态，使用中
    public static final Integer KNOWLEDGE_STATE_IN_USING = 1;

    // 健康知识状态，停用中
    public static final Integer KNOWLEDGE_STATE_STOPPED = 2;

    // 健康问答目录等级1
    public static final Integer ASK_CATALOG_LEVEL_ONE = 1;

    // 健康问答目录等级2
    public static final Integer ASK_CATALOG_LEVEL_TWO = 2;

    // 健康问答状态，使用中
    public static final Integer ASK_CATALOG_STATE_IN_USING = 1;

    // 健康问答状态，停用中
    public static final Integer ASK_CATALOG_STATE_STOPPED = 2;

    // 健康问答回复状态，1：待回复
    public static final Integer ASK_STATE_WAIT_REPLY = 1;

    // 健康问答回复状态，2：已关闭
    public static final Integer ASK_STATE_CLOSED = 2;

    // 我的提问
    public static final String ASK_CATALOG_MY_PUBLISH = "_myPub";

    // 食物目录等级1
    public static final Integer FOOD_CATALOG_LEVEL_ONE = 1;

    // 食物目录等级2
    public static final Integer FOOD_CATALOG_LEVEL_TWO = 2;

    // 食物有效
    public static final Integer FOOD_STATE_ENABLE = 1;

    // 食物无效
    public static final Integer FOOD_STATE_DISABLE = 0;

    // 挂号就诊卡类型,身份证
    public static final Integer CLINIC_CARD_TYPE_ID_CARD = 1;

    // 挂号就诊卡类型,社保卡
    public static final Integer CLINIC_CARD_TYPE_SOCIAL_SECURITY_CARD = 2;

    // 挂号状态：待就诊
    public static final Integer HOSPITAL_REGISTER_STATE_WAIT_DIAGNOSIS = 1;

    // 挂号状态：已取消
    public static final Integer HOSPITAL_REGISTER_STATE_CANCELED = 2;

    // 挂号状态：已就诊
    public static final Integer HOSPITAL_REGISTER_STATE_HAVE_DIAGNOSIS = 3;

    // 挂号状态：未就诊
    public static final Integer HOSPITAL_REGISTER_STATE_NOT_DIAGNOSIS = 4;

    // 健康计划类型，血压
    public static final Integer HEALTHY_PLAN_TYPE_BLOOD_PRESSURE = 1;

    // 健康计划类型，血糖
    public static final Integer HEALTHY_PLAN_TYPE_BLOOD_SUGAR = 2;

    // 健康计划类型，体重
    public static final Integer HEALTHY_PLAN_TYPE_WEIGHT = 3;

    // 健康告警类型，血压
    public static final Integer HEALTHY_WARNING_TYPE_XY = 1;

    // 健康告警类型，血糖
    public static final Integer HEALTHY_WARNING_TYPE_XT = 2;

    // 健康告警状态，未阅
    public static final Integer HEALTHY_WARNING_READ_STATE_NOTREAD = 1;

    // 健康告警状态，已阅
    public static final Integer HEALTHY_WARNING_READ_STATE_READ = 2;

    // 健康告警级别，一级高血压
    public static final Integer HEALTHY_WARNING_LEVEL_XY_ONE = 1;

    // 健康告警级别，二级高血压
    public static final Integer HEALTHY_WARNING_LEVEL_XY_TWO = 2;

    // 健康告警级别，三级高血压
    public static final Integer HEALTHY_WARNING_LEVEL_XY_THREE = 3;

    // 健康告警级别，疑似糖尿病
    public static final Integer HEALTHY_WARNING_LEVEL_XT = 4;

    // 门诊记录状态，1：草稿
    public static final Integer OUTPATIENT_STATE_DRAFT = 1;

    // 门诊记录状态，2：提交待交费
    public static final Integer OUTPATIENT_STATE_WAIT_CHARGE = 2;

    // 门诊记录状态，3：已交费
    public static final Integer OUTPATIENT_STATE_FINISH = 3;

    // 门诊费用：普通
    public static final Integer OUTPATIENT_FEE_TYPE_COMMON = 1;

    // 门诊费用：汇总
    public static final Integer OUTPATIENT_FEE_TYPE_SUM = 2;

    // 门诊收费
    public static final Integer OUTPATIENT_CHARGE_TYPE_PAY = 1;

    // 门诊退费
    public static final Integer OUTPATIENT_CHARGE_TYPE_REFUND = 2;

    // 告警处理状态 1 未处理
    public static final Integer WARNING_STATE_NOT_DEAL = 1;

    // 告警处理状态 2 已处理
    public static final Integer WARNING_STATE_DEAL = 2;

    // 咨询状态 1 未回复
    public static final Integer CONSULT_STATE_NOT_REPLY = 1;

    // 咨询状态 2 已回复
    public static final Integer CONSULT_STATE_REPLY = 2;

    // 家庭圈   申请状态：1：申请中
    public static final Integer FAMILY_STATE_APPLY = 1;

    // 家庭圈  2：申请成功
    public static final Integer FAMILY_STATE_APPLY_SUCCESS = 2;

    // 家庭圈  3：申请失败
    public static final Integer FAMILY_STATE_APPLY_FAIL = 3;

    // 登录方式：1：平台
    public static final Integer LOGIN_TYPE_WEB = 1;

    // 登录方式：2：客户端
    public static final Integer LOGIN_TYPE_CLIENT = 2;

    // 定位方式
    public static final String POSITION_MODE_GPS = "GPS";

    // 定位方式
    public static final String POSITION_MODE_LBS = "LBS";

    // 腕表设置服务下发状态-待发送
    public static final int WB_SERVICE_FOR_SEND = 1;

    //订单状态-待付款
    public static final int ORDER_TYPE_NO = 0;
    
    //订单状态-已付款
    public static final int ORDER_TYPE_YES = 1;
    

}
