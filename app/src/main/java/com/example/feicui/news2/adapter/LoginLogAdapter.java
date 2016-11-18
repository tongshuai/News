package com.example.feicui.news2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feicui.news2.R;
import com.example.feicui.news2.base.MyBaseAdapter;
import com.example.feicui.news2.entity.LoginLog;


/**
 * Created by Justin on 2016/6/14.
 */
public class LoginLogAdapter extends MyBaseAdapter<LoginLog> {

    public LoginLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_login_log, null);
            holder = new ViewHolder();
            holder.login_address = (TextView) convertView.findViewById(R.id.login_address);
            holder.login_time = (TextView) convertView.findViewById(R.id.login_time);
            holder.login_type = (TextView) convertView.findViewById(R.id.login_type);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        LoginLog item = getItem(position);
        holder.login_address.setText(item.getAddress());
        holder.login_time.setText(item.getTime());
        holder.login_type.setText(item.getDevice()==0?"手机":"PC");
        return convertView;
    }
    class ViewHolder{
        TextView login_address;
        TextView login_time;
        TextView login_type;
    }
}
