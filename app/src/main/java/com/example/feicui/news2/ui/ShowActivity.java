package com.example.feicui.news2.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.base.MyBaseActivity;
import com.example.feicui.news2.entity.News;
import com.example.feicui.news2.model.dao.DBManager;
import com.example.feicui.news2.utils.SystemUtils;


public class ShowActivity extends MyBaseActivity {

    private PopupWindow popupWindow;
    private DBManager dbManager;
    private News news;
    private WebView webView;
    private ProgressBar progressBar;
    private ImageView iv_menu;
    private ImageView iv_back;
    private TextView tv_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent=getIntent();
        news= (News) intent.getSerializableExtra("newsitem");


        if(!SystemUtils.getInstance(this).isNetConn()){
            setContentView(R.layout.oh_no);
        }else{
            setContentView(R.layout.activity_show);
            webView = (WebView) findViewById(R.id.webView1);
            progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            iv_back = (ImageView) findViewById(R.id.imageView_back);
            iv_menu = (ImageView) findViewById(R.id.imageView_menu);
            tv_comment = (TextView) findViewById(R.id.textView2);
            dbManager = DBManager.getInstance(this);
            news = (News) getIntent().getSerializableExtra("newsitem");

            iv_back.setOnClickListener(clickListener);
            iv_menu.setOnClickListener(clickListener);
            tv_comment.setOnClickListener(clickListener);

            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }
            });
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);
                    if(newProgress>=100){
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            webView.loadUrl(news.getLink());
            initPopupWindow();
        }

    }

    private void initPopupWindow() {
        View popView = getLayoutInflater().inflate(R.layout.item_pop_save, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置可触摸popupWindow之外的地方
//        popupWindow.setOutsideTouchable(true);
        TextView tv_sacelocal = (TextView) popView.findViewById(R.id.saveLocal);
        tv_sacelocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                boolean isSuccess = dbManager.saveLoveNews(news);
                if(isSuccess){
                    Toast.makeText(ShowActivity.this, "收藏成功！ \n 在主界面侧滑菜单中查看", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ShowActivity.this, "已经收藏过这条新闻了！ \n 在主界面侧滑菜单中查看", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView_menu:
                    if(popupWindow!=null && popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }else{
                        popupWindow.showAsDropDown(iv_menu,0,12);
                    }

                    break;
                case R.id.imageView_back:
                    finish();
                    break;
                case R.id.textView2:
                    Intent intent = new Intent(ShowActivity.this,CommentActivity.class);
                    intent.putExtra("nid",news.getNid());
                    startActivity(intent);
                    break;
            }

        }
    };
}
