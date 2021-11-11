package com.donfyy.crowds.asm;

import com.alibaba.fastjson.annotation.JSONField;

public class XxxPlugin {
    private static class XxxRequest extends BaseModel {
        @JSONField(name = "xpath")
        @RequiredParam
        public String path;
    }
}
