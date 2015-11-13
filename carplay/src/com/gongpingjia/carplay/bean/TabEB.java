package com.gongpingjia.carplay.bean;

import java.util.Map;

public class TabEB {
    int index;
    Map<String, Object> params;

    public TabEB() {
    }

    public TabEB(int index, Map<String, Object> params) {
        this.index = index;
        this.params = params;
    }

    public int getIndex() {
        return index;
    }

    public Map<String, Object> getParams() {
        return params;
    }

}
