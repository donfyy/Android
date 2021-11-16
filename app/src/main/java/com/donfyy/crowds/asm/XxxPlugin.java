package com.donfyy.crowds.asm;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;

public class XxxPlugin {
    @JSONType(serialzeFilters = XxxRequestFilter.class)
    public static class XxxRequest extends BaseModel {
        @JSONField(name = "xpath")
        @RequiredParam
        public String path;
        @JSONField(name = "xint")
        public int aint;
        @JSONField(name = "abyte")
        public byte abyte;
        @JSONField(name = "ashort")
        public short aShort;
        @JSONField(name = "achar")
        public char aChar;
        @JSONField(name = "along")
        public long aLong;
        @JSONField(name = "afloat")
        public float aFloat;
        @JSONField(name = "aDouble")
        public double aDouble;
        @JSONField(name = "boolean")
        public boolean aBoolean;
        @JSONField(name = "aDoubleArray")
        public double[] doubleArray;

        @JSONField(name = "aObjectArray")
        public Object[] objects;
        @JSONField(name = "aStringList")
        public List<String> stringList;
        public Activity activity;
        public TextUtils textUtils;

        @JSONField(name = "maxLen")
        public long valueMaxLen;
    }

    private static class XxxResponse extends BaseModel {}

    @PluginFunction(eventName = "createDownloadTask")
    void pluginMethod(XxxRequest request, int extra, LKEventInvokeCallback<XxxResponse> callback) {

    }
}
