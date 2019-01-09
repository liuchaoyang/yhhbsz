package com.yzxt.yh.module.msg.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.bean.Information;
import com.yzxt.yh.module.msg.service.InfoCatalogService;
import com.yzxt.yh.module.msg.service.InformationService;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 信息资讯Action类
 * @author f
 *
 */
public class InformationAction extends BaseAction
{
    private Logger logger = Logger.getLogger(InformationAction.class);

    private static final long serialVersionUID = 1L;

    private InformationService informationService;

    private InfoCatalogService infoCatalogService;

    private Information information;

    // 资讯图标
    private File infoIcon;

    private String infoIconFileName;

    private String infoIconContentType;

    public InformationService getInformationService()
    {
        return informationService;
    }

    public void setInformationService(InformationService informationService)
    {
        this.informationService = informationService;
    }

    public InfoCatalogService getInfoCatalogService()
    {
        return infoCatalogService;
    }

    public void setInfoCatalogService(InfoCatalogService infoCatalogService)
    {
        this.infoCatalogService = infoCatalogService;
    }

    public Information getInformation()
    {
        return information;
    }

    public void setInformation(Information information)
    {
        this.information = information;
    }

    public File getInfoIcon()
    {
        return infoIcon;
    }

    public void setInfoIcon(File infoIcon)
    {
        this.infoIcon = infoIcon;
    }

    public String getInfoIconFileName()
    {
        return infoIconFileName;
    }

    public void setInfoIconFileName(String infoIconFileName)
    {
        this.infoIconFileName = infoIconFileName;
    }

    public String getInfoIconContentType()
    {
        return infoIconContentType;
    }

    public void setInfoIconContentType(String infoIconContentType)
    {
        this.infoIconContentType = infoIconContentType;
    }

    public String toEdit() throws Exception
    {
        String id = request.getParameter("id");
        // 没有指定ID，新建资讯
        if (StringUtil.isEmpty(id))
        {
            information = new Information();
            information.setLevel(2);
            request.setAttribute("update", false);
        }
        else
        {
            information = informationService.load(id);
            if (information == null)
            {
                request.setAttribute("msg", "资讯修改出错，此资讯已不存在！");
                return "editError";
            }
            String iconFilePath = information.getIconFilePath();
            if (StringUtil.isNotEmpty(iconFilePath))
            {
                information.setIconFilePath(FileUtil.encodePath(iconFilePath));
                information.setIconFileExtName(FileUtil.getExtNameByName(information.getIconFileName()));
            }
            request.setAttribute("update", true);
        }
        request.setAttribute("infomation", information);
        // 所有栏目列表
        List<InfoCatalog> infoColumns = infoCatalogService.getInfoColumns();
        request.setAttribute("infoColumns", infoColumns);
        return "edit";
    }

