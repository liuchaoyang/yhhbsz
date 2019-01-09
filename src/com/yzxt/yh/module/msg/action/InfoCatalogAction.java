package com.yzxt.yh.module.msg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.service.InfoCatalogService;
import com.yzxt.yh.module.sys.bean.User;

/**
 * 栏目管理Action
 * @author huanggang
 */

public class InfoCatalogAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private InfoCatalog infoCatalog;

    private String[] userTypes;

    private InfoCatalogService infoCatalogService;

    public String[] getUserTypes()
    {
        return userTypes;
    }

    public void setUserTypes(String[] userTypes)
    {
        this.userTypes = userTypes;
    }

    public InfoCatalogService getInfoCatalogService()
    {
        return infoCatalogService;
    }

    public void setInfoCatalogService(InfoCatalogService infoCatalogService)
    {
        this.infoCatalogService = infoCatalogService;
    }

    public InfoCatalog getInfoCatalog()
    {
        return infoCatalog;
    }

    public void setInfoCatalog(InfoCatalog infoCatalog)
    {
        this.infoCatalog = infoCatalog;
    }

    /**
     * 获取栏目或是专题列表
     *@author huanggang 
     */
    public String list()
    {
        String retInfo = "";
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String keyword = request.getParameter("keyword");
            filter.put("keyword", keyword != null ? keyword.trim() : null);
            PageModel<InfoCatalog> list = infoCatalogService.getInfoTopicByPage(filter, getPage(), getRows());
            /*retInfo = getListByJson(list);*/
            super.write(list);

        }
        catch (NumberFormatException numex)
        {
            /*retInfo = ReturnInfo.RetParamErr;*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
            /* retInfo = ReturnInfo.RetOtherErr;*/
        }
        /*this.outHtmlDatas(retInfo);*/
        return null;
    }

    /**
     * 增加栏目或是专题列表行
     *@author huanggang 
     */
    public String addInfoTopic() throws Exception
    {

        {
            String msg = "0";
            try
            {
                User user = (User) getCurrentUser();
                User creator = new User();
                creator.setId(user.getId());
                infoCatalog.setCreateBy(user.getId());//建档人
                infoCatalog.setUpdateBy(user.getId());//修改人
                infoCatalog.setType(2);
                int r = infoCatalogService.addInfoTopic(infoCatalog);
                // 编码已经存在
                if (r == -11)
                {
                    msg = "-11";
                }
                else if (r == -12)
                {
                    msg = "-12";
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                msg = "-3";
            }
            super.write(msg);
            return null;
        }
    }

    /**
     * 修改栏目或是专题列表行
     *@author huanggang 
     */
    public String updateInfoTopic() throws Exception
    {

        String msg = "0";
        try
        {
            User user = (User) getCurrentUser();
            User creator = new User();
            creator.setId(user.getId());
            infoCatalog.setUpdateBy(user.getId());//建档人
            String resid = request.getParameter("infoCatalogAuthResid");
            String residMem = request.getParameter("infoCatalogAuthResidMem");
            int r = infoCatalogService.updateInfoTopic(infoCatalog);
            if (r == -21)
            {
                msg = "-21";
            }
            else if (r == -22)
            {
                msg = "-22";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    /**
     * 编辑栏目或是专题行
     *@author huanggang 
     */
    public String edit()
    {
        String list = null;
        return list;
    }

    /**
     * 专题生效
     *@author huanggang 
     */
    public String updateInfoTopicState()
    {
        String msg = "0";
        try
        {
            User user = (User) getCurrentUser();
            User creator = new User();
            creator.setId(user.getId());
            infoCatalog.setUpdateBy(user.getId());//建档人
            int r = infoCatalogService.updateState1InfoTopic(infoCatalog);
            if (r == -21)
            {
                msg = "-21";
            }
            else if (r == -22)
            {
                msg = "-22";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    /**
    * 停用专题
    * @param id
    * @author huanggang
    * @return
    */
    public String updateState() throws Exception
    {

        String msg = "0";
        try
        {
            User user = (User) getCurrentUser();
            User creator = new User();
            creator.setId(user.getId());
            infoCatalog.setUpdateBy(user.getId());//建档人
            int r = infoCatalogService.updateStateInfoTopic(infoCatalog);
            if (r == -21)
            {
                msg = "-21";
            }
            else if (r == -22)
            {
                msg = "-22";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    /*
     * 栏目模块的查询列表，启用和停用栏目，修改栏目，增加栏目
     * */
    public String getInfoCatalogList() throws Exception
    {
        /* String retInfo = "";
         try {
             Map<String, Object> filter = new HashMap<String, Object>();
             String keyword = request.getParameter("keyword");
             filter.put("keyword", keyword != null ? keyword.trim() : null);
             ListPageBean list = infoCatalogService.getInfoCatalogByPage(filter, getPage(), getRows());
             retInfo = getListByJson(list);

         } catch (NumberFormatException numex) {
             retInfo = ReturnInfo.RetParamErr;
         } catch (Exception e) {
             e.printStackTrace();
             retInfo = ReturnInfo.RetOtherErr;
         }
         System.out.println(retInfo);
         this.outHtmlDatas(retInfo);
         return null;*/

        List<InfoCatalog> infoCatalogs = infoCatalogService.getInfoCatalogList();
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> jsonList = new ArrayList<String>();
        int len = infoCatalogs != null ? infoCatalogs.size() : 0;
        for (int i = 0; i < len; i++)
        {
            String jsonStr = gson.toJson(infoCatalogs.get(i));
            jsonList.add(jsonStr);
        }
        map.put("rows", jsonList);
        JSONObject js = JSONObject.fromObject(map);
        super.write(js.toString());
        return null;
    }

    /**
     * 增加栏目
     *@author huanggang 
     */
    public String addInfoCatalog() throws Exception
    {

        {
            String msg = "0";
            try
            {
                infoCatalog.setUserTypes(userTypes);
                User user = (User) getCurrentUser();
                User creator = new User();
                creator.setId(user.getId());
                infoCatalog.setCreateBy(user.getId());//建档人
                infoCatalog.setUpdateBy(user.getId());//修改人
                infoCatalog.setType(1);
                int r = infoCatalogService.addInfoCatalog(infoCatalog);
                // 编码已经存在
                if (r == -11)
                {
                    msg = "-11";
                }
                else if (r == -12)
                {
                    msg = "-12";
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                msg = "-3";
            }
            super.write(msg);
            return null;
        }
    }

    /**
     * 修改栏目
     *@author huanggang 
     */
    public String updateInfoCatalog() throws Exception
    {

        String msg = "0";
        try
        {
            /*String[] userTypes = request.getParameterValues("userType");*/
            infoCatalog.setUserTypes(userTypes);
            User user = (User) getCurrentUser();
            User creator = new User();
            creator.setId(user.getId());
            infoCatalog.setUpdateBy(user.getId());//建档人

            int r = infoCatalogService.updateInfoCatalog(infoCatalog);
            if (r == -21)
            {
                msg = "-21";
            }
            else if (r == -22)
            {
                msg = "-22";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    /**
     * 启用栏目
     *@author huanggang 
     */
    public String updateInfoCatalogState()
    {
        String msg = "0";
        try
        {
            User user = (User) getCurrentUser();
            User creator = new User();
            creator.setId(user.getId());
            infoCatalog.setUpdateBy(user.getId());//建档人
            int r = infoCatalogService.updateInfoCatalogState(infoCatalog);
            if (r == -21)
            {
                msg = "-21";
            }
            else if (r == -22)
            {
                msg = "-22";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }

    /**
    * 停用栏目
    * @param id
    * @author huanggang
    * @return
    */
    public String updateInfoCatalogStateNo() throws Exception
    {

        String msg = "0";
        try
        {
            User user = (User) getCurrentUser();
            User creator = new User();
            creator.setId(user.getId());
            infoCatalog.setUpdateBy(user.getId());//建档人
            int r = infoCatalogService.updateInfoCatalogStateNo(infoCatalog);
            if (r == -21)
            {
                msg = "-21";
            }
            else if (r == -22)
            {
                msg = "-22";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            msg = "-3";
        }
        super.write(msg);
        return null;
    }
}
