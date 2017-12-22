package com.ro.xdroid.media.image.viewer;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ro.xdroid.R;
import com.ro.xdroid.net.ok.OkKit;
import com.ro.xdroid.net.ok.taotong.TTDL;

/**
 * Created by roffee on 2017/9/13 16:42.
 * Contact with 460545614@qq.com
 */
public class ImageOverlayView extends RelativeLayout{
    private TextView dscriptionTv;
    private Button shareBtn;
    private String sharingText;

    public ImageOverlayView(Context context) {
        super(context);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDescription(String description) {
        dscriptionTv.setText(description + "");
    }
    @SuppressWarnings("deprecation")
    public void setDescriptionColor(@ColorRes int color) {
        dscriptionTv.setTextColor(getResources().getColor(color));
    }
    public void setDescriptionColorInt(@ColorInt int color) {
        dscriptionTv.setTextColor(color);
    }
    @SuppressWarnings("deprecation")
    public void setDescriptionSize(@DimenRes int size) {
        dscriptionTv.setTextSize(getResources().getDimension(size));
    }
    public void setDescriptionSizePx(int size) {
        dscriptionTv.setTextSize(size);
    }

    public void setShareText(String text) {
        this.sharingText = text + "";
    }
    public void setShareBtnText(String text) {
        shareBtn.setText(text+ "");
    }
    @SuppressWarnings("deprecation")
    public void setShareColor(@ColorRes int color) {
        shareBtn.setTextColor(getResources().getColor(color));
    }
    public void setShareColorInt(@ColorInt int color) {
        shareBtn.setTextColor(color);
    }
    @SuppressWarnings("deprecation")
    public void setShareSize(@DimenRes int size) {
        shareBtn.setTextSize(getResources().getDimension(size));
    }
    public void setShareSizePx(int size) {
        shareBtn.setTextSize(size);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_image_overlay, this);
        dscriptionTv = (TextView) view.findViewById(R.id.tvDescription);
        shareBtn = view.findViewById(R.id.btnShare);
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginType = dscriptionTv.getText().toString().trim();
                if ("0".equalsIgnoreCase(loginType)){//未登录
                    Toast.makeText(getContext(), "请登录后下载", Toast.LENGTH_SHORT).show();
                }else {
                    sendShareIntent();
                }
            }
        });
    }
    TTDL ttdl;
    private void sendShareIntent() {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
//        sendIntent.setType("text/plain");
//        getContext().startActivity(sendIntent);
        String name=System.currentTimeMillis()/1000+".jpg";

        ttdl = OkKit.TTDLRequest(sharingText, "http://doubihai.com/?m=home&c=index&a=gettoken")
                .isAutoCancel(false)
                .isBindToLifecycle(false)
                .isBreakpoint(true, name)
                .isCover(true)
                .execute(null);


    }

}
