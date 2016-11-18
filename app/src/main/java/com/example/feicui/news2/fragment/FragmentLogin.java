package com.example.feicui.news2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.entity.BaseEntity;
import com.example.feicui.news2.entity.Register;
import com.example.feicui.news2.parser.ParserUser;
import com.example.feicui.news2.ui.MainActivity;
import com.example.feicui.news2.ui.UserActivity;
import com.example.feicui.news2.utils.CommonUtil;
import com.example.feicui.news2.utils.SharedPreferencesUtils;
import com.example.feicui.news2.utils.UserManager;

import edu.feicui.mnewsupdate.volley.Response;
import edu.feicui.mnewsupdate.volley.VolleyError;


/**
 * Created by Administrator on 2016/11/9.
 */

public class FragmentLogin extends Fragment{

    private View view;
    private EditText et_zhanghao,et_mima;
    private Button btn_login;
    private TextView tv_wangjimima,tv_zhuce;
    private UserManager userManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user_denglu,container,false);
        et_zhanghao= (EditText) view.findViewById(R.id.et_login_zhanghao);
        et_mima= (EditText) view.findViewById(R.id.et_login_mima);
        btn_login= (Button) view.findViewById(R.id.btn_login);
        tv_wangjimima= (TextView) view.findViewById(R.id.tv_login_wangjimima);
        tv_zhuce= (TextView) view.findViewById(R.id.tv_login_zhuce);

        btn_login.setOnClickListener(clickListener);
        tv_wangjimima.setOnClickListener(clickListener);
        tv_zhuce.setOnClickListener(clickListener);

        return view;
    }

    private Response.Listener<String> listener=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            BaseEntity<Register> entity= ParserUser.parserRegister(response);
            int status=Integer.parseInt(entity.getStatus());
            String result="";
            if(status==0){
                result = "login success!";
                SharedPreferencesUtils.saveRegister(getActivity(),entity);

                startActivity(new Intent(getActivity(), UserActivity.class));

            }else if(status==-3){
                result = "wrong username or password!";

            }else{
                result = "login failure";
            }

            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();


        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(), "network disconnect", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_login_zhuce:
                    ((MainActivity)getActivity()).showFragmentRegister();
                    break;
                case R.id.tv_login_wangjimima:
                    ((MainActivity)getActivity()).showFragmentForget();
                    break;
                case R.id.btn_login:
                    String name = et_zhanghao.getText().toString().trim();
                    String pwd = et_mima.getText().toString().trim();

                    if(name==null||name.equals("")){
                        Toast.makeText(getActivity(), "username can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pwd.length()<6||pwd.length()>22){
                        Toast.makeText(getActivity(), "can't longer than 22 or can't shorter than 6", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(userManager==null){
                        userManager = UserManager.getInstance(getActivity());
                    }

                    userManager.login(getActivity(),listener,errorListener, CommonUtil.VERSION_CODE+"",name,pwd,0+"");
                    break;

            }

        }
    };

}
