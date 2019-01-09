package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.cli.bean.FenceWarn;
import com.yzxt.yh.module.cli.service.FenceWarnService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;

public class FenceWarnTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(FenceWarnTranAction.class);

    protected FenceWarnService fenceWarnService;

    public FenceWarnService getFenceWarnService()
    {
        return fenceWarnService;
    }

    public void setFenceWarnService(FenceWarnService fenceWarnService)
    {
        this.fenceWarnService = fenceWarnService;
    }

    /**
     * 客户端查询围栏告警
     */
    public void query()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            User operUser = super.getOperUser();
            filter.put("operUser", operUser);
            String custId = GsonUtil.toString(obj.get("patientType"));
            filter.put("custId", custId);
            String deviceCode = GsonUtil.toString(obj.get("deviceCode"));
            filter.put("deviceNo", deviceCode);
            PageTran<FenceWarn> pageTran = fenceWarnService.queryTran(filter, synTime, synType, pageSize);
            List<FenceWarn> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (FenceWarn fw : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("id", fw.getId());
                jObj.addProperty("deviceCode", fw.getDeviceNo());
                jObj.addProperty("checkTime", DateUtil.getTranTime(fw.getWarnTime()));
                jObj.addProperty("positionMode", Constant.POSITION_MODE_GPS);
                jObj.addProperty("longitude", fw.getLongitude());
                jObj.addProperty("latitude", fw.getLatitude());
                // jObj.addProperty("speed", "");
                // jObj.addProperty("direction", "");
                // jObj.addProperty("altitude", "");
                // jObj.addProperty("mnc", "");
                // jObj.addProperty("lac", "");
                // jObj.addProperty("cellid", "");
                jObj.addProperty("address", fw.getAddress());
                jObj.addProperty("synTime", DateUtil.getTranTime(fw.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("客户端查询电子围栏告警错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询电子围栏告警列表错误。");
        }
    }

}
