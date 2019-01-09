package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.msg.bean.Ask;
import com.yzxt.yh.module.msg.bean.AskCatalog;
import com.yzxt.yh.module.msg.bean.AskReply;
import com.yzxt.yh.module.msg.service.AskService;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.DoctorService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康问答Action类
 * @author f
 *
 */
public class AskTransAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(AskTransAction.class);

    private AskService askService;
    
    private DoctorService doctorService;

    public DoctorService getDoctorService() {
		return doctorService;
	}

	public void setDoctorService(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	public AskService getAskService()
    {
        return askService;
    }

    public void setAskService(AskService askService)
    {
        this.askService = askService;
    }

    /**
     * 获取健康问答科室子分类
     */
    public void listChildCatas()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            Timestamp lastSynTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            // 返回值对象
            JsonObject retJson = new JsonObject();
            Timestamp needSynTime = askService.getLatestCatalogSynTime();
            retJson.addProperty("updateTime", DateUtil.toSynTimeStr(needSynTime));
            // 如果客户端未指定时间或时间比服务器早，则要回传二级目录列表
            if (lastSynTime == null || needSynTime != null && needSynTime.getTime() > lastSynTime.getTime())
            {
                JsonArray catasJson = new JsonArray();
                List<AskCatalog> catas = askService.getCatalogsBySyn();
                if (catas != null && catas.size() > 0)
                {
                    for (AskCatalog cata : catas)
                    {
                        JsonObject cataJson = new JsonObject();
                        cataJson.addProperty("departmentId", cata.getId());
                        cataJson.addProperty("name", cata.getName());
                        cataJson.addProperty("enName", cata.getName());
                        cataJson.addProperty("level", cata.getLevel());
                        cataJson.addProperty("parentId", cata.getParentId());
                        catasJson.add(cataJson);
                    }
                }
                retJson.add("classifyDatas", catasJson);
            }
            super.write(ResultTran.STATE_OK, "获取健康问答目录成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取健康问答目录错误.", e);
            super.write("-1", "参数错误,请重新输入");
        }
    }

    /**
     * 获取健康问答问题列表
     */
    public void listAsks()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            JsonElement departmentIdEle = paramJson.get("departmentId");
            String topLevelAskCatalogId = departmentIdEle != null ? departmentIdEle.getAsString() : null;
            JsonElement subClassifyIdEle = paramJson.get("subClassifyId");
            String secondLevelAskCatalogId = subClassifyIdEle != null ? subClassifyIdEle.getAsString() : null;
            int pageSize = GsonUtil.toInt(paramJson.get("pageSize"), 10);
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            if (StringUtil.isEmpty(topLevelAskCatalogId) && StringUtil.isEmpty(secondLevelAskCatalogId))
            {
                super.write(ResultTran.STATE_ERROR, "请指定问题大类或小类.", null);
                return;
            }
            // 过滤条件
            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("user", super.getOperUser());
            filterMap.put("topLevelAskCatalogId", topLevelAskCatalogId);
            filterMap.put("secondLevelAskCatalogId", secondLevelAskCatalogId);
            // 拉取时间
            filterMap.put("sysTime", synTime);
            filterMap.put("maxSize", pageSize);
            List<Ask> asks = askService.queryAsksBySyn(filterMap);
            // 返回值
            JsonArray retJson = new JsonArray();
            if (asks != null && asks.size() > 0)
            {
                for (Ask ask : asks)
                {
                    JsonObject askJson = new JsonObject();
                    askJson.addProperty("id", ask.getId());
                    askJson.addProperty("departmentId", ask.getParentCatalogId());
                    askJson.addProperty("subClassifyId", ask.getCatalogId());
                    askJson.addProperty("title", ask.getTitle());
                    askJson.addProperty("content", ask.getContent());
                    // askJson.addProperty("imgs", null);
                    askJson.addProperty("sex", ask.getSex());
                    String birthdayStr = null;
                    if (ask.getBirthday() != null)
                    {
                        birthdayStr = DateUtil.toSynTimeStr(new Timestamp(ask.getBirthday().getTime()));
                    }
                    askJson.addProperty("birdthday", birthdayStr);
                    askJson.addProperty("userId", ask.getUpdateBy());
                    askJson.addProperty("time", DateUtil.toSynTimeStr(ask.getUpdateTime()));
                    askJson.addProperty("lastSynTime", DateUtil.toSynTimeStr(ask.getUpdateTime()));
                    // 医生的一条回答信息
                    AskReply reply = ask.getAskReply();
                    if (reply != null)
                    {
                        String replyUserType = null;
                        if (Constant.USER_TYPE_DOCTOR.equals(reply.getUpdateByUserType()))
                        {
                            replyUserType = "typeDoc";
                        }
                        else if (Constant.USER_TYPE_CUSTOMER.equals(reply.getUpdateByUserType()))
                        {
                            replyUserType = "typeUser";
                        }
                        JsonObject replyDataJson = new JsonObject();
                        replyDataJson.addProperty("id", reply.getId());
                        replyDataJson.addProperty("content", reply.getContent());
                        replyDataJson.addProperty("replyUserId", reply.getUpdateBy());
                        replyDataJson.addProperty("replyName", reply.getUpdateByName());
                        // replyDataJson.addProperty("replyHead", null);
                        replyDataJson.addProperty("replyUserType", replyUserType);
                        // replyDataJson.addProperty("replyLevel", null);
                        replyDataJson.addProperty("time", DateUtil.toSynTimeStr(reply.getUpdateTime()));
                        askJson.add("replyData", replyDataJson);
                    }
                    retJson.add(askJson);
                }
            }
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端获取问题列表失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 获取健康问答回复数据列表
     */
    public void listReplys()
    {
        try
        {
            JsonObject paramJson = getParams();
            JsonArray retJson = new JsonArray();
            JsonObject replyJson;
            if (paramJson == null)
            {
                return;
            }
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String doctorId = GsonUtil.toString(paramJson.get("doctorId"));
            String askId = GsonUtil.toString(paramJson.get("askId"));
            /*if (StringUtil.isEmpty(askId))
            {
                super.write(ResultTran.STATE_ERROR, "问题ID不能为空.", null);
                return;
            }*/
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            int pageSize = GsonUtil.toInt(paramJson.get("pageSize"), 10);
            // 过滤条件
            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("custId", custId);
            filterMap.put("doctorId", doctorId);
            // 拉取时间
            filterMap.put("sysTime", synTime);
            filterMap.put("maxSize", pageSize);
           List<Ask> askList = askService.getCustId(custId,doctorId);
           if(askList!=null){
        	   for (Ask ask : askList) {
        		   if(ask.getType()==0){
        			   replyJson = new JsonObject();
        			   replyJson.addProperty("type", ask.getType());
        			   replyJson.addProperty("userName", ask.getCreateBy());
        			   replyJson.addProperty("userContent", ask.getTitle());
                       replyJson.addProperty("userTime",ask.getCreateTime().toString());
                       retJson.add(replyJson);
        		   }else{
        			   List<AskReply> replys = askService.queryReplysBySyn(filterMap);
        	            // 返回值
        	            if (replys != null && replys.size() > 0)
        	            {
        	                for (AskReply reply : replys)
        	                {
        	                    String replyUserType = null;
        	                    if (Constant.USER_TYPE_DOCTOR.equals(reply.getUpdateByUserType()))
        	                    {
        	                        replyUserType = "typeDoc";
        	                    }
        	                    else if (Constant.USER_TYPE_CUSTOMER.equals(reply.getUpdateByUserType()))
        	                    {
        	                        replyUserType = "typeUser";
        	                    }
        	                    replyJson = new JsonObject();
        	                    replyJson.addProperty("type", reply.getType());
        	                    replyJson.addProperty("docontent", reply.getContent());
        	                    replyJson.addProperty("replyUserId", reply.getUpdateBy());
        	                    replyJson.addProperty("replyName", reply.getUpdateByName());
        	                    // replyJson.addProperty("replyHead", null);
        	                    replyJson.addProperty("replyUserType", replyUserType);
        	                    replyJson.addProperty("userName", reply.getCreateBys());
        	                    replyJson.addProperty("userContent", reply.getTitle());
        	                    replyJson.addProperty("userTime", DateUtil.toSynTimeStr(reply.getCreateTimes()));
        	                    // replyJson.addProperty("replyLevel", null);
        	                    replyJson.addProperty("docTime", DateUtil.toSynTimeStr(reply.getUpdateTime()));
        	                    retJson.add(replyJson);
        	                }
        	            }
        		   }
        		   
			}
        	   super.write(ResultTran.STATE_OK, "成功", retJson);
           }
           
        }
        catch (Exception e)
        {
            logger.error("客户端获取问题回复列表失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 发表问题
     */
    public void saveAsk()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            // 解析参数，目前客户端只支持添加，不支持修改
            User user = super.getOperUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String askId = null;
            String parentCatalogId = GsonUtil.toString(paramJson.get("departmentId"));
            String catalogId = GsonUtil.toString(paramJson.get("subClassifyId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String title = GsonUtil.toString(paramJson.get("title"));
            String content = GsonUtil.toString(paramJson.get("content"));
            String doctorId = GsonUtil.toString(paramJson.get("doctorId"));
            Integer sex = GsonUtil.toInteger(paramJson.get("sex"));
            Integer state = GsonUtil.toInteger(paramJson.get("state"));
            // 未指定性别，则性别选项值为未知
            sex = sex != null ? sex : 0;
            Date birthday = null;
            String birthdayStr = GsonUtil.toString(paramJson.get("birdthday"));
            if (StringUtil.isNotEmpty(birthdayStr))
            {
                birthday = DateUtil.fromJsonStr(birthdayStr);
            }
            // 验证数据
           /* if (StringUtil.isEmpty(catalogId))
            {
                super.write(ResultTran.STATE_ERROR, "请指定问题类别.", null);
                return;
            }*/
            if (StringUtil.isEmpty(title))
            {
                super.write(ResultTran.STATE_ERROR, "标题不能为空.", null);
                return;
            }
           /* if (StringUtil.isEmpty(content))
            {
                super.write(ResultTran.STATE_ERROR, "问题内容不能为空.", null);
                return;
            }*/
            // 装配数据
            String summary = content;
            if (summary.length() > 100)
            {
                summary = summary.substring(0, 97) + "...";
            }
            Ask ask = new Ask();
            ask.setId(askId);
            ask.setParentCatalogId(parentCatalogId);
            ask.setCatalogId(catalogId);
            ask.setTitle(title);
            ask.setType(0);
            ask.setSummary(summary);
            ask.setContent(content);
            ask.setSex(sex);
            ask.setState(state);
            ask.setDoctorId(doctorId);
            ask.setBirthday(birthday);
            // 保存
            if (StringUtil.isEmpty(askId))
            {
                ask.setCreateBy(user.getName());
                //ask.setUpdateBy(user.getId());
                ask.setUpdateBy(custId);
                Result r = askService.add(ask);
                askId = (String) r.getData();
                
            }
            else
            {
                ask.setUpdateBy(user.getId());
                askService.update(ask);
            }
            JsonObject retJson = new JsonObject();
            retJson.addProperty("askId", askId);
            retJson.addProperty("custId", ask.getUpdateBy());
            retJson.addProperty("time", DateUtil.toSynTimeStr(now));
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }  
    }

    /**
     * 发表回复
     */
    public void saveReply()
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            // 解析参数，目前客户端只支持添加，不支持修改
            User user = super.getOperUser();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String askReplyId = null;
            String askId = GsonUtil.toString(paramJson.get("problemId"));
            String content = GsonUtil.toString(paramJson.get("content"));
            // 验证数据
            if (StringUtil.isEmpty(askId))
            {
                super.write(ResultTran.STATE_ERROR, "请指定问题ID.", null);
                return;
            }
            if (StringUtil.isEmpty(content))
            {
                super.write(ResultTran.STATE_ERROR, "回复内容不能为空.", null);
                return;
            }
            // 装配数据
            AskReply askReply = new AskReply();
            askReply.setId(askReplyId);
            askReply.setAskId(askId);
            askReply.setContent(content);
            // 保存
            if (StringUtil.isEmpty(askReplyId))
            {
                askReply.setCreateBy(user.getId());
                askReply.setUpdateBy(user.getId());
                Result r = askService.addAskReply(askReply);
                askId = (String) r.getData();
            }
            else
            {
                askReply.setUpdateBy(user.getId());
                askService.updateAskReply(askReply);
            }
            JsonObject retJson = new JsonObject();
            retJson.addProperty("id", askId);
            retJson.addProperty("time", DateUtil.toSynTimeStr(now));
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端保存问题回复失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }
    
    /**
     * 客户查看接口
     */
    
    public void ready(){
   	 try
        {
            JsonObject obj = super.getParams();
            String doctorId = GsonUtil.toString(obj.get("doctorId"));
            Doctor doctor = doctorService.getDoctor(doctorId);
            doctor.setState(2);
            doctorService.updatePre(doctor);
            super.write(ResultTran.STATE_OK,"成功");
        }
        catch (Exception e)
        {
            logger.error("上传错误。", e);
            super.write(ResultTran.STATE_ERROR, "上传错误。");
        }
   } 
}
