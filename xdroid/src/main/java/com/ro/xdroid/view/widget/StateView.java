package com.ro.xdroid.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.ro.xdroid.R2;
import com.ro.xdroid.kit.KnifeKit;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;

/**
 * Created by roffee on 2017/7/24 18:13.
 * Contact with 460545614@qq.com
 */
public class StateView extends AutoLinearLayout {
    @BindView(R2.id.baseStateIcon)
    ImageView baseStateIcon;
    @BindView(R2.id.baseStateTv)
    TextView baseStateTv;

    public StateView(Context context) {
        super(context);
        setView(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setView(context);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setView(context);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setView(context);
    }

    public void setStateIcon(int iconId) {
        if (iconId > 0) {baseStateIcon.setImageResource(iconId);}
    }
    public void setStateMsg(String msg){baseStateTv.setText("" + msg);}

    private void setView(Context context) {
        inflate(context, R2.layout.view_base_state, this);
        KnifeKit.bind(this);
    }
}
