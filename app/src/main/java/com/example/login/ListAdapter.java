package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Snippet> {
    public ListAdapter(Context context, List<Snippet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if(null == convertView){
            convertView = inflater.inflate(
                    R.layout.list_item,
                    parent,
                    false);

            holder = new ViewHolder();

            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Snippet snippet = getItem(position);
        System.out.println(snippet.getId());
        holder.id.setText("ID: "+snippet.getId());
        holder.title.setText(snippet.getTitle());

        return convertView;
    }
    static class ViewHolder {
        TextView id;
        TextView title;
    }
}
