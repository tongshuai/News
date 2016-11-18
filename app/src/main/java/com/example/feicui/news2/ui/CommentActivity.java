package com.example.feicui.news2.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.adapter.CommentsAdapter;
import com.example.feicui.news2.base.MyBaseActivity;
import com.example.feicui.news2.entity.Comment;
import com.example.feicui.news2.parser.ParserComments;
import com.example.feicui.news2.utils.CommentsManager;
import com.example.feicui.news2.utils.CommonUtil;
import com.example.feicui.news2.utils.NewsManager;
import com.example.feicui.news2.utils.SharedPreferencesUtils;
import com.example.feicui.news2.utils.SystemUtils;
import com.example.feicui.news2.xlistview.XListView;

import java.util.List;

import edu.feicui.mnewsupdate.volley.Response;
import edu.feicui.mnewsupdate.volley.VolleyError;


public class CommentActivity extends MyBaseActivity {

    private XListView listView;
    private CommentsAdapter adapter;

    //刷新模式
    private int mode;

    //发送评论
    private ImageView iv_send;

    //输入评论
    private EditText et_comment;

    //退出当前页面
    private ImageView iv_back;

    //从intent中携带过来
    private int nid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        nid = getIntent().getIntExtra("nid",-1);
        listView = (XListView) findViewById(R.id.listview);
        adapter = new CommentsAdapter(this,listView);
        listView.setAdapter(adapter);
        iv_send = (ImageView) findViewById(R.id.imageview);
        iv_back = (ImageView) findViewById(R.id.imageView_back);
        et_comment = (EditText) findViewById(R.id.edittext_comment);
        loadNextComment();
        //点击事件
        listView.setXListViewListener(xlistviewListener);
        iv_back.setOnClickListener(clickListener);
        iv_send.setOnClickListener(clickListener);


    }

    //加载之前的评论
    private void loadPreComment(){
        Comment comment = adapter.getItem(listView.getLastVisiblePosition()-2);
        mode = NewsManager.MODE_PREVIOUS;
        if(SystemUtils.getInstance(this).isNetConn()){
            CommentsManager.loadComments(this, CommonUtil.VERSION_CODE+"",listener,errorListener,nid,mode,comment.getCid());
        }
    }
    //加载后面的评论
    private void loadNextComment(){
        int curId = adapter.getAdapterData().size()<=0?0:adapter.getItem(0).getCid();
        mode = NewsManager.MODE_NEXT;
        if(SystemUtils.getInstance(this).isNetConn()){
            CommentsManager.loadComments(this,CommonUtil.VERSION_CODE+"",listener,errorListener,nid,mode,curId);
        }
    }

    //volley的成功监听
    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Comment> comments = ParserComments.parserComments(response);
            if(comments==null||comments.size()<=0){
                return;
            }
            boolean flag = mode==NewsManager.MODE_NEXT?true:false;
            adapter.appendData(comments,flag);
            adapter.update();
        }
    };

    //volley的失败监听
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(CommentActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
        }
    };


    //xlistview的刷新和加载的监听
    private XListView.IXListViewListener xlistviewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {

        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView_back://back
                    finish();
                    break;
                case R.id.imageview://Send comment
                    String content = et_comment.getText().toString();
                    //评论内容为空时return
                    if(content==null||content.equals("")){
                        Toast.makeText(CommentActivity.this, "please input message!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //从sp中获取token（还没有存储）
                    final String token = SharedPreferencesUtils.getToken(CommentActivity.this);
                    if(TextUtils.isEmpty(token)){
                        Toast.makeText(CommentActivity.this, "not yet login", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //当点击了发送按钮之后，暂时让此按钮失去点击效果，当联网结束后恢复
                    iv_send.setEnabled(false);

                    String imei = SystemUtils.getIMEI(CommentActivity.this);

                    //联网，获取数据
                    CommentsManager.sendComment(CommentActivity.this, nid, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int status = ParserComments.parserSendComment(response);
                            if(status==0){
                                Toast.makeText(CommentActivity.this, "comment success!", Toast.LENGTH_SHORT).show();
                                iv_send.setEnabled(true);
                                et_comment.setText(null);
                                et_comment.clearFocus();
                            }else{
                                Toast.makeText(CommentActivity.this, "comment failed!", Toast.LENGTH_SHORT).show();
                                iv_send.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CommentActivity.this, "check your network!", Toast.LENGTH_SHORT).show();
                            iv_send.setEnabled(true);
                        }
                    },CommonUtil.VERSION_CODE+"",token,imei,content);

                    break;
            }
        }
    };
}
