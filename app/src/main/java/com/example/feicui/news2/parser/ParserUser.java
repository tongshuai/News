package com.example.feicui.news2.parser;

import com.example.feicui.news2.entity.BaseEntity;
import com.example.feicui.news2.entity.Register;
import com.example.feicui.news2.entity.ToEmail;
import com.example.feicui.news2.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Justin on 2016/6/12.
 */
public class ParserUser {


    public static BaseEntity<Register> parserRegister(String json){
        return new Gson().fromJson(json,new TypeToken<BaseEntity<Register>>(){}.getType());
    }

    public static BaseEntity<User> parserUser(String json){
        return new Gson().fromJson(json,new TypeToken<BaseEntity<User>>(){}.getType());
    }

    public static BaseEntity<ToEmail> parserGetPwd(String json) {
        return new Gson().fromJson(json, new TypeToken<BaseEntity<ToEmail>>() {
        }.getType());

    }
}
