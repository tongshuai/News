package com.example.feicui.news2.utils;

import android.content.Context;

import com.example.feicui.news2.entity.News;
import com.example.feicui.news2.model.dao.DBManager;
import com.example.feicui.news2.model.volleys.VolleyHttp;

import java.util.List;

import edu.feicui.mnewsupdate.volley.Response;


/**
 * Created by Justin on 2016/6/6.
 */
public class NewsManager {

    public static final int MODE_NEXT = 1;
    public static final int MODE_PREVIOUS = 2;


    /**
     * news_sort?ver=版本号&imei=手机标识符 加载新闻分类
     *
     * @param context       :上下文
     * @param listener      :成功回调接口
     * @param errorListener :失败回调接口
     *                      <p/>
     *                      http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1
     */
    public static void loadNewsType(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        int ver = CommonUtil.VERSION_CODE;
        String imei = SystemUtils.getIMEI(context);
        VolleyHttp http = new VolleyHttp(context);
        http.getJSONObject(CommonUtil.APPURL+"/news_sort?ver="+ver+"&imei="+imei,listener,errorListener);


    }

    /**
     * news_list?ver=版本号&subid=分类名&dir=1&nid=新闻id&stamp=20140321&cnt=20 加载新闻数据
     *
     * @param mode          模式/方向
     * @param subid           分类号
     * @param nid           新闻id
     * @param listener      成功回调接口
     * @param errorListener 失败回调接口
     */
    public static void loadNewsFromServer(Context context, int nid, int subid, int mode, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        int ver = CommonUtil.VERSION_CODE;
        String imei = SystemUtils.getIMEI(context);
        VolleyHttp http = new VolleyHttp(context);
        http.getJSONObject(CommonUtil.APPURL+"/news_list?ver="+ver+"&subid="+subid+"&dir="+mode+"&nid="+nid+"&stamp="+CommonUtil.getDate()+"&cnt="+20,
                listener,errorListener);

    }



    public static void loadNewsFromLocal(Context context,int nid,int mode,LocalResponseHandler handler){
        if(mode==1){
            List<News> newses = DBManager.getInstance(context).queryNews();
            handler.update(newses,true);
        }else if(mode==2){
            List<News> newses = DBManager.getInstance(context).queryNews();
            handler.update(newses,false);
        }

    }


    public interface LocalResponseHandler{
        public void update(List<News> data, boolean isClearOld);
    }


}
