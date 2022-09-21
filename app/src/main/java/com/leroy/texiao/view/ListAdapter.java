package com.leroy.texiao.view;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leroy.texiao.utils.FilterBean;
import com.leroy.texiao.R;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private FilterBean[] list;
    private OnItemClick itemClick;

    public ListAdapter(FilterBean[] list){
        this.list = list;
    }

    public void setItemClick(OnItemClick itemClick){
        this.itemClick = itemClick;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        String name = list[position].getName();
        String color = list[position].getColor();
        if (!TextUtils.isEmpty(color)) {
            holder.tvText.setTextColor(Color.parseColor(color));
        } else {
            holder.tvText.setTextColor(Color.parseColor("#ffffff"));
        }
        holder.tvText.setText(name);
        holder.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null) {
                    itemClick.itemClick(name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvText;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }

    public interface OnItemClick{
        void itemClick(String filterName);
    }


}
