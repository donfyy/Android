package com.donfyy.willremove.testlist.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donfyy.conflict.R;


/**
 * Created by song on 2018/8/22 0022
 * My email : logisong@163.com
 * The role of this :
 */
public class PagerListAdapter extends RecyclerView.Adapter{
private String title;
    public PagerListAdapter(String title) {
        this.title=title;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.rv_item_normal,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tv = holder.itemView.findViewById(R.id.tv);
        tv.setText(position+title);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
