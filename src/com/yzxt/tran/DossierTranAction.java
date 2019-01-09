package com.yzxt.tran;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.module.ach.bean.Dossier;
import com.yzxt.yh.module.ach.service.DossierService;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class DossierTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(DossierTranAction.class);

    private DossierService dossierService;

    public DossierService getDossierService()
    {
        return dossierService;
    }

    public void setDossierService(DossierService dossierService)
    {
        this.dossierService = dossierService;
    }

    /**
     * 添加病历夹
     */
    public void add()
    {
        try
        {
            JsonObject obj = super.getParams();
            User operUser = super.getOperUser();
            String custId = GsonUtil.toString(obj.get("custId"));
            Integer type = GsonUtil.toInteger(obj.get("type"));
            String memo = GsonUtil.toString(obj.get("info"));
            JsonElement createTimeObj = obj.get("createTime");
            Timestamp createTime = null;
            if (createTimeObj != null && StringUtil.isNotEmpty(createTimeObj.toString()))
            {
                createTime = gson.fromJson(createTimeObj, Timestamp.class);
            }
            // 病历图片
            JsonElement filesObj = obj.get("files");
            if (filesObj == null)
            {
                super.write(ResultTran.STATE_ERROR, "图片信息无效");
                return;
            }
            List<FileDesc> fds = new ArrayList<FileDesc>();
            JsonArray filesArr = filesObj.getAsJsonArray();
            int size = filesArr.size();
            for (int i = 0; i < size; i++)
            {
                String fileStr = filesArr.get(i).getAsString();
                String[] fileInfo = decodeFileInfo(fileStr);
                String path = FileUtil.getFullPath(fileInfo[0]);
                File temFile = new File(path);
                FileDesc fd = FileUtil.save(temFile, temFile.getName(), ConstFilePath.HIS_FOLDER, true);
                fds.add(fd);
            }
            Dossier dossier = new Dossier();
            dossier.setCustId(StringUtil.isNotEmpty(custId) ? custId : operUser.getId());
            dossier.setType(type);
            dossier.setMemo(memo);
            dossier.setCreateBy(operUser.getId());
            dossier.setCreateTime(createTime);
            dossier.setFileDescs(fds);
            Result r = dossierService.add(dossier);
            if (r.getState() == Result.STATE_SUCESS)
            {
                super.write(ResultTran.STATE_OK, "保存成功");
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error("添加病历夹错误。", e);
            super.write(ResultTran.STATE_ERROR, "添加病历夹错误。");
        }
    }
    
    /**
     * 加载病历夹明细
     */
    public void load()
    {
        
    }

    /**
     * 病历夹列表
     */
    public void list()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            // 客户ID
            filter.put("operUser", super.getOperUser());
            filter.put("custId", GsonUtil.toString(obj.get("custId")));
            filter.put("type", GsonUtil.toInteger(obj.get("type")));
            PageTran<Dossier> pageTran = dossierService.queryTran(filter, synTime, pageSize);
            JsonArray ja = new JsonArray();
            List<Dossier> list = pageTran.getData();
            if (list != null && !list.isEmpty())
            {
                for (Dossier d : list)
                {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("id", d.getId());
                    jo.addProperty("custId", d.getCustId());
                    jo.addProperty("type", d.getType());
                    jo.addProperty("name", d.getTypeName());
                    jo.addProperty("info", d.getMemo());
                    JsonArray pJa = new JsonArray();
                    String[] paths = StringUtil.splitWithoutEmpty(d.getPaths(), ",");
                    if (paths != null)
                    {
                        for (String path : paths)
                        {
                            pJa.add(new JsonPrimitive("pub/cf_img.do?pt=" + FileUtil.encodePath(path)));
                        }
                    }
                    jo.add("filePaths", pJa);
                    jo.addProperty("synTime", DateUtil.toSynTimeStr(d.getCreateTime()));
                    ja.add(jo);
                }
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询体检报告列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询体检报告列表错误。");
        }
    }

}
