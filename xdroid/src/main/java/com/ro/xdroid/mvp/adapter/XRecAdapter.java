package com.ro.xdroid.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roffee on 2017/7/25 10:09.
 * Contact with 460545614@qq.com
 */
public abstract class XRecAdapter<T> extends RecyclerView.Adapter<XRecViewHolder> {
    private Context context;
    protected List<T> data;
    protected XRecViewHolder xRecViewHolder;

    public XRecAdapter(Context context) {
        super();
        this.context = context;
    }
    public XRecAdapter(Context context, List<T> data) {
        super();
        this.context = context;
        this.data = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public XRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        xRecViewHolder = getViewHolder(view);
        return xRecViewHolder;
    }

    public abstract XRecViewHolder getViewHolder(View itemView);
    public abstract int getLayoutId();

    public void setNewData(List<T> data){
        this.data = data == null ? new ArrayList<T>() : data;
        notifyDataSetChanged();
    }
    public void addData(T item){
        if(this.data == null || item == null){return;}
        this.data.add(item);
        notifyDataSetChanged();
    }
    public void addData(List<T> data){
        if(this.data == null || data == null){return;}
        this.data.addAll(data);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(XRecViewHolder.OnItemClickListener onItemClickListener){
        if(xRecViewHolder == null){return;}
        xRecViewHolder.setOnItemClickListener(onItemClickListener);
    }
}
