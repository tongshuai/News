package com.example.feicui.news2.biz;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.example.feicui.news2.model.volleys.VolleyHttp;
import com.example.feicui.news2.utils.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.feicui.mnewsupdate.volley.Response;


/**
 * Created by Justin on 2016/6/3.
 */
public class UpdateManager {

    /**
     *
     * @param context
     * @param url
     */
    public static void downLoad(Context context, String url){
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI );
        //{@link #setNotificationVisibility(int)}
        request.setShowRunningNotification(true);//已被上面的方法代替
        request.setVisibleInDownloadsUi(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        String date = dateFormat.format(new Date());

        request.setDestinationInExternalFilesDir(context, null, date + ".apk");
        manager.enqueue(request);

    }

    public static void judgeUpdate(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener, String ...args){
        String url = CommonUtil.APPURL + "update?imei=" + args[0] + "&pkg=" + args[1] + "&ver=" + args[2];
        new VolleyHttp(context).getJSONObject(url, listener, errorListener);
    }

}
