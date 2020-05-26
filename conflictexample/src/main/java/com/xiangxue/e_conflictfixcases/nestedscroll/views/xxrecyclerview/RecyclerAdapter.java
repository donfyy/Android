package com.xiangxue.e_conflictfixcases.nestedscroll.views.xxrecyclerview;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.xiangxue.e_conflictfixcases.nestedscroll.base.recyclerview.BaseViewHolder;
import com.xiangxue.e_conflictfixcases.nestedscroll.views.titleview.TitleView;
import com.xiangxue.e_conflictfixcases.nestedscroll.views.titleview.TitleViewViewModel;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public List<String> data;

    public RecyclerAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(new TitleView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(new TitleViewViewModel(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

