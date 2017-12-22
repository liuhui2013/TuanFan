package com.ro.xdroid.mvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ro.xdroid.kit.KnifeKit;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by roffee on 2017/7/25 10:20.
 * Contact with 460545614@qq.com
 */
public abstract class XRecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener onItemClickListener;
    private boolean useAutolayout = true, bindView = true;

    public XRecViewHolder(View itemView) {
        super(itemView);
        if(useAutolayout){AutoUtils.autoSize(itemView);}
        if(bindView){KnifeKit.bind(this, itemView);}
        itemView.setOnClickListener(this);
    }
    public XRecViewHolder(View itemView, boolean useAutolayout, boolean bindView) {
        super(itemView);
        this.useAutolayout = useAutolayout;
        this.bindView = bindView;
        if(useAutolayout){AutoUtils.autoSize(itemView);}
        if(bindView){KnifeKit.bind(this, itemView);}
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, this.getPosition());
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){this.onItemClickListener = onItemClickListener;}
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
