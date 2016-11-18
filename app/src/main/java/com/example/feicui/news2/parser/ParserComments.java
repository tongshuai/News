package com.example.feicui.news2.parser;

import com.example.feicui.news2.entity.BaseEntity;
import com.example.feicui.news2.entity.Comment;
import com.example.feicui.news2.entity.Send;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


/**
 * Created by Justin on 2016/6/12.
 */
public class ParserComments {


    //解析评论集合的数据
    public static List<Comment> parserComments(String json){
        BaseEntity<List<Comment>> entity = new Gson().fromJson(json, new TypeToken<BaseEntity<List<Comment>>>() {
        }.getType());

        return entity.getData();

    }

    //返回值为当前发布评论是否成功的状态（status）
    public static int parserSendComment(String json){
        BaseEntity<Send> entity = new Gson().fromJson(json, new TypeToken<BaseEntity<Send>>() {
        }.getType());

        return Integer.parseInt(entity.getStatus());
    }

}
