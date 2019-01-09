package com.yzxt.fw.ext.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class NumberAdapter implements JsonDeserializer<Number>
{
    public NumberAdapter()
    {
    }

    public Number deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
    {
        if (!(arg0 instanceof JsonPrimitive))
        {
            throw new JsonParseException("The number should be a string value.");
        }
        String value = arg0.getAsString();
        if (arg0.getAsString().trim().length() < 1)
        {
            return null;
        }
        if (arg1.toString().contains("Integer"))
        {
            return Integer.valueOf(value);
        }
        if (arg1.toString().contains("Double"))
        {
            return Double.valueOf(value);
        }
        return null;
    }
}