    /**
     * 添加资讯信息
     * @return
     * @throws Exception
     */
    public void add()
    {
        int state = 0;
        String msg = "";
        try
        {
            // 验证数据
            if (infoIcon == null)
            {
                state = -1;
                msg = "发布失败，请选择图标文件！";
            }
            else if (infoIcon.length() > 99999999)
            {
                state = -1;
                msg = "发布失败，图标文件过大！";
            }
            // 验证通过，开始保存数据
            if (StringUtil.isEmpty(msg))
            {
                // 图标文件
                FileDesc iconFile = FileUtil.save(infoIcon, infoIconFileName, ConstFilePath.MSG_FILE_FOLDER, true);
                information.setIconFile(iconFile);
                // 用户
                User user = (User) getCurrentUser();
                information.setCreateBy(user.getId());
                information.setUpdateBy(user.getId());
                // 保存
                informationService.add(information);
                state = 1;
            }
        }
        catch (Exception e)
        {
            state = -1;
            msg = "发布出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 更新资讯信息
     * @return
     * @throws Exception
     */
    public void update()
    {
        int state = 0;
        String msg = "";
        try
        {
            // 验证数据
            if (infoIcon != null && infoIcon.length() > 99999999)
            {
                state = -1;
                msg = "发布失败，图标文件过大！";
            }
            // 验证通过，开始保存数据
            if (StringUtil.isEmpty(msg))
            {
                // 如果有修改图标文件
                if (infoIcon != null)
                {
                    FileDesc iconFile = FileUtil.save(infoIcon, infoIconFileName, ConstFilePath.MSG_FILE_FOLDER, true);
                    information.setIconFile(iconFile);
                }
                // 用户
                User user = (User) getCurrentUser();
                information.setUpdateBy(user.getId());
                // 更新
                Result r = informationService.update(information);
                state = r.getState();
                msg = r.getMsg();
            }
        }
        catch (Exception e)
        {
            state = -1;
            msg = "发布出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 删除资讯信息
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
                msg = "请指定资讯ID！";
            }
            // 验证通过，开始删除数据
            if (StringUtil.isEmpty(msg))
            {
                informationService.delete(id);
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
     * 查看资讯信息
     * @return
     * @throws Exception
     */
    public String view() throws Exception
    {
        User user = (User) getCurrentUser();
        try
        {
            String id = request.getParameter("id");
            Information information = informationService.load(id);
            if (information == null)
            {
                request.setAttribute("msg", "错误，此资讯已不存在！");
                return "editError";
            }
            request.setAttribute("information", information);
            request.setAttribute("editable", user != null ? user.getId().equals(information.getUpdateBy()) : false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return "view";
    }

    /**
     * 显示资讯列表信息
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public String list() throws Exception
    {
        User user = (User) getCurrentUser();
        Integer userType = user != null ? user.getType() : null;
        if (userType == null)
        {
            userType = Constant.USER_TYPE_CUSTOMER;
        }
        List<InfoCatalog> columns = infoCatalogService.getInfoColumnsByUserType(userType, false);
        String infoCatalogId = request.getParameter("infoCatalogId");
        int infoCatalogType;
        // 如果是指定了专题ID，保存此专题
        InfoCatalog infoCatalog = null;
        // 如果没有指定栏目ID，默认展开第一个栏目
        if (StringUtil.isEmpty(infoCatalogId))
        {
            // 如果存在栏目，默认展开第一个栏目
            if (columns.size() > 0)
            {
                infoCatalogId = columns.get(0).getId();
                infoCatalogType = Constant.INFO_CATALOG_TYPE_COLUMN;
            }
            else
            // 如果没有专题外的栏目，默认展示专题
            {
                infoCatalogId = Constant.INFO_COLUMN_TOPIC;
                infoCatalogType = Constant.INFO_CATALOG_TYPE_TOPIC;
            }
        }
        else
        // 指定了目录ID
        {
            // 判断是否是专题特有的
            if (!Constant.INFO_COLUMN_TOPIC.equals(infoCatalogId)
                    && !Constant.INFO_COLUMN_MY_PUBLISH.equals(infoCatalogId))
            {
                infoCatalog = infoCatalogService.getInfoCatalog(infoCatalogId);
                infoCatalogType = infoCatalog.getType();
            }
            else
            {
                infoCatalogType = Constant.INFO_CATALOG_TYPE_COLUMN;
            }
        }
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
        Map<String, Object> filter = new HashMap<String, Object>();
        PageModel pageModel;
        // 查询栏目或专题下的资讯
        if (!Constant.INFO_COLUMN_TOPIC.equals(infoCatalogId))
        {
            filter.put("infoCatalogId", infoCatalogId);
            filter.put("infoCatalogType", infoCatalogType);
            filter.put("userType", userType);
            filter.put("userId", user != null ? user.getId() : "-9");
            pageModel = informationService.queryInfomationByPage(filter, page, pageSize);
        }
        else
        // 查询专题下的专题列表
        {
            filter.put("haveInfoOnly", true);
            pageModel = informationService.queryTopicByPage(filter, page, pageSize);
        }
        request.setAttribute("infoCatalogId", infoCatalogId);
        request.setAttribute("infoCatalogType", infoCatalogType);
        request.setAttribute("infoCatalog", infoCatalog);
        request.setAttribute("canPublish", Constant.USER_TYPE_DOCTOR.equals(userType));
        request.setAttribute("columns", columns);
        request.setAttribute("pageModel", pageModel);
        return "list";
    }

    /**
     * 查询资讯
     */
    public void queryCustHomeInfo()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Information> pageModel = informationService.queryCustHomeInfo(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询首页客户资讯错误。", e);
        }
    }

    /**
     * 分页查询专题信息
     */
    public void queryTopicByPage()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String keyword = request.getParameter("keyword");
            filter.put("keyword", keyword != null ? keyword.trim() : null);
            PageModel<InfoCatalog> pageModel = informationService.queryTopicByPage(filter, super.getPage(),
                    super.getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
