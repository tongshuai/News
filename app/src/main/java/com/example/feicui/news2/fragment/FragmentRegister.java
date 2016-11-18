package com.example.feicui.news2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class FragmentRegister extends Fragment {

    private View view;
    private EditText et_email;
    private EditText et_name;
    private EditText et_pwd;
    private Button btn_commit;
    private CheckBox checkBox;

    private UserManager userManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user_zhuce,container,false);
        btn_commit = (Button) view.findViewById(R.id.btn_zhuce);
        et_email = (EditText) view.findViewById(R.id.et_register_email);
        et_name = (EditText) view.findViewById(R.id.et_register_name);
        et_pwd = (EditText) view.findViewById(R.id.et_register_pwd);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        btn_commit.setOnClickListener(clickListener);
        return view;
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!checkBox.isChecked()){
                Toast.makeText(getActivity(), "没有同意条款！", Toast.LENGTH_SHORT).show();
                return;
            }
            String email = et_email.getText().toString().trim();
            String name = et_name.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();
            if(!CommonUtil.verifyEmail(email)){
                Toast.makeText(getActivity(), "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(name)){
                Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!CommonUtil.verifyPassword(pwd)){
                Toast.makeText(getActivity(), "请输入6-22位包含数字和字母的密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if(userManager==null){
                userManager = UserManager.getInstance(getActivity());
            }
            userManager.register(getActivity(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BaseEntity<Register> entity = ParserUser.parserRegister(response);

                    Register data = entity.getData();
                    Log.d("DDD", "请求成功");
                    int status = Integer.parseInt(entity.getStatus());
                    Log.d("DDD", ""+status);
                    if(status==0){
                        String result = data.getResult();
                        String explain = data.getExplain();
                        if(data.getResult().equals("0")){
                            //登录成功
                            SharedPreferencesUtils.saveRegister(getActivity(),entity);
                            startActivity(new Intent(getActivity(),UserActivity.class));
                            Log.d("DDD", "onResponse: ");
                            getActivity().overridePendingTransition(R.anim.anim_activity_right_in,R.anim.anim_activity_bottom_out);

                            ((MainActivity)getActivity()).changeFragmentUser();

                        }
                        Toast.makeText(getActivity(), explain, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("DDD", "请求失败 ");
                }
            }, CommonUtil.VERSION_CODE+"",name,email,pwd);
        }
    };
}
