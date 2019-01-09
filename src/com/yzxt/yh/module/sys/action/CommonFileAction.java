package com.yzxt.yh.module.sys.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.util.FileUtil;

public class CommonFileAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(CommonFileAction.class);

    public void dl() throws IOException
    {
        String path = request.getParameter("pt");
        String filePath = FileUtil.getFullPath(FileUtil.decodePath(path));
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory())
        {
            logger.error("需要下载的文件“”" + filePath + "”不存在。");
            return;
        }
        String fileName = file.getName();
        response.reset();
        // 设置response的Header
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + file.length());
        OutputStream os = response.getOutputStream();
       // FileUtils.copyFile(file, os);
        os.close();
    }

    public void img() throws IOException
    {
        String path = request.getParameter("pt");
        String filePath = FileUtil.getFullPath(FileUtil.decodePath(path));
        File file = new File(filePath);
        if (!file.exists())
        {
            logger.error("需要显示的图片“”" + filePath + "”不存在。");
            return;
        }
        String extName = FileUtil.getExtNameByName(file.getName());
        extName = extName != null ? extName.toLowerCase() : "";
        if ("jpeg".equals(extName) || "jpg".equals(extName))
        {
            response.setContentType("image/jpeg");
        }
        else if ("png".equals(extName))
        {
            response.setContentType("image/jpeg");
        }
        else if ("gif".equals(extName))
        {
            response.setContentType("image/gif");
        }
        else if ("bmp".equals(extName))
        {
            response.setContentType("image/jpeg");
        }
        else
        {
            response.setContentType("image/*");
        }
        OutputStream os = response.getOutputStream();
       // FileUtils.copyFile(file, os);
        os.close();
    }
}
