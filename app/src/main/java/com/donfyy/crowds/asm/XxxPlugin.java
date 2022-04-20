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
        @JSONField(name = "aInteger")
        public Integer aInteger;
        @JSONField(name = "abyte")
        public byte abyte;
        @JSONField(name = "aByte")
        public Byte aByte;
        @JSONField(name = "ashort")
        public short aShort;
        @JSONField(name = "aShortObject")
        public Short aShortObject;
        @JSONField(name = "achar")
        public char aChar;
        @JSONField(name = "aCharacter")
        public Character aCharacter;
        @JSONField(name = "along")
        public long aLong;
        @JSONField(name = "aLongObject")
        public Long aLongObject;
        @JSONField(name = "afloat")
        public float aFloat;
        @JSONField(name = "afloatObject")
        public Float aFloatObject;
        @JSONField(name = "aDouble")
        public double aDouble;
        @JSONField(name = "aDoubleObject")
        public Double aDoubleObject;
        @JSONField(name = "boolean")
        public boolean aBoolean;
        @JSONField(name = "aBooleanObject")
        public Boolean aBooleanObject;
        @JSONField(name = "aDoubleArray")
        public double[] doubleArray;
        @JSONField(name = "aDoubleObjectArray")
        public Double[] aDoubleArray;

        @JSONField(name = "aObjectArray")
        public Object[] objects;
        @JSONField(name = "aStringList")
        public List<String> stringList;
        @JSONField(name = "aIntegerList")
        public List<Integer> aIntegerList;
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
