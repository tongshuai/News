package com.example.feicui.news2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.feicui.news2.R;
import com.example.feicui.news2.adapter.NewsAdapter;
import com.example.feicui.news2.adapter.NewsTypeAdapter;
import com.example.feicui.news2.entity.News;
import com.example.feicui.news2.entity.SubType;
import com.example.feicui.news2.model.dao.DBManager;
import com.example.feicui.news2.parser.ParserNews;
import com.example.feicui.news2.ui.MainActivity;
import com.example.feicui.news2.ui.ShowActivity;
import com.example.feicui.news2.utils.CommonUtil;
import com.example.feicui.news2.utils.NewsManager;
import com.example.feicui.news2.utils.SystemUtils;
import com.example.feicui.news2.view.HorizontalListView;
import com.example.feicui.news2.xlistview.XListView;

import java.util.List;

import edu.feicui.mnewsupdate.volley.Response;
import edu.feicui.mnewsupdate.volley.VolleyError;


/**新闻列表界面**/
public class FragmentMain extends Fragment{
	//填充view
	private View view;
	//新闻分类的adapter
	private NewsTypeAdapter newsTypeAdapter;
	private NewsAdapter newsAdapter;
	//下拉刷新的ListView
	private XListView listView;
	//水平的ListView
	private HorizontalListView hl_type;
	//数据库管理类
	private DBManager dbManager;
	//mode代表上拉下拉，上拉为1，下拉为2
	private int mode;
	//当前fragment绑定的activity
	private MainActivity mainActivity;
	//新闻子类id，默认为1
	private int subid = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_newslist,container,false);
		listView= (XListView) view.findViewById(R.id.listview);


		//初始化数据库管理类
		dbManager = DBManager.getInstance(getActivity());
		mainActivity = (MainActivity) getActivity();

		hl_type = (HorizontalListView) view.findViewById(R.id.hl_type);
		if(hl_type!=null){
			newsTypeAdapter = new NewsTypeAdapter(getActivity());
			hl_type.setAdapter(newsTypeAdapter);
			hl_type.setOnItemClickListener(itemClickListener);
		}
		loadNewsType();
		//初始化新闻分类的adapter

		//初始化Xlistview
		listView = (XListView) view.findViewById(R.id.listview);
		if(listView!=null){
			//初始化新闻adapter
			newsAdapter = new NewsAdapter(getActivity(),listView);
			listView.setAdapter(newsAdapter);
			listView.setPullRefreshEnable(true);
			listView.setPullLoadEnable(true);
		}
		loadNextNews(true);
		listView.setXListViewListener(listViewListener);
		listView.setOnItemClickListener(newsItemListener);


		mainActivity.showLoadingDialog(mainActivity,"正在加载",true);
		return view;
	}


	//加载新闻类型
	public void loadNewsType(){
		//如果数据库中已经存储了type数据，则从数据库中获取
		if(dbManager.queryNewsType().size()!=0){
			//通过数据库去查询获取相应的数据
			List<SubType> subTypes = dbManager.queryNewsType();
			//更新adapter
			newsTypeAdapter.appendData(subTypes,true);
			newsTypeAdapter.update();
		}else{
			//如果网络是连接的，则联网获取
			if(SystemUtils.getInstance(getActivity()).isNetConn()){
				NewsManager.loadNewsType(getActivity(),new VolleyTypeResponseHandler()
						,new VolleyErrorResponseHandler());
			}
		}
	}

	//当上拉加载的时候，获取更多数据
	public void loadNextNews(boolean isNewType){
		int nid = 0;
		if (!isNewType) {
			if(newsAdapter.getCount()>0)
				nid = newsAdapter.getItem(0).getNid();
		}
		//mode有两种，next和previous，根据mode（在url中是dir）和nid的值，服务器会返回相应的数据
		mode = NewsManager.MODE_NEXT;
		if(SystemUtils.getInstance(getActivity()).isNetConn()){
			//从网络加载数据
			NewsManager.loadNewsFromServer(getActivity(),nid,subid,mode,new VolleyNewsResponseHandler(),new VolleyErrorResponseHandler());
		}else{
			//如果网络不连接，则从本地（数据库）获取（未完成）
			NewsManager.loadNewsFromLocal(getActivity(), nid, mode, new MyLocalResponseHandler());
		}

	}

	//当下拉刷新的时候加载之前的数据
	public void loadPreNews(){
		//当条目不足一条时返回，不去加载
		if(listView.getCount()-2<=0){
			return;
		}
		//获取nid的值（待测试）
		int nid = newsAdapter.getItem(listView.getLastVisiblePosition()-2).getNid();
		mode = NewsManager.MODE_PREVIOUS;
		if(SystemUtils.getInstance(getActivity()).isNetConn()){
			NewsManager.loadNewsFromServer(getActivity(), nid, subid, mode, new VolleyNewsResponseHandler(), new VolleyErrorResponseHandler());
		}else{
			NewsManager.loadNewsFromLocal(getActivity(), nid, mode, new MyLocalResponseHandler());
		}


	}

	//当从数据库中加载数据时的回调接口的实现
	class MyLocalResponseHandler implements NewsManager.LocalResponseHandler{

		@Override
		public void update(List<News> data,boolean isClearOld) {
			newsAdapter.appendData(data,isClearOld);
			newsAdapter.update();
		}
	}

	//Volley执行联网的时候需要传入的 联网获取数据成功 的 回调 的 实现
	//这个是当联网获取type数据的时候用的回调的实现
	class VolleyTypeResponseHandler implements Response.Listener<String>{

		@Override
		public void onResponse(String response) {
			List<SubType> subTypes = ParserNews.parserTypeList(response);
			dbManager.saveNewsType(subTypes);
			newsTypeAdapter.appendData(subTypes,true);
			newsTypeAdapter.update();
		}
	}
	//同样是Volley需要传入的联网成功的接口实现
	//这个实现类用于联网获取news列表
	class VolleyNewsResponseHandler implements Response.Listener<String>{

		@Override
		public void onResponse(String response) {
			//使用Gson解析json数据
			List<News> newses = ParserNews.praserNewsList(response);
			boolean isClearOld = mode == NewsManager.MODE_NEXT ? true : false;
			newsAdapter.appendData(newses,isClearOld);
			mainActivity.cancelDialog();
			newsAdapter.update();

		}
	}

	//当Volley联网失败的回调
	class VolleyErrorResponseHandler implements Response.ErrorListener{

		@Override
		public void onErrorResponse(VolleyError error) {
			mainActivity.cancelDialog();
			Toast.makeText(getActivity(), "服务器连接异常", Toast.LENGTH_SHORT).show();
		}
	}
	private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener() {
		@Override
		public void onRefresh() {
			loadNextNews(false);
			listView.stopRefresh();
			listView.setRefreshTime(CommonUtil.getSystime());
		}

		@Override
		public void onLoadMore() {
			loadPreNews();
			listView.stopLoadMore();
			listView.setRefreshTime(CommonUtil.getSystime());

		}
	};

	private AdapterView.OnItemClickListener newsItemListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(),ShowActivity.class);
			News news = (News) parent.getItemAtPosition(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("newsitem",news);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			SubType type = (SubType) parent.getItemAtPosition(position);
			subid = type.getSubid();
			newsTypeAdapter.setSelectPosition(position);
			newsTypeAdapter.update();
			loadNextNews(true);
			mainActivity.showLoadingDialog(getActivity(),"正在加载",false);
		}
	};

}
