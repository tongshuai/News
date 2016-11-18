package com.example.feicui.news2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.entity.BaseEntity;
import com.example.feicui.news2.entity.ToEmail;
import com.example.feicui.news2.parser.ParserUser;
import com.example.feicui.news2.ui.MainActivity;
import com.example.feicui.news2.utils.CommonUtil;
import com.example.feicui.news2.utils.UserManager;

import edu.feicui.mnewsupdate.volley.Response;
import edu.feicui.mnewsupdate.volley.VolleyError;

/**
 * Created by Administrator on 2016/11/10.
 */

public class FragmentForgetpass  extends Fragment {


    private View view;
    private EditText et_email;
    private Button btn_commit;
    private UserManager userManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user_wangjimima,container,false);
        et_email= (EditText) view.findViewById(R.id.et_wangjimima);
        btn_commit= (Button) view.findViewById(R.id.btn_wangjimima);
        btn_commit.setOnClickListener(clickListener);

        return view;
    }
    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            BaseEntity<ToEmail> entity = ParserUser.parserGetPwd(response);
            int status = Integer.parseInt(entity.getStatus());
            ToEmail data = entity.getData();
            if(status==0){
                if(data.getResult()==0){
                    ((MainActivity)getActivity()).showFragmentMain();
                }else if(data.getResult()==-1){
                    et_email.requestFocus();
                }else if(data.getResult()==-2){
                    et_email.requestFocus();
                }
                Toast.makeText(getActivity(), data.getExplain(), Toast.LENGTH_SHORT).show();
            }


        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(), "网络连接异常", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_wangjimima){
                String email = et_email.getText().toString().trim();
                if(!CommonUtil.verifyEmail(email)){
                    Toast.makeText(getActivity(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userManager==null){
                    userManager = UserManager.getInstance(getActivity());
                }

                userManager.forgetPass(getActivity(),listener,errorListener,CommonUtil.VERSION_CODE+"",email);


            }
        }
    };

}
