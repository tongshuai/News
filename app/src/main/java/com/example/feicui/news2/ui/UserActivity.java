package com.example.feicui.news2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.adapter.LoginLogAdapter;
import com.example.feicui.news2.base.MyBaseActivity;
import com.example.feicui.news2.entity.BaseEntity;
import com.example.feicui.news2.entity.LoginLog;
import com.example.feicui.news2.entity.Register;
import com.example.feicui.news2.entity.User;
import com.example.feicui.news2.model.dao.LoadImage;
import com.example.feicui.news2.parser.ParserUser;
import com.example.feicui.news2.utils.CommonUtil;
import com.example.feicui.news2.utils.SharedPreferencesUtils;
import com.example.feicui.news2.utils.SystemUtils;
import com.example.feicui.news2.utils.UserManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import edu.feicui.mnewsupdate.volley.Response;
import edu.feicui.mnewsupdate.volley.VolleyError;

public class UserActivity extends MyBaseActivity implements LoadImage.ImageLoadListener {

    //选取图片的requestCode
    private static final int PHOTO_GALLARY = 300;
    //cut图片的requestCode
    private static final int PHOTO_REQUEST_CUT = 400;
    //用来显示popupwindow的parent
    private LinearLayout layout;
    //图片bitmap
    private Bitmap alterBitmap,bitmap;

    private ImageView iv_return,iv_user;
    private TextView tv_name,tv_namexia,tv_jishu;
    private ListView lv_rizhi;
    private Button btn_exit;
    private LoginLogAdapter loginLogAdapter;
    //图片的本地文件
    private File file;
    private String token;
    //加载图片工具类
    private LoadImage loadImage;

    //popupwindow
    private PopupWindow popupWindow;

    //选择图片
    private LinearLayout photo_take,photo_sel;
    //本地存储图片的路径
    private String localpath;


    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initView();
        initData();
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sp.getString("username", "游客");
        tv_name.setText(username);
        localpath = sp.getString("localpath", "");
        if(alterBitmap!=null){
            iv_user.setImageBitmap(bitmap);
        }else
        if(!localpath.equals("")){
            bitmap = BitmapFactory.decodeFile(localpath);
            iv_user.setImageBitmap(bitmap);
        }

        initPopWindow();

    }

    private void initView(){

        loadImage = new LoadImage(this,this);


        layout= (LinearLayout) findViewById(R.id.activity_user);
        iv_return= (ImageView) findViewById(R.id.iv_title_layout);
        iv_user= (ImageView) findViewById(R.id.iv_user);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_namexia= (TextView) findViewById(R.id.tv_namexia);
        tv_jishu= (TextView) findViewById(R.id.tv_jishu);
        lv_rizhi= (ListView) findViewById(R.id.lv_rizhi);
        btn_exit= (Button) findViewById(R.id.btn_tuichu);
        loginLogAdapter=new LoginLogAdapter(this);
        lv_rizhi.setAdapter(loginLogAdapter);


        iv_return.setOnClickListener(clickListener);
        iv_user.setOnClickListener(clickListener);
        btn_exit.setOnClickListener(clickListener);

    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /***封装请求 Gallery 的 intent*//*
    public Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");
        intent.putExtra("crop", "true");//设置裁剪功能
        intent.putExtra("aspectX", 1); //宽高比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80); //宽高值
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true); //返回裁剪结果
        return intent;
    }*/

    private void save(Bitmap bitmap){
        if(bitmap==null){
            return;
        }
        roundPic();
        iv_user.setImageBitmap(alterBitmap);
        File rootDir = Environment.getExternalStorageDirectory();
        File dir = new File(rootDir,"azynews");
        dir.mkdirs();
        file = new File(dir,"userpic.jpg");
        try {
            OutputStream os = new FileOutputStream(file);
            if(alterBitmap.compress(Bitmap.CompressFormat.JPEG,100,os)){
                UserManager.getInstance(this).changePhoto(this,token,file,listener,errorListener);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("localpath", file.getAbsolutePath());
                editor.commit();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void roundPic() {

        Bitmap backBp = BitmapFactory.decodeResource(getResources(),R.drawable.userbg);
        alterBitmap = Bitmap.createBitmap(backBp.getWidth(), backBp.getHeight(), backBp.getConfig());
        Canvas canvas = new Canvas(alterBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //画背景
        canvas.drawBitmap(backBp,new Matrix(),paint);
        //画用户头像图片

        //模式是SRC_IN的时候有一个难看的黑框
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        bitmap = Bitmap.createScaledBitmap(bitmap,backBp.getWidth(),backBp.getHeight(),true);
        canvas.drawBitmap(bitmap,new Matrix(),paint);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            Log.e("TAG", "onActivityResult: 100"+" "+bitmap.getHeight() );
            save(bitmap);

        }else if(requestCode==PHOTO_GALLARY){
            if(data!=null){
                Uri uri = data.getData();
                crop(uri);
            }
        }else if(requestCode==PHOTO_REQUEST_CUT){
            bitmap = data.getParcelableExtra("data");
            save(bitmap);
        }
    }

    @Override
    public void imageLoadOK(Bitmap bitmap, String url) {

        iv_user.setImageBitmap(bitmap);
    }


    private void initData(){
        token = SharedPreferencesUtils.getToken(this);
        UserManager.getInstance(this).getUserInfo(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseEntity<User> entity = ParserUser.parserUser(response);
                if(Integer.parseInt(entity.getStatus())!=0){
                    return;
                }
                SharedPreferencesUtils.saveUser(UserActivity.this,entity);
                User user = entity.getData();
                tv_name.setText(user.getUid());

                Log.e("TAG", "onResponse: uid="+user.getUid() );
//                if(!TextUtils.isEmpty(user.getPortrait())&&localpath.equals("")){
//                    loadImage.getBitmap(user.getPortrait(),iv_user);
//                }
                Log.e("TAG", "onResponse: integral="+user.getIntegration() );

                tv_namexia.setText("积分："+user.getIntegration()+"");
                tv_jishu.setText(+user.getComnum()+"");
                List<LoginLog> loginlog = user.getLoginlog();
                loginLogAdapter.appendData(loginlog,true);
                loginLogAdapter.update();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();

            }
        }, CommonUtil.VERSION_CODE+"",token, SystemUtils.getIMEI(this));

    }

    private void initPopWindow() {
        View contentView = View.inflate(this, R.layout.item_pop_selectpic, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        photo_take = (LinearLayout) contentView.findViewById(R.id.photo_take);
        photo_sel = (LinearLayout) contentView.findViewById(R.id.photo_sel);
        photo_sel.setOnClickListener(clickListener);
        photo_take.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_user://选择照片或拍照的popupWindow
                    popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
                    break;
                case R.id.iv_title_layout:
                    startActivity(new Intent(UserActivity.this,MainActivity.class));
                    finish();
                    break;
                case R.id.photo_take://拍照
                    popupWindow.dismiss();
                    takePhoto();
                    break;
                case R.id.photo_sel:
                    popupWindow.dismiss();
//                    selectPhoto();
                    gallary();
                    break;
                case R.id.btn_tuichu:
                    SharedPreferencesUtils.clearUser(UserActivity.this);
                    startActivity(new Intent(UserActivity.this,MainActivity.class));
                    finish();
            }
        }
    };
    public void gallary(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PHOTO_GALLARY);
    }

    protected void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }

    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            BaseEntity<Register> entity = ParserUser.parserRegister(response);
            if(entity.getData().getResult().equals("0")){
                iv_user.setImageBitmap(bitmap);
            }
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(UserActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
        }
    };


}
