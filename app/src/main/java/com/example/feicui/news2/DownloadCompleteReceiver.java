package com.example.feicui.news2;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Justin on 2016/6/3.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = downloadManager.query(query);
            String fileName = "";
            if(cursor.moveToFirst()){
                String string = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                fileName = string.replace("file://","");
            }

            File file = new File(fileName);

            installApk(file,context);

            context.unregisterReceiver(this);

        }
    }

    /**
     * MIME_MapTable是所有文件的后缀名所对应的MIME类型的一个String数组
     *  {".apk",    "application/vnd.android.package-archive"}
     */
    private void installApk(File file, Context context) {
        if(!file.exists()){
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}

