package com.ro.xdroid.date.picker;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;

/**
 * Created by roffee on 2017/10/25 11:11.
 * Contact with 460545614@qq.com
 */
public class OptionsPicker {
    public static OptionsPickerView.Builder getBuilder(Context context, OnOptionsPickerListener onOptionsPickerListener){
        return new OptionsPicker()
                .createBuilder(context, onOptionsPickerListener);
    }
    public OptionsPickerView.Builder createBuilder(Context context, OnOptionsPickerListener onOptionsPickerListener){
        return new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(onOptionsPickerListener != null) onOptionsPickerListener.onOptionsPicker(options1, options2, options3, v);
            }
        })
//                .setLayoutRes(R.layout.options_picker_layout, new CustomListener() {
//                    @Override
//                    public void customLayout(View v) {
//
//                    }
//                })
                .isCenterLabel(false)
                .setBgColor(Color.WHITE)
                .setDividerColor(Color.LTGRAY)
                .setContentTextSize(16)
                .setLineSpacingMultiplier(1.8f)
                .setTitleBgColor(0xffffbb33)
                .setTitleSize(17)
                .setTitleColor(Color.WHITE)
                .setSubCalSize(16)
                .setCancelColor(Color.WHITE)
                .setCancelText("取消")
                .setSubmitColor(Color.WHITE)
                .setSubmitText("选择");
    }
    public interface OnOptionsPickerListener{
        void onOptionsPicker(int option1Pos, int option2Pos, int option3Pos, View v);
    }
}
