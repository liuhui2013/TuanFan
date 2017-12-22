package com.ro.xdroid.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.ro.xdroid.R;
import com.ro.xdroid.kit.ToolsKit;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by roffee on 2017/7/25 09:08.
 * Contact with 460545614@qq.com
 */
public class LoadingDialog extends Dialog {
    private Context context;
    private CardView cardView;
    private AVLoadingIndicatorView avLoadingIndicatorView;

//    private boolean isCanceledOnKeyBack;


    public LoadingDialog(Context context) {
        this(context, R.style.Dialog);
    }
    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.layout_loading_dialog);
        this.context = context;
        cardView = findViewById(R.id.loadingFramCardView);
        avLoadingIndicatorView = findViewById(R.id.loadingIndicatorView);
    }
    public LoadingDialog isFrame(boolean isFrame){
        if(!isFrame){
            cardView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        return this;
    }
    public LoadingDialog isCanceledOnTouchOutside(boolean isCanceledOnTouchOutside){
        this.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        return this;
    }
//    public LoadingDialog isCanceledOnKeyBack(boolean isCanceledOnKeyBack){
//        this.isCanceledOnKeyBack = isCanceledOnKeyBack;
//        return this;
//    }
    public LoadingDialog isCancelable(boolean isCancelable){
        this.setCancelable(isCancelable);
        return this;
    }
    public LoadingDialog setFrameColor(String color){
        if(ToolsKit.isEmpty(color)) return this;
        cardView.setCardBackgroundColor(Color.parseColor(color));
        return this;
    }
    public LoadingDialog setFrameColor(int resColor){
        cardView.setCardBackgroundColor(context.getResources().getColor(resColor));
        return this;
    }
    @Override
    public void show() {
        super.show();
    }
    @Override
    public void dismiss() {
        if(!isShowing()){return;}
        super.dismiss();
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if(isCanceledOnKeyBack) dismiss();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
