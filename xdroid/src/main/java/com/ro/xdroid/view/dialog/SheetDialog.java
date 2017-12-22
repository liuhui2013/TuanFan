package com.ro.xdroid.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ro.xdroid.R;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.view.widget.RecycleViewDivider;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roffee on 2017/10/24 14:12.
 * Contact with 460545614@qq.com
 */
public class SheetDialog<T> {
    protected Context context;
    protected BottomSheetDialog bottomSheetDialog;
    protected View dialogView;
    protected TextView tvTitle;
    protected RecyclerView recyclerView;
    protected int dividerHeight = 1;
    protected int dividerColor = Color.LTGRAY;
    protected int normalTextSize = 22;
    protected int normalTextColor = Color.BLACK;
    protected int selectedTextSize = 22;
    protected int selectedTextColor = Color.BLACK;
    protected int textPadding = 38;
    protected int selectedItemPos = -1;
    protected int itemLayoutId = R.layout.bsd_single_text_item_layout;
    protected boolean isSelectTextBorder = false;
    protected boolean isUserDefinedUi;
    protected boolean isDataOnlyString;
    protected boolean isUseDefaultUiSetting = true;
    protected OnItemTextTransform<T> onItemTextTransform;
    protected OnItemLayoutTransform<T> onItemLayoutTransform;
    protected OnItemClickListener<T> onItemClickListener;
    public List<T> data;

