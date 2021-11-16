package com.donfyy.crowds;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ApiNameList {
    @JSONField(name = "apiNameList")
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}