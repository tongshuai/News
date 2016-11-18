package com.example.feicui.news2.entity;

/**
 * Created by Justin on 2016/6/12.
 */
public class ToEmail {

    private int result;
    private String explain;

    public ToEmail(int result, String explain) {
        this.result = result;
        this.explain = explain;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
