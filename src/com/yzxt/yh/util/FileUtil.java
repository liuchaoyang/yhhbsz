package com.yzxt.yh.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.yzxt.fw.server.Config;
import com.yzxt.yh.constant.ConfigKey;
import com.yzxt.yh.module.sys.bean.FileDesc;

public class FileUtil
{
    private static Logger logger = Logger.getLogger(FileUtil.class);

    private static Random random = new Random();

    private static SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy");

    /**
     * 将临时文件保存到目标文件夹中
     * @param temp
     * @return
     * @throws IOException 
     */
    public static FileDesc save(File file, String fileName, String basePath) throws IOException
    {
        return save(file, fileName, basePath, false);
    }

    /**
     * 将临时文件保存到目标文件夹中
     * @param file
     * @param fileName
     * @param basePath
     * @param del 保存后是否删除文件
     * @return
     * @throws IOException
     */
    public static FileDesc save(File file, String fileName, String basePath, boolean del) throws IOException
    {
        if (file == null)
        {
            return null;
        }
        if (fileName == null)
        {
            fileName = "";
        }
        int extSplitPos = fileName.lastIndexOf('.');
        String extName = extSplitPos > -1 ? fileName.substring(extSplitPos + 1) : "";
        // 产生内部存储路径
        String innerPath = genNewPath(extName, basePath);
        // 生成存储后的文件
        FileUtils.copyFile(file, new File(getFullPath(innerPath)));
        FileDesc fd = new FileDesc();
        fd.setExtName(extName);
        fd.setFileSize(file.length());
        fd.setName(fileName);
        fd.setPath(innerPath);
        // 删除临时文件
        if (del)
        {
            file.delete();
        }
        return fd;
    }

    /**
     * 生成新的文件路径
     */
    public static String genNewPath(String extName, String basePath)
    {
        StringBuilder innerPathSb = new StringBuilder(basePath);
        innerPathSb.append(pathFormat.format(new Date())).append('/');
        innerPathSb.append(System.currentTimeMillis());
        for (int i = 0; i < 6; i++)
        {
            innerPathSb.append(random.nextInt(10));
        }
        if (extName != null && extName.length() > 0)
        {
            innerPathSb.append('.').append(extName);
        }
        return innerPathSb.toString();
    }

    /**
     * 获取文件全路径
     * @param filePath
     * @return
     */
    public static String getFullPath(String filePath)
    {
        return Config.getInstance().getString(ConfigKey.FILE_DIR) + filePath;
    }

    /**
     * 对外显示的路径，需要转码
     * @param path
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String encodePath(String path)
    {
        if (StringUtil.isEmpty(path))
        {
            return "";
        }
        String str = "";
        try
        {
            str = Base64.encodeBase64URLSafeString(path.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("路径转换失败，不支持UTF-8编码.", e);
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 对外显示的路径，需要转码
     * @param path
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String decodePath(String path)
    {
        if (StringUtil.isEmpty(path))
        {
            return "";
        }
        String str = "";
        try
        {
            str = new String(Base64.decodeBase64(path), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("路径转换失败，不支持UTF-8编码.", e);
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getExtNameByName(String fileName)
    {
        int pos = fileName != null ? fileName.lastIndexOf('.') : -1;
        if (pos > -1)
        {
            return fileName.substring(pos + 1);
        }
        return "";
    }

}
