package com.yzxt.tran;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.module.sys.bean.ClientVersion;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.ClientVersionService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class SysTransAction extends BaseTranAction
{

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(FileUtil.class);

    private Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create();

    private UserService userService;

    private ClientVersionService clientVersionService;

    private File tmpFile[];

    private String tmpFileFileName[];

    private String tmpFileContentType[];

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public ClientVersionService getClientVersionService()
    {
        return clientVersionService;
    }

    public void setClientVersionService(ClientVersionService clientVersionService)
    {
        this.clientVersionService = clientVersionService;
    }

    public File[] getTmpFile()
    {
        return tmpFile;
    }

    public void setTmpFile(File[] tmpFile)
    {
        this.tmpFile = tmpFile;
    }

    public String[] getTmpFileFileName()
    {
        return tmpFileFileName;
    }

    public void setTmpFileFileName(String[] tmpFileFileName)
    {
        this.tmpFileFileName = tmpFileFileName;
    }

    public String[] getTmpFileContentType()
    {
        return tmpFileContentType;
    }

    public void setTmpFileContentType(String[] tmpFileContentType)
    {
        this.tmpFileContentType = tmpFileContentType;
    }

    /**
     * 客户端修改密码
     * @return
     */
    public void changePwd()
    {
        JsonObject obj = getParams();
        if (obj == null)
            return;
        String state = new String();
        String stateInfo = new String();
        try
        {

            //String phone = super.getOperUser().getPhone();
        	String phone = obj.get("phone").getAsString();
            String password = obj.get("password").getAsString();
            String newPassword = obj.get("newPassword").getAsString();
            User user = userService.getUserByPhone(phone);
            if (user == null)
            {
                state = "-2";
                stateInfo = "用户不存在";
            }
            else if (user.getType() == 0)
            {
                state = "-3";
                stateInfo = "此用户不能在终端登录";
            }
            else
            {
                boolean oldValid = userService.getPasswordValid(user.getId(), password);

                if (oldValid && !password.equals(newPassword))
                {
                    state = "1";
                    stateInfo = "操作成功";
                    userService.updatePassword(user.getId(), newPassword);
                }
                else if (oldValid && password.equals(newPassword))
                {
                    state = "-1";
                    stateInfo = "新密码不能与原密码相同";
                }
                else
                {
                    state = "-1";
                    stateInfo = "原密码错误";
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Map<String, Object> resDatas = new HashMap<String, Object>();
        resDatas.put("dataCode", state);
        resDatas.put("dataMsg", stateInfo);
        super.write(ResultTran.STATE_OK, "成功",null);
    }

    /**待完善
     * 版本更新
     * @return
     */
    public String version()
    {
        try
        {
            Map<String, Object> resDatas = new HashMap<String, Object>();
            JsonObject obj = super.getParams();
            // String currentVersion = obj.get("currentVersion").getAsString();
            String appType = obj.get("appType").getAsString();
            String deviceType = obj.get("deviceType").getAsString();
            if (appType == null || appType.length() == 0)
            {
                resDatas.put("version", 0);
                super.write(ResultTran.STATE_OK, "成功", gson.toJsonTree(resDatas));
            }
            ClientVersion clientVersion = clientVersionService.getLatestVersion(appType, deviceType);
            if (clientVersion != null && clientVersion.getVersionNum() != null
                    && clientVersion.getVersionNum().intValue() > 0 && clientVersion.getPath() != null
                    && clientVersion.getPath().length() > 0)
            {
                resDatas.put("version", clientVersion.getVersion());
                resDatas.put("versionNum", clientVersion.getVersionNum());
                resDatas.put("filePath", clientVersion.getPath());
                resDatas.put("updateInfo", clientVersion.getRemark());
                super.write(ResultTran.STATE_OK, "成功", gson.toJsonTree(resDatas));
            }
            else
            {
                resDatas.put("version", 0);
                super.write(ResultTran.STATE_OK, "成功", gson.toJsonTree(resDatas));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编码客户端文件上传信息
     * @param filePath
     * @param fileName
     * @return
     */
    public static String encodeFileInfo(String filePath, String fileName)
    {
        String str = "";
        try
        {
            filePath = filePath != null ? filePath : "";
            fileName = fileName != null ? fileName : "";
            str = Base64.encodeBase64URLSafeString(("path:" + filePath + ",name:" + fileName).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("文件信息转换失败，不支持UTF-8编码.", e);
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 解码文件上传信息
     * @param fileInfo
     * @return 文件路径和
     */
    public static String[] decodeFileInfo(String fileInfo)
    {
        if (StringUtil.isEmpty(fileInfo))
        {
            return null;
        }
        String[] strs = null;
        try
        {
            String str = new String(Base64.decodeBase64(fileInfo), "UTF-8");
            int mPos = str.indexOf(",name:");
            if (mPos > -1)
            {
                strs = new String[]
                {str.substring(5, mPos), str.substring(mPos + 6)};
            }
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("文件信息转换失败，不支持UTF-8编码.", e);
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 客户端向服务器上传临时文件
     */
    public void ulTmpFile()
    {
        String resultCode = ResultTran.STATE_ERROR;
        String msg = "";
        JsonArray data = new JsonArray();
        try
        {
            int fileCount = tmpFile != null ? tmpFile.length : 0;
            if (fileCount > 0)
            {
                for (int i = 0; i < fileCount; i++)
                {
                    if (tmpFile[i] == null)
                    {
                        continue;
                    }
                    FileDesc fd = FileUtil.save(tmpFile[i], tmpFileFileName[i], ConstFilePath.TMP_FILE_PATH_FOLDER,
                            true);
                    String fileInfo = encodeFileInfo(fd.getPath(), tmpFileFileName[i]);
                    data.add(new JsonPrimitive(fileInfo));
                }
            }
            resultCode = ResultTran.STATE_OK;
        }
        catch (Exception e)
        {
            resultCode = ResultTran.STATE_ERROR;
            msg = "保存临时文件出错.";
        }
        super.write(resultCode, msg, data);
    }

    public static void main(String[] args)
    {
        System.out.println(encodeFileInfo("tmp/2015/bl1.png", "bl1.png"));
        System.out.println(encodeFileInfo("tmp/2015/bl2.jpg", "bl2.jpg"));
    }

}
