package com.example.feicui.news2.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feicui.news2.R;
import com.example.feicui.news2.ui.MainActivity;


/**
 * Created by Justin on 2016/6/3.
 */
public class FragmentMenuRight extends Fragment{


	private  View view;
	private SharedPreferences sharedPreferences;
	private TextView tv_login,tv_updata;
	private ImageView iv_photo;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view=inflater.inflate(R.layout.fragment_menu_right,container,false);
		tv_login= (TextView) view.findViewById(R.id.tv1_fragment_right);
		iv_photo= (ImageView) view.findViewById(R.id.iv_fragment_right);
		tv_updata= (TextView) view.findViewById(R.id.tv2_fragment_right);

		tv_login.setOnClickListener(listener);
		return view;
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.tv1_fragment_right:
					((MainActivity)getActivity()).showFragmentLogin();
					break;
			}

		}
	};

	//当登录后改变右侧菜单的视图，变为已登录的视图
	public void switchView() {
//		if(islogin){
//			relativelayout_unlogin.setVisibility(View.GONE);
//			relativelayout_logined.setVisibility(View.VISIBLE);
//			initUserInfo();
//		}else{
//			relativelayout_logined.setVisibility(View.GONE);
//			relativelayout_unlogin.setVisibility(View.VISIBLE);
//		}

	}


}
