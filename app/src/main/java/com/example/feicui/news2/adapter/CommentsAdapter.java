package com.example.feicui.news2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.feicui.news2.R;
import com.example.feicui.news2.base.MyBaseAdapter;
import com.example.feicui.news2.entity.Comment;


/**
 * Created by Justin on 2016/6/12.
 */
public class CommentsAdapter extends MyBaseAdapter<Comment> {


    private Bitmap bitmapDefault;
    private ListView listView;

    public CommentsAdapter(Context context, ListView listView) {
        super(context);
        this.listView = listView;
        bitmapDefault = BitmapFactory.decodeResource(context.getResources(), R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_list_comment,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Comment item = getItem(position);
        holder.tv_name.setText(item.getUid());
        holder.iv_icon.setImageBitmap(bitmapDefault);

        holder.tv_time.setText(item.getStamp());
        holder.tv_content.setText(item.getContent());

        return convertView;
    }

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;

        public ViewHolder(View view){
            iv_icon = (ImageView) view.findViewById(R.id.imageView1);
            tv_name = (TextView) view.findViewById(R.id.textView2);
            tv_time = (TextView) view.findViewById(R.id.textView3);
            tv_content = (TextView) view.findViewById(R.id.textView1);
        }

    }
}
