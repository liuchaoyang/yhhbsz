package com.yzxt.fw.ext.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class DateAdapter implements JsonDeserializer<Date>
{
    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public DateAdapter()
    {
    }

    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        if (!(json instanceof JsonPrimitive))
        {
            throw new JsonParseException("The date should be a string value.");
        }
        String dateStr = json.getAsString();
        if (dateStr == null || dateStr.length() == 0)
        {
            return null;
        }
        if (dateStr.length() == 10)
        {
            dateStr = dateStr + " 00:00:00";
        }
        try
        {
            Date date = formatter.parse(dateStr);
            return new Timestamp(date.getTime());
        }
        catch (ParseException e)
        {
            throw new JsonParseException("客户端时间转换出错，时间：" + dateStr, e);
        }
    }

}