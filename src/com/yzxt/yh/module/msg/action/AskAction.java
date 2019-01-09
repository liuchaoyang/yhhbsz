package com.yzxt.yh.module.msg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.Ask;
import com.yzxt.yh.module.msg.bean.AskCatalog;
import com.yzxt.yh.module.msg.bean.AskReply;
import com.yzxt.yh.module.msg.service.AskService;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.DoctorService;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import common.Logger;

/**
 * 健康问答Action类
 * @author f
 *
 */
public class AskAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    
    private static String masterSecret = "a057d6dddc5bd011757dafff";
    private static String appKey = "75cf0844e5ee2b0a456e6f1a";
    private static final String ALERT = "医生回复";

    private Logger logger = Logger.getLogger(AskAction.class);

    private AskService askService;
    private FamilyService familyService;
    
    private DoctorService doctorService;

    public DoctorService getDoctorService() {
		return doctorService;
	}

	public void setDoctorService(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	public FamilyService getFamilyService() {
		return familyService;
	}

	public void setFamilyService(FamilyService familyService) {
		this.familyService = familyService;
	}

	private Ask ask;

    private AskReply askReply;

    public AskService getAskService()
    {
        return askService;
    }

    public void setAskService(AskService askService)
    {
        this.askService = askService;
    }

    public Ask getAsk()
    {
        return ask;
    }

    public void setAsk(Ask ask)
    {
        this.ask = ask;
    }

    public AskReply getAskReply()
    {
        return askReply;
    }

    public void setAskReply(AskReply askReply)
    {
        this.askReply = askReply;
    }

    /**
     * 显示子目录JSON数据
     */
    public void listChildrenAskCatalogsByJson()
    {
        String catalogId = request.getParameter("catalogId");
        List<AskCatalog> children = null;
        if (StringUtil.isNotEmpty(catalogId))
        {
            children = askService.getChildrenAskCatalogs(catalogId);
        }
        JsonArray retJson = new JsonArray();
        if (children != null && children.size() > 0)
        {
            for (AskCatalog cata : children)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", cata.getId());
                jsonObj.addProperty("name", cata.getName());
                retJson.add(jsonObj);
            }
        }
        super.write(retJson);
    }

    public String toEdit() throws Exception
    {
        String askId = request.getParameter("id");
        List<AskCatalog> topLevelAskCatalogs = askService.getTopLevelAskCatalogs();
        List<AskCatalog> secondLevelAskCatalogs = null;
        // 没有指定ID，新建资讯
        if (StringUtil.isEmpty(askId))
        {
            ask = new Ask();
            request.setAttribute("update", false);
        }
        else
        {
            ask = askService.load(askId);
            if (ask == null)
            {
                request.setAttribute("msg", "提问修改出错，此提问已不存在！");
                return "editError";
            }
            if (StringUtil.isNotEmpty(ask.getParentCatalogId()))
            {
                secondLevelAskCatalogs = askService.getChildrenAskCatalogs(ask.getParentCatalogId());
            }
            request.setAttribute("update", true);
        }
        if (secondLevelAskCatalogs == null)
        {
            secondLevelAskCatalogs = new ArrayList<AskCatalog>();
        }
        request.setAttribute("ask", ask);
        request.setAttribute("topLevelAskCatalogs", topLevelAskCatalogs);
        request.setAttribute("secondLevelAskCatalogs", secondLevelAskCatalogs);
        return "edit";
    }

    /**
     * 添加问答信息
     * @return
     * @throws Exception
     */
    public void add()
    {
        int state = 0;
        String msg = "";
        String newAskId = null;
        try
        {
            // 用户
            User user = (User) getCurrentUser();
            ask.setCreateBy(user.getId());
            ask.setUpdateBy(user.getId());
            Result r = askService.add(ask);
            newAskId = (String) r.getData();
            state = 1;
        }
        catch (Exception e)
        {
            state = -1;
            msg = "提问失败！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        result.setData(newAskId);
        super.write(result);
    }

    /**
     * 更新问答信息
     * @return
     * @throws Exception
     */
    public void update()
    {
        int state = 0;
        String msg = "";
        try
        {
            // 用户
            User user = (User) getCurrentUser();
            ask.setUpdateBy(user.getId());
            // 更新
            Result r = askService.update(ask);
            state = r.getState();
            msg = r.getMsg();
        }
        catch (Exception e)
        {
            state = -1;
            msg = "修改提问出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setData(ask.getId());
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 删除问答信息
     * @return
     * @throws Exception
     */
    public void delete()
    {
        int state = 0;
        String msg = "";
        try
        {
            String id = request.getParameter("id");
            if (StringUtil.isEmpty(id))
            {
                state = -1;
                msg = "请指定提问ID！";
            }
            // 验证通过，开始删除数据
            if (StringUtil.isEmpty(msg))
            {
                askService.delete(id);
                state = 1;
            }
        }
        catch (Exception e)
        {
            state = -1;
            msg = "删除出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 查看问答信息
     * @return
     * @throws Exception
     */
    public String view() throws Exception
    {
        User user = (User) getCurrentUser();
        try
        {
            String id = request.getParameter("id");
            Ask ask = askService.load(id);
            if (ask == null)
            {
                request.setAttribute("msg", "出错了，此提问已被删除！");
                return "editError";
            }
            // 年龄
            String ageStr = "";
            if (ask.getBirthday() != null)
            {
                ageStr = DateUtil.getAge(ask.getBirthday(), null) + "";
            }
            ask.setContent(parseContent(ask.getContent()));
            // 加载答复列表
            List<AskReply> askReplys = askService.getReplysByAsk(id);
            if (askReplys != null && askReplys.size() > 0)
            {
                for (AskReply askReply : askReplys)
                {
                    askReply.setContent(parseContent(askReply.getContent()));
                }
            }
            request.setAttribute("ask", ask);
            request.setAttribute("ageStr", ageStr);
            request.setAttribute("askReplys", askReplys);
            boolean replyAble = Constant.USER_TYPE_DOCTOR.equals(user.getType());
            request.setAttribute("replyAble", replyAble);
            request.setAttribute("needReply", replyAble ? "Y".equals(request.getParameter("needReply")) : false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return "view";
    }

    /**
     * 显示问答列表信息
     * @return
     * @throws Exception
     */
    public String listAsks() throws Exception
    {
        // 分页信息处理
        int page = super.getPage();
        int pageSize = super.getRows();
        if (page == 0)
        {
            page = 1;
        }
        if (pageSize == 0)
        {
            pageSize = 10;
        }
        // 所有可用类别
        List<AskCatalog> topLevelAskCatalogs = askService.getTopLevelAskCatalogs();
        // 子类别
        List<AskCatalog> secondLevelAskCatalogs = null;
        // 需要显示的目录ID和级别
        String topLevelCatalogId = null;
        String secondLevelCatalogId = null;
        // 获取参数中的目录ID
        String askCatalogId = request.getParameter("askCatalogId");
        // 如果不是特殊目录
        if (!Constant.ASK_CATALOG_MY_PUBLISH.equals(askCatalogId))
        {
            AskCatalog askCatalog = null;
            if (StringUtil.isNotEmpty(askCatalogId))
            {
                askCatalog = askService.getAskCatalog(askCatalogId);
            }
            // 判断选中的类别
            if (askCatalog != null && Constant.ASK_CATALOG_STATE_IN_USING.equals(askCatalog.getState()))
            {
                if (Constant.ASK_CATALOG_LEVEL_TWO.equals(askCatalog.getLevel()))
                {
                    topLevelCatalogId = askCatalog.getParentId();
                    secondLevelCatalogId = askCatalog.getId();
                }
                else
                {
                    topLevelCatalogId = askCatalog.getId();
                    secondLevelCatalogId = null;
                }
            }
            else
            {
                topLevelCatalogId = topLevelAskCatalogs.size() > 0 ? topLevelAskCatalogs.get(0).getId() : null;
                secondLevelCatalogId = null;
            }
            // 展示选中一层目录的子目录
            if (StringUtil.isNotEmpty(topLevelCatalogId))
            {
                secondLevelAskCatalogs = askService.getChildrenAskCatalogs(topLevelCatalogId);
            }
        }
        else
        {
            topLevelCatalogId = askCatalogId;
        }
        // 查询选中目录下的问答列表
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("topLevelCatalogId", topLevelCatalogId);
        filter.put("secondLevelCatalogId", secondLevelCatalogId);
        filter.put("user", super.getCurrentUser());
        PageModel<Ask> pageModel = askService.queryAskByPage(filter, page, pageSize);
        // 设置返回参数
        request.setAttribute("topLevelCatalogId", topLevelCatalogId);
        request.setAttribute("secondLevelCatalogId", secondLevelCatalogId);
        request.setAttribute("topLevelAskCatalogs", topLevelAskCatalogs);
        request.setAttribute("secondLevelAskCatalogs", secondLevelAskCatalogs);
        request.setAttribute("pageModel", pageModel);
        return "list";
    }

    /**
     * 查询问题
     */
    public void queryDocHomeAsk()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Ask> pageModel = askService.queryDocHomeAsk(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询会员问题错误。", e);
        }
    }

    /**
     * 添加问答回复信息
     * @return
     * @throws Exception
     */
    public void addAskReply() throws Exception
    {
        int state = 0;
        String msg = "";
        String newAskId = null;
        try
        {
            // 用户
            User user = (User) getCurrentUser();
            askReply.setCreateBy(user.getId());
            askReply.setUpdateBy(user.getId());
            Result r = askService.addAskReply(askReply);
            newAskId = (String) r.getData();
            state = r.getState();
        }
        catch (Exception e)
        {
            state = -1;
            msg = "回复失败！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        result.setData(newAskId);
        Ask asks = askService.get(askReply.getAskId());
        askService.update(asks);
        if(asks.getState()==null){
        	this.push();
        }else{
    	  Doctor doctor = doctorService.getDoctor(asks.getDoctorId());
          doctor.setState(1);
          doctorService.updatePre(doctor);
        }
       super.write(result);
    }

    /**
     * 更新问答回复信息
     * @return
     * @throws Exception
     */
    public void updateAskReply()
    {
        int state = 0;
        String msg = "";
        try
        {
            // 用户
            User user = (User) getCurrentUser();
            askReply.setUpdateBy(user.getId());
            // 更新
            Result r = askService.updateAskReply(askReply);
            state = r.getState();
            msg = r.getMsg();
            state = r.getState();
        }
        catch (Exception e)
        {
            state = -1;
            msg = "回复出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 删除问答回复信息
     * @return
     * @throws Exception
     */
    public void deleteAskReply()
    {
        int state = 0;
        String msg = "";
        try
        {
            String askReplyId = request.getParameter("askReplyId");
            if (StringUtil.isEmpty(askReplyId))
            {
                state = -1;
                msg = "请指定回复ID！";
            }
            // 验证通过，开始删除数据
            if (StringUtil.isEmpty(msg))
            {
                askService.deleteAskReply(askReplyId);
                state = 1;
            }
        }
        catch (Exception e)
        {
            state = -1;
            msg = "删除回复出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    private String parseContent(String content)
    {
        StringBuilder html = new StringBuilder();
        if (StringUtil.isEmpty(content))
        {
            return html.toString();
        }
        Scanner scan = new Scanner(content);
        boolean isFirstLine = true;
        while (scan.hasNextLine())
        {
            if (!isFirstLine)
            {
                html.append("<br />");
            }
            String line = scan.nextLine();
            html.append(line);
            isFirstLine = false;
        }
        return html.toString();
    }
    
    public PushResult push() throws APIConnectionException, APIRequestException{
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0, null,clientConfig);
        //PushPayload payload = buildPushObject_android_tag_alertWithTitle();
        PushPayload payload = buildPushObject_all_alias_alert();
        return jpushClient.sendPush(payload);
    }
    
    public  PushPayload buildPushObject_all_alias_alert() {
    	User user  = (User) super.getCurrentUser();
    	String askId = request.getParameter("id");
    	Ask ask = askService.load(askId);
    	CustFamilyAudit familyUser = familyService.getFamilyUser(ask.getUpdateBy());
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(familyUser.getCustId()))
                .setNotification(Notification.alert(ALERT))
                .setMessage(Message.newBuilder().setMsgContent(ALERT).addExtra("doctorId", user.getId()).build())
                .build();
    }
    
  
}
