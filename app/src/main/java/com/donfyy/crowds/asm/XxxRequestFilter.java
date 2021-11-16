package com.donfyy.crowds.asm;

import com.alibaba.fastjson.serializer.PropertyFilter;

public class XxxRequestFilter implements PropertyFilter {
    @Override
    public boolean apply(Object object, String name, Object value) {
        System.out.println("XxxRequestFilter, name:" + name + " value:" + value + " object:" + object);

        if ("maxLen".equals(name)) {
            return false;
        }
        return true;
    }
}
