package com.example.feicui.news2.entity;

/**
 * Created by Justin on 2016/6/12.
 */
public class Send {

    private Integer result;
    private String explain;

    public Send(Integer result, String explain) {
        this.result = result;
        this.explain = explain;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
