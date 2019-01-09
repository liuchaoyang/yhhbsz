package com.yzxt.fw.ext.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

public class HibernateParams
{
    private List<Object> vals = new ArrayList<Object>();

    private List<Type> types = new ArrayList<Type>();

    public static String getLikePart(String likeVal)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("'%");
        int len = likeVal.length();
        for (int i = 0; i < len; i++)
        {
            char ch = likeVal.charAt(i);
            if (ch == '%')
            {
                sql.append("/%");
            }
            else if (ch == '/')
            {
                sql.append("//");
            }
            else if (ch == '_')
            {
                sql.append("/_");
            }
            else
            {
                sql.append(ch);
            }
        }
        sql.append("%' escape '/'");
        return sql.toString();
    }

    public void clear()
    {
        vals.clear();
        types.clear();
    }

    public Object[] getVals()
    {
        return vals.toArray();
    }

    public Type[] getTypes()
    {
        return types.toArray(new Type[types.size()]);
    }

    public HibernateParams add(Object val, Type type)
    {
        vals.add(val);
        types.add(type);
        return this;
    }

    public String addLikePart(String likeVal)
    {
        StringBuilder sql = new StringBuilder();
        sql.append('%').append(likeVal).append('%');
        vals.add(sql.toString());
        types.add(Hibernate.STRING);
        return "?";
    }

}
