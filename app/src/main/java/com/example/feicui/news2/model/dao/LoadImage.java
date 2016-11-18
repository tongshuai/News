package com.example.feicui.news2.model.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Justin on 2016/6/8.
 */
public class LoadImage {

    private Context context;
    private LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(1024*1024*3){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight();
        }
    };
    private ImageLoadListener listener;

    public interface ImageLoadListener{
        void imageLoadOK(Bitmap bitmap, String url);
    }

    public LoadImage(Context context,ImageLoadListener listener){
        this.context = context;
        this.listener = listener;
    }


    public void getBitmap(String url, ImageView iv){
        Bitmap bitmap = null;

        if(url==null || url.length()<=0){
            return;
        }

        bitmap = getBitmapFromCache(url);
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
        }

        bitmap = getBitmapFromCacheFile(url);
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
        }

        getBitmapAsync(url);



    }

    private void getBitmapAsync(String url) {
        ImageAsyncTask task = new ImageAsyncTask();
        task.execute(url);
    }


    //泛型类型：params代表"doInBackground()方法中的方法参数类型"
    //Progress:代表publishProgress()方法的方法参数类型
    //Result:代表onPostExecute()方法的方法的参数类型
    class ImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

        String path = "";

        //当分线程任务执行之前，在主线程处理的任务，用来做一些UI的准备工作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        //在分线程执行耗时操作，这个是主要的操作
        @Override
        protected Bitmap doInBackground(String... params) {
            //用来发布进度
//            publishProgress();

            Bitmap bitmap = null;
            try {
                path = params[0];
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                cache.put(path,bitmap);
                saveBitmapToChcheFile(params[0],bitmap);
                Log.e("TAG", "doInBackground: " );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }


        //当在分线程调用了publishProgress()方法之后，会回调下面这个方法，用来更新进度
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //当分线程任务结束后，会调用下面这个方法，并且分线程任务执行完会把执行结果传过来，这里可以用这个结果执行一些主线程的任务
        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
            listener.imageLoadOK(bitmap,path);
        }
    }

    /**
     * http://192.168.1.43/newsClient/Images/232342422223.jpg
     */
    private void saveBitmapToChcheFile(String url, Bitmap bitmap) {
        String name = url.substring(url.lastIndexOf("/")+1);
        File cacheDir = context.getCacheDir();

        File BitFile = new File(cacheDir,name);
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(BitFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Bitmap getBitmapFromCache(String url){
        Bitmap bitmap = cache.get(url);
        return bitmap;
    }

    private Bitmap getBitmapFromCacheFile(String url){
        Bitmap bitmap = null;
        String name = url.substring(url.lastIndexOf("/")+1);
        File cacheDir = context.getCacheDir();

        File bitfile = null;
        File[] files = cacheDir.listFiles();
        if(files==null){
            return bitmap;
        }
        for(int i = 0;i<files.length;i++){
            if(name.equals(files[i].getName())){
                bitfile = files[i];
                break;
            }
        }
        if(bitfile==null){
            return bitmap;
        }
        bitmap = BitmapFactory.decodeFile(bitfile.getAbsolutePath());
        return bitmap;

    }


}
