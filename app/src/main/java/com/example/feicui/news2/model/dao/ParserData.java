package com.example.feicui.news2.model.dao;

import com.example.feicui.news2.entity.News;
import com.google.gson.Gson;


import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class ParserData {

    //Gson解析
    public static List<News> parserItemData(String json){
        Gson gson = new Gson();
//        SearchResult result = gson.fromJson(json, new TypeToken<SearchResult>() {
//        }.getType());

//        return result.getItems();
          return null;

    }

}
