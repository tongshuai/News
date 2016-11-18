package com.example.feicui.news2.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.base.MyBaseActivity;
import com.example.feicui.news2.fragment.FragmentFavorite;
import com.example.feicui.news2.fragment.FragmentForgetpass;
import com.example.feicui.news2.fragment.FragmentLogin;
import com.example.feicui.news2.fragment.FragmentMain;
import com.example.feicui.news2.fragment.FragmentMenu;
import com.example.feicui.news2.fragment.FragmentMenuRight;
import com.example.feicui.news2.fragment.FragmentRegister;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import static com.example.feicui.news2.R.id.activity_fragment;
import static com.example.feicui.news2.R.id.tv_title;


public class MainActivity extends MyBaseActivity {


    private SlidingMenu slidingMenu;
    private FragmentMenu fragmentMenu;
    private FragmentMain fragmentMain;
    private FragmentMenuRight fragmentMenuRight;
    private ImageView imageView_left;
    private ImageView imageView_right;
    private TextView textView_title;
    private FragmentLogin fragmentLogin;
    private FragmentRegister fragmentRegister;
    private FragmentForgetpass fragmentForgetPass;
    private FragmentFavorite fragmentFavorite;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getviewID();

        initSlidingMenu();
         homejiemian();
    }

    public void initSlidingMenu(){

        fragmentMenu=new FragmentMenu();
        fragmentMenuRight=new FragmentMenuRight();

        slidingMenu=new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.activity_main_pianyiliang);
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.fragment_none1);
        slidingMenu.setSecondaryMenu(R.layout.fragment_none2);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_none1,fragmentMenu).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_none2,fragmentMenuRight).commit();

    }

    private void homejiemian(){
        fragmentMain=new FragmentMain();
        getSupportFragmentManager().beginTransaction().add(activity_fragment,fragmentMain).commit();


    }
    private void getviewID(){
        textView_title= (TextView) findViewById(tv_title);
        imageView_left= (ImageView) findViewById(R.id.iv_title_left);
        imageView_right= (ImageView) findViewById(R.id.iv_title_right);
        imageView_left.setOnClickListener(listener);
        imageView_right.setOnClickListener(listener);


    }

    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_title_left:
                    slidingMenu.showMenu();
                    break;
                case R.id.iv_title_right:
                    slidingMenu.showSecondaryMenu();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(slidingMenu.isMenuShowing()||slidingMenu.isSecondaryMenuShowing()){
            slidingMenu.showContent();
        }else{
            exitTwice();
        }


    }

    private boolean isFirstExit = true;
    private void exitTwice() {
        if(isFirstExit){
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            isFirstExit = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        isFirstExit = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            finish();
        }
    }

    public void showFragmentMain() {
        setTitle("资讯");
        slidingMenu.showContent();
        if(fragmentMain==null){
            fragmentMain = new FragmentMain();
        }

        getSupportFragmentManager().beginTransaction().replace(activity_fragment, fragmentMain).commit();
    }

    private void setTitle(String title){
        if(title!=null){
            textView_title.setText(title);
        }
    }
    public void showFragmentFavorite() {
        setTitle("收藏");
        slidingMenu.showContent();
        if(fragmentFavorite==null){
            fragmentFavorite = new FragmentFavorite();
        }

        getSupportFragmentManager().beginTransaction().replace(activity_fragment, fragmentFavorite).commit();
    }

    public void showFragmentRegister() {
        setTitle("注册");
        slidingMenu.showContent();
        if(fragmentRegister==null){
            fragmentRegister = new FragmentRegister();
        }

        getSupportFragmentManager().beginTransaction().replace(activity_fragment, fragmentRegister).commit();
    }

    public void changeFragmentUser() {
        if(fragmentMenuRight==null){
            fragmentMenuRight = new FragmentMenuRight();
        }
        fragmentMenuRight.switchView();
    }

    public void showFragmentLogin() {
        setTitle("登录");
        slidingMenu.showContent();
        if(fragmentLogin==null){
            fragmentLogin = new FragmentLogin();
        }

        getSupportFragmentManager().beginTransaction().replace(activity_fragment, fragmentLogin).commit();
    }

    public void showFragmentForget() {
        setTitle("忘记密码");
        slidingMenu.showContent();
        if(fragmentForgetPass==null){
            fragmentForgetPass = new FragmentForgetpass();
        }
        getSupportFragmentManager().beginTransaction().replace(activity_fragment, fragmentForgetPass).commit();
    }
}
