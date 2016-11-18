package com.example.feicui.news2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.feicui.news2.model.dao.LoadImage;
import com.example.feicui.news2.R;
import com.example.feicui.news2.base.MyBaseAdapter;
import com.example.feicui.news2.entity.News;




/**
 * Created by Justin on 2016/6/6.
 */
public class NewsAdapter extends MyBaseAdapter<News> {

    private Bitmap defaultBitmap;
    private ListView listView;
    private LoadImage loadImage;
    private LoadImage.ImageLoadListener listener = new LoadImage.ImageLoadListener() {
        @Override
        public void imageLoadOK(Bitmap bitmap, String url) {
            ImageView icon = null;
            if(url!=null)
                icon = (ImageView) listView.findViewWithTag(url);
            if(icon!=null)
                icon.setImageBitmap(bitmap);
        }
    };

    public NewsAdapter(Context context,ListView listView) {
        super(context);
        defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultpic);
        this.listView = listView;
        loadImage = new LoadImage(context,listener);

    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_list_news, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        News news = myList.get(position);
        holder.tv_title.setText(news.getTitle());
        holder.tv_text.setText(news.getSummary());
        holder.tv_from.setText(news.getStamp());
        holder.icon.setTag(news.getIcon());
        holder.icon.setImageBitmap(defaultBitmap);

        loadImage.getBitmap(news.getIcon(),holder.icon);

        return convertView;
    }

    class ViewHolder{
        public ImageView icon;
        public TextView tv_title;
        public TextView tv_text;
        public TextView tv_from;
        public ViewHolder(View view){
            icon = (ImageView) view.findViewById(R.id.imageView1);
            tv_title = (TextView) view.findViewById(R.id.textView1);
            tv_text = (TextView) view.findViewById(R.id.textView2);
            tv_from = (TextView) view.findViewById(R.id.textView3);
        }
    }
}