    public SheetDialog (Context context){
        if(context == null) return;
        if(ToolsKit.isLifecycle(context)) return;

        this.context = context;
        bottomSheetDialog = new BottomSheetDialog(context);
        dialogView = LayoutInflater.from(context)
                .inflate(R.layout.bsd_container_layout,null);
        tvTitle  = (TextView) dialogView.findViewById(R.id.bsd_title_tv);
        recyclerView  = (RecyclerView) dialogView.findViewById(R.id.bsd_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public SheetDialog<T> setDialogBgColor(@ColorInt int color){
        ((ViewGroup)recyclerView.getParent()).setBackgroundColor(color);
        return this;
    }
    public SheetDialog<T> setDialogBgColorRes(@ColorRes int colorId){
        ((ViewGroup)recyclerView.getParent()).setBackgroundResource(colorId);
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setTitle(String title){
        if(ToolsKit.isEmpty(title)) tvTitle.setVisibility(View.GONE);
        else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        return this;
    }
    public SheetDialog<T> setTitleSize(int px){
        tvTitle.setTextSize(px);
        return this;
    }
    public SheetDialog<T> setTitleColor(@ColorInt int color){
        tvTitle.setTextColor(color);
        return this;
    }
    public SheetDialog<T> setTitleColorRes(@ColorRes int colorId){
        tvTitle.setTextColor(context.getResources().getColor(colorId));
        return this;
    }
    public SheetDialog<T> setTitleBgColor(@ColorInt int color){
        tvTitle.setBackgroundColor(color);
        return this;
    }
    public SheetDialog<T> setTitlePadding(int px){
        tvTitle.setPadding(px, px, px, px);
        return this;
    }
    public SheetDialog<T> setTitleBgColorRes(@ColorRes int colorId){
        tvTitle.setBackgroundResource(colorId);
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setNormalTextSize(int px){
        this.normalTextSize = px;
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setNormalTextColor(@ColorInt int color){
        this.normalTextColor = color;
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setSelectTextColorRes(@ColorRes int colorId){
        this.normalTextColor = context.getResources().getColor(colorId);
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setSelectedTextSize(int px){
        this.selectedTextSize = px;
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setSelectedTextColor(@ColorInt int color){
        this.selectedTextColor = color;
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setSelectedTextColorRes(@ColorRes int colorId){
        this.selectedTextColor = context.getResources().getColor(colorId);
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> isDataOnlyString(boolean isDataOnlyString){
        this.isDataOnlyString = isDataOnlyString;
        return this;
    }
    public SheetDialog<T> setSelectedItemPos(int selectedItemPos){
        this.selectedItemPos = selectedItemPos;
        return this;
    }
    public SheetDialog<T> setTextPadding(int px){
        this.textPadding = px;
        isUseDefaultUiSetting = false;
        return this;
    }
    public SheetDialog<T> setDividerHeight(int dh){
        this.dividerHeight = dh;
        return this;
    }
    public SheetDialog<T> setDividerColor(@ColorInt int color){
        this.dividerColor = color;
        return this;
    }
    public SheetDialog<T> setDividerColorRes(@ColorRes int colorId){
        this.dividerColor = context.getResources().getColor(colorId);
        return this;
    }
    public SheetDialog<T> setItemLayoutRes(@ColorRes int itemLayoutId){
        this.itemLayoutId = itemLayoutId;
        isUserDefinedUi = true;
        return this;
    }
    public SheetDialog<T> setData(T[] t){
        if(t == null) return this;
        if(data == null) data = new ArrayList<T>();
        for(T t1 : t){
            data.add(t1);
        }
        return this;
    }
    public SheetDialog<T> setData(List<T> t){
        data = t;
        return this;
    }
    public SheetDialog<T> isSelectTextBorder(boolean isSelectTextBorder){
        this.isSelectTextBorder = isSelectTextBorder;
        return this;
    }
    public SheetDialog<T> setOnItemTextTransform(OnItemTextTransform<T> onItemTextTransform){
        this.onItemTextTransform = onItemTextTransform;
        return this;
    }
    public SheetDialog<T> setOnItemLayoutTransform(OnItemLayoutTransform<T> onItemLayoutTransform){
        this.onItemLayoutTransform = onItemLayoutTransform;
        return this;
    }
    public SheetDialog<T> setOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        this.onItemClickListener = onItemClickListener;
        return this;
    }
    public SheetDialog<T> build(){
        if(context == null || bottomSheetDialog == null) return this;

        recyclerView.addItemDecoration(new RecycleViewDivider(context, RecycleViewDivider.HORIZONTAL_LIST, dividerHeight, dividerColor));
        BaseQuickAdapter quickAdapter = new BaseQuickAdapter<T, BaseViewHolder>(itemLayoutId, data) {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
                AutoUtils.autoSize(baseViewHolder.itemView);
                return baseViewHolder;
            }
            @Override
            protected void convert(BaseViewHolder helper, T item) {
                int pos  = helper.getLayoutPosition();
                if(isUserDefinedUi){
                    if(onItemLayoutTransform != null) onItemLayoutTransform.onItemLayoutTransform(helper, item);
                }else{
                    TextView textView = helper.getView(R.id.bsd_item_tv);
                    if(isUseDefaultUiSetting){
                        textView.setPadding(textPadding, textPadding, textPadding, textPadding);
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        textView.setTextSize(normalTextSize);
                        textView.setTextColor(normalTextColor);
                        if(selectedItemPos == pos){
                            textView.setTextSize(selectedTextSize);
                            textView.setTextColor(selectedTextColor);
                            if(isSelectTextBorder) textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        }
                    }
                    if(isDataOnlyString){
                        helper.setText(R.id.bsd_item_tv, item.toString());
                    }else{
                        if(onItemTextTransform != null) textView.setText(onItemTextTransform.onItemTextTransform(item));
                    }
                }
            }
        };
        quickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                bottomSheetDialog.dismiss();
                if(onItemClickListener != null) onItemClickListener.onItemClick(data.get(i), i);
            }
        });
        recyclerView.setAdapter(quickAdapter);
        AutoUtils.auto(dialogView);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        return this;
    }
    public SheetDialog<T> show(){
        if(bottomSheetDialog == null) return this;
        if(!bottomSheetDialog.isShowing()) bottomSheetDialog.show();
        return this;
    }
    public void dismiss(){
        if(bottomSheetDialog == null) return;
        if(bottomSheetDialog.isShowing()) bottomSheetDialog.dismiss();
    }
    public void release(){
        dismiss();
        bottomSheetDialog = null;
        dialogView = null;
        recyclerView = null;
        onItemTextTransform = null;
        onItemLayoutTransform = null;
        onItemClickListener = null;
//        if(data != null){
//            data.clear();
//            data = null;
//        }
    }
    public interface OnItemTextTransform<D>{
        String onItemTextTransform(D item);
    }
    public interface OnItemLayoutTransform<D>{
        void onItemLayoutTransform(BaseViewHolder helper, D item);
    }
    public interface OnItemClickListener<D>{
        void onItemClick(D item, int pos);
    }
}
