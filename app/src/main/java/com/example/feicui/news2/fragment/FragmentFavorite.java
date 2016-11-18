package com.example.feicui.news2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.feicui.news2.R;
import com.example.feicui.news2.adapter.NewsAdapter;
import com.example.feicui.news2.entity.News;
import com.example.feicui.news2.model.dao.DBManager;
import com.example.feicui.news2.ui.ShowActivity;

import java.util.ArrayList;


/**
 * Created by Justin on 2016/6/8.
 */
public class FragmentFavorite extends Fragment {

    private DBManager dbManager;
    private View view;
    private ListView listView;
    private NewsAdapter newsAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        dbManager = DBManager.getInstance(getActivity());
        listView = (ListView) view.findViewById(R.id.listview);
        newsAdapter = new NewsAdapter(getActivity(), listView);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(itemClickListener);
        loadLoveNews();
        return view;
    }

    private void loadLoveNews(){
        ArrayList<News> newses = dbManager.queryLoveNews();
        newsAdapter.appendData(newses, true);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            News news = (News) parent.getItemAtPosition(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("newsitem", news);
            Intent intent = new Intent(getActivity(), ShowActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };




}
