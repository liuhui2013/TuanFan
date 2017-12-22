package com.ro.xdroid.date.picker;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by roffee on 2017/10/23 17:09.
 * Contact with 460545614@qq.com
 */
public class TimePicker {

    public static TimePickerView.Builder getBuilder(Context context, OnTimerPickerListener onTimerPickerListener){
        return new TimePicker().createBuilder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(onTimerPickerListener != null) onTimerPickerListener.onTimePicker(date, v);
            }
        });
    }
    public TimePickerView.Builder createBuilder(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener){
        Calendar startDate = Calendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH),startDate.get(Calendar.HOUR_OF_DAY),startDate.get(Calendar.MINUTE));
        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 1, 1);
        //0xFF24AD9D
        return  new TimePickerView.Builder(context, onTimeSelectListener)
                .setRangDate(startDate, endDate)
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false)
                .setBgColor(Color.WHITE)
                .setDividerColor(Color.LTGRAY)
                .setContentSize(16)
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
    public interface OnTimerPickerListener{
        void onTimePicker(Date date, View v);
    }
}
