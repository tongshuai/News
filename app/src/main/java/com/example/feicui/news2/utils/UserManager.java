package com.example.feicui.news2.utils;

import android.content.Context;

import com.example.feicui.news2.model.volleys.VolleyHttp;

import java.io.File;

import edu.feicui.mnewsupdate.volley.Response;


/**
 * Created by Justin on 2016/6/1.
 */
public class UserManager {

    private Context context;
    private String imei;
    private static UserManager userManager;

    private UserManager(Context context) {
        this.context = context;
        imei = SystemUtils.getIMEI(context);
    }

    public static UserManager getInstance(Context context) {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager(context);
                }
            }
        }
        return userManager;

    }


    /**
     * user_login?ver=版本号&uid=用户名&pwd=密码&device=0
     *
     * @param args          包含参数如下： ver : 版本 uid : 用户昵称 pwd : 密码 imei : 手机IMEI号 device :
     *                      登录设备 ： 0 为移动端 ，1为PC端
     * @param listener
     * @param errorListener
     */
    public void login(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener, String... args) {
//        LogUtil.e(LogUtil.TAG, "执行登录");
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL + "/user_login?ver=" + args[0] + "&uid=" + args[1] + "&pwd=" + args[2] + "&device=" + args[3], listener, errorListener);
    }

    /**
     * user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
     *
     * @param context
     * @param listener
     * @param errorListener
     * @param args
     */
    public void register(Context context, Response.Listener<String> listener,
                         Response.ErrorListener errorListener, String... args) {
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL + "/user_register?ver=" + args[0] + "&uid=" + args[1] + "&email=" + args[2] + "&pwd=" + args[3], listener, errorListener);

    }

    /***
     * user_forgetpass?ver=版本号&email=邮箱
     *
     * @param args ver：版本号
     *             email：邮箱
     */
    public void forgetPass(Context context, Response.Listener<String> listener,
                           Response.ErrorListener errorListener, String... args) {
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL
                        + "/user_forgetpass?ver=" + args[0] + "&email=" + args[1],
                listener, errorListener);
    }

    /**
     * user_home?ver=版本号&imei=手机标识符&token =用户令牌
     */
    public void getUserInfo(Context context, Response.Listener<String> listener,
                            Response.ErrorListener errorListener, String... args){
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL
                + "/user_home?ver=" + args[0] + "&token=" + args[1] + "&imei="
                + args[2], listener, errorListener);
    }

    /**
     * user_image?token=用户令牌& portrait =头像
     * @param context
     * @param token
     * @param file
     * @param listener
     * @param errorListener
     */
    public void changePhoto(Context context, String token, File file,
                            Response.Listener<String> listener, Response.ErrorListener errorListener){

        new VolleyHttp(context).upLoadImage(CommonUtil.APPURL+"/user_image?token="+token,file,listener,errorListener);

    }
}
