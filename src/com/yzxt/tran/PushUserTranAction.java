package com.yzxt.tran;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.module.sys.bean.PushUser;
import com.yzxt.yh.module.sys.service.PushUserService;
import com.yzxt.yh.util.StringUtil;

/**
 * 推送用户映射信息客户端接口类
 *
 */
public class PushUserTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(PushUserTranAction.class);

    private PushUserService pushUserService;

    public PushUserService getPushUserService()
    {
        return pushUserService;
    }

    public void setPushUserService(PushUserService pushUserService)
    {
        this.pushUserService = pushUserService;
    }

    /**
     * 保存用户推送映射信息
     */
    public void save()
    {
        try
        {
            JsonObject obj = getParams();
            // 目前不做用户登录校验，参数信息可能为空
            if (obj == null)
            {
                return;
            }
            String userId = GsonUtil.toString(obj.get("userId"));
            String pushChannelId = GsonUtil.toString(obj.get("pushChannelId"));
            String pushUserId = GsonUtil.toString(obj.get("pushUserId"));
            Integer deviceType = GsonUtil.toInteger(obj.get("deviceType"));
            if (StringUtil.isNotEmpty(userId) && StringUtil.isNotEmpty(pushChannelId))
            {
                PushUser pushUser = new PushUser();
                pushUser.setUserId(userId);
                pushUser.setPushChannelId(pushChannelId);
                pushUser.setPushUserId(pushUserId);
                pushUser.setDeviceType(deviceType != null ? deviceType : ConstDevice.DEVICE_TYPE_ANDROID);
                pushUserService.save(pushUser);
            }
            super.write(ResultTran.STATE_OK, "保存用户推送配置信息成功", null);
        }
        catch (Exception e)
        {
            logger.error("客户端保存用户推送配置信息失败.", e);
            super.write(ResultTran.STATE_ERROR, "保存用户推送配置信息错误", null);
        }
    }

    /**
     * 删除用户推送映射信息
     * @throws Exception
     */
    public void del()
    {
        try
        {
            JsonObject obj = getParams();
            // 目前不做用户登录校验，参数信息可能为空
            if (obj == null)
            {
                return;
            }
            String userId = GsonUtil.toString(obj.get("userId"));
            String deviceTypeStr = GsonUtil.toString(obj.get("deviceType"));
            String pushChannelId = GsonUtil.toString(obj.get("pushChannelId"));
            if (StringUtil.isNotEmpty(userId) && StringUtil.isNotEmpty(deviceTypeStr))
            {
                PushUser pushUser = new PushUser();
                pushUser.setUserId(userId);
                pushUser.setPushChannelId(pushChannelId);
                pushUser.setDeviceType(Integer.valueOf(deviceTypeStr));
                pushUserService.delete(pushUser);
            }
            super.write(ResultTran.STATE_OK, "删除用户推送配置信息成功", null);
        }
        catch (Exception e)
        {
            logger.error("客户端删除用户推送配置信息失败.", e);
            super.write(ResultTran.STATE_ERROR, "删除用户推送配置信息错误", null);
        }
    }

}
