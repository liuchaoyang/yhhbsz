package com.yzxt.yh.module.msg.action;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.yh.module.msg.bean.Knowledge;
import com.yzxt.yh.module.msg.bean.KnowledgeCatalog;
import com.yzxt.yh.module.msg.service.KnowledgeCatalogService;
import com.yzxt.yh.module.msg.service.KnowledgeService;
import com.yzxt.yh.module.msg.service.RichtextService;
import com.yzxt.yh.module.sys.bean.User;

/*
 * 上传Excel并用poi解析Excel，然后插入数据库中
 * */

public class ImportExcelAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ImportExcelAction.class);

    private RichtextService richtextService;
    private KnowledgeService knowledgeService;

    private KnowledgeCatalogService knowledgeCatalogService;

    private Knowledge knowledge;

    public RichtextService getRichtextService()
    {
        return richtextService;
    }

    public void setRichtextService(RichtextService richtextService)
    {
        this.richtextService = richtextService;
    }

    public KnowledgeService getKnowledgeService()
    {
        return knowledgeService;
    }

    public void setKnowledgeService(KnowledgeService knowledgeService)
    {
        this.knowledgeService = knowledgeService;
    }

    public KnowledgeCatalogService getKnowledgeCatalogService()
    {
        return knowledgeCatalogService;
    }

    public void setKnowledgeCatalogService(KnowledgeCatalogService knowledgeCatalogService)
    {
        this.knowledgeCatalogService = knowledgeCatalogService;
    }

    public Knowledge getKnowledge()
    {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge)
    {
        this.knowledge = knowledge;
    }

    // 上传文件域
    private File[] attachment;

    // 上传文件名
    private String attachmentFileName;

    // 上传文件类型
    private String attachmentContentType;

    // 保存文件的目录路径(通过依赖注入)
    private String attachmentPath;

    public File[] getAttachment()
    {
        return attachment;
    }

    public void setAttachment(File[] attachment)
    {
        this.attachment = attachment;
    }

    public String getAttachmentFileName()
    {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName)
    {
        this.attachmentFileName = attachmentFileName;
    }

    public String getAttachmentContentType()
    {
        return attachmentContentType;
    }

    public void setAttachmentContentType(String attachmentContentType)
    {
        this.attachmentContentType = attachmentContentType;
    }

    public String getAttachmentPath()
    {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath)
    {
        this.attachmentPath = attachmentPath;
    }

    public String saveData() throws Exception
    {
        List<List<Object>> list = new LinkedList<List<Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径    
        XSSFWorkbook xwb = new XSSFWorkbook("D:/高血压健康知识.xlsx");
        // 读取第一章表格内容    
        XSSFSheet sheet = xwb.getSheetAt(0);
        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++)
        {
            row = sheet.getRow(i);
            if (row == null)
            {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++)
            {
                cell = row.getCell(j);
                if (cell == null)
                {
                    continue;
                }
                DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符    
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串    
                DecimalFormat nf = new DecimalFormat("0");// 格式化数字    
                switch (cell.getCellType())
                {
                    case XSSFCell.CELL_TYPE_STRING:
                        //   System.out.println(i+"行"+j+" 列 is String type");    
                        value = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        //   System.out.println(i+"行"+j+" 列 is Number type ; DateFormt:"+cell.getCellStyle().getDataFormatString());    
                        if ("@".equals(cell.getCellStyle().getDataFormatString()))
                        {
                            value = df.format(cell.getNumericCellValue());
                        }
                        else if ("General".equals(cell.getCellStyle().getDataFormatString()))
                        {
                            value = nf.format(cell.getNumericCellValue());
                        }
                        else
                        {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        }
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        //   System.out.println(i+"行"+j+" 列 is Boolean type");    
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        //           System.out.println(i+"行"+j+" 列 is Blank type");    
                        value = "";
                        break;
                    default:
                        //   System.out.println(i+"行"+j+" 列 is default type");    
                        value = cell.toString();
                }
                if (value == null || "".equals(value))
                {
                    continue;
                }
                linked.add(value);
            }
            list.add(linked);
        }
        if (list.size() >= 1)
        {
            int j = 0;
            int k = 0;
            int n = 0;
            for (int i = 0; i < list.size(); i++)
            {
                //通过    (String)list.get(i).get(0)，(String)list.get(i).get(1)这种方式获取相应的值  
                //并新建对象，把数据持久化到数据库中  
                Knowledge knowledge = new Knowledge();
                if (list.get(i).size() == 6)
                {
                    String parentname = (String) list.get(i).get(0);
                    String childname = (String) list.get(i).get(1);
                    List<KnowledgeCatalog> parentList = knowledgeCatalogService.getKnowledgeCatalogByName(parentname);
                    KnowledgeCatalog parent = parentList.get(0);
                    String parentId = parent.getId();
                    List<KnowledgeCatalog> childList = knowledgeCatalogService.getKnowledgeCatalogById(parentId);
                    for (int m = 0; m < childList.size(); m++)
                    {
                        KnowledgeCatalog child = new KnowledgeCatalog();
                        child = childList.get(m);
                        String childId = child.getId();
                        String childName = child.getName();
                        if (childname.equals(childName))
                        {
                            knowledge.setCatalogId(childId);
                        }
                    }
                    knowledge.setTitle(((String) list.get(i).get(2)));
                    knowledge.setSummary(((String) list.get(i).get(4)));
                    knowledge.setContent(parseContent(((String) list.get(i).get(5))));
                    k = i;
                }
                else if (list.get(i).size() == 5)
                {
                    String childname = (String) list.get(i).get(0);
                    if (k == 0)
                    {
                        String parentname = (String) list.get(k).get(0);
                        List<KnowledgeCatalog> parentList = knowledgeCatalogService
                                .getKnowledgeCatalogByName(parentname);
                        KnowledgeCatalog parent = parentList.get(0);
                        String parentId = parent.getId();
                        List<KnowledgeCatalog> childList = knowledgeCatalogService.getKnowledgeCatalogById(parentId);
                        for (int m = 0; m < childList.size(); m++)
                        {
                            KnowledgeCatalog child = new KnowledgeCatalog();
                            child = childList.get(m);
                            String childId = child.getId();
                            String childName = child.getName();
                            if (childname.equals(childName))
                            {
                                knowledge.setCatalogId(childId);
                            }
                        }
                    }
                    knowledge.setTitle(((String) list.get(i).get(1)));
                    knowledge.setSummary(((String) list.get(i).get(3)));
                    knowledge.setContent(parseContent(((String) list.get(i).get(4))));
                    j = i;
                }
                else if (list.get(i).size() == 4)
                {
                    String parentname = (String) list.get(k).get(0);
                    if (k == n || j == k)
                    {
                        String childname = (String) list.get(j).get(1);
                        List<KnowledgeCatalog> parentList = knowledgeCatalogService
                                .getKnowledgeCatalogByName(parentname);
                        KnowledgeCatalog parent = parentList.get(0);
                        String parentId = parent.getId();
                        List<KnowledgeCatalog> childList = knowledgeCatalogService.getKnowledgeCatalogById(parentId);
                        for (int m = 0; m < childList.size(); m++)
                        {
                            KnowledgeCatalog child = new KnowledgeCatalog();
                            child = childList.get(m);
                            String childId = child.getId();
                            String childName = child.getName();
                            if (childname.equals(childName))
                            {
                                knowledge.setCatalogId(childId);
                            }
                        }
                    }
                    else
                    {
                        String childname = (String) list.get(j).get(0);
                        List<KnowledgeCatalog> parentList = knowledgeCatalogService
                                .getKnowledgeCatalogByName(parentname);
                        KnowledgeCatalog parent = parentList.get(0);
                        String parentId = parent.getId();
                        List<KnowledgeCatalog> childList = knowledgeCatalogService.getKnowledgeCatalogById(parentId);
                        for (int m = 0; m < childList.size(); m++)
                        {
                            KnowledgeCatalog child = new KnowledgeCatalog();
                            child = childList.get(m);
                            String childId = child.getId();
                            String childName = child.getName();
                            if (childname.equals(childName))
                            {
                                knowledge.setCatalogId(childId);
                            }
                        }
                    }
                    knowledge.setTitle(((String) list.get(i).get(0)));
                    knowledge.setSummary(((String) list.get(i).get(2)));
                    knowledge.setContent(parseContent(((String) list.get(i).get(3))));
                    n = i;
                }
                User user = (User) getCurrentUser();
                knowledge.setCreateBy(user.getId());
                knowledge.setUpdateBy(user.getId());
                knowledge.setCount(i);
                Result r = knowledgeService.add(knowledge);
            }
        }
        else
        {

        }
        return "success";
    }

    private static String parseContent(String plainText)
    {
        if (plainText == null)
        {
            return "";
        }
        StringBuilder html = new StringBuilder();
        html.append("<div style=\"line-height: 150%;font-size: 18px;\">");
        Scanner scanner = new Scanner(plainText);
        while (scanner.hasNextLine())
        {
            html.append("<p>").append(scanner.nextLine()).append("</p>");
        }
        html.append("</div>");
        return html.toString();
    }

}
