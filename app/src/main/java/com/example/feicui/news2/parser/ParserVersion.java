package com.example.feicui.news2.parser;

import com.example.feicui.news2.entity.Version;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Created by Justin on 2016/6/3.
 */
public class ParserVersion {


    public static Version parserJson(String jsonObject){
        Gson gson = new Gson();
        Version version = gson.fromJson(jsonObject,new TypeToken<Version>(){}.getType());
        return version;
    }


}
