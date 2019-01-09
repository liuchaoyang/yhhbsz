package com.yzxt.fw.ext.struts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class StrutsDateConverter extends StrutsTypeConverter
{

    private final DateFormat[] dfs =
    {new SimpleDateFormat("yyyy年MM月dd日"), new SimpleDateFormat("yyyy-MM-dd"),};

    @SuppressWarnings("rawtypes")
    @Override
    public Object convertFromString(Map context, String[] values, Class toType)
    {
        String dateStr = values[0];
        if (dateStr == null || dateStr.length() == 0)
        {
            return null;
        }
        for (int i = 0; i < dfs.length; i++)
        {
            try
            {
                return dfs[i].parse(dateStr);
            }
            catch (Exception e)
            {
                continue;
            }
        }
        throw new TypeConversionException();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String convertToString(Map context, Object object)
    {
        Date date = (Date) object;
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

}
