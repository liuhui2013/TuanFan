package com.ro.xdroid.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ro.xdroid.kit.ToolsKit;

import java.util.List;

/**
 * Created by roffee on 2017/7/21 10:38.
 * Contact with 460545614@qq.com
 */
public class DialogKit {
    private static LoadingDialog loadingDialog;

    public static LoadingDialog showLoading(Context context) {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog = null;
                    }
                });
        loadingDialog.show();
        return loadingDialog;
    }

    public static void dismissLoading() {
        if (loadingDialog == null) return;
        loadingDialog.dismiss();
        loadingDialog = null;
    }

    public static MaterialDialog.Builder makeMDialog(Context context) {
        return new MaterialDialog.Builder(context);
    }

    /**
     * 一般对话框，此处是例子，可根据需要自行配置
     *
     * @param context
     * @param content
     * @param singleButtonCallback
     * @singleButtonCallback
     */
    public static MaterialDialog showMDialog(Context context, String content, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        if (context == null || singleButtonCallback == null) {
            return null;
        }
        return makeMDialog(context)
                .content("" + content)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(singleButtonCallback)
                .onNegative(singleButtonCallback)
                .show();
    }

    public static MaterialDialog showMDialog(Context context, String content, String positie, String negative, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        if (context == null || singleButtonCallback == null) {
            return null;
        }
        if(ToolsKit.isLifecycle(content)) return null;
        try {
            return new MaterialDialog.Builder(context)
                    .content("" + content)
                    .positiveText("" + positie)
                    .negativeText("" + negative)
                    .onPositive(singleButtonCallback)
                    .onNegative(singleButtonCallback)
                    .autoDismiss(true)
                    .cancelable(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入框，此处是例子，可根据需要自行配置
     *
     * @param context
     * @param hint
     * @param inputCallback
     * @return
     */
    public static MaterialDialog showMDialog(Context context, String hint, MaterialDialog.InputCallback inputCallback) {
        if (context == null || inputCallback == null) {
            return null;
        }
        return makeMDialog(context)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("确定")
                .input("" + hint, "", false, inputCallback)
                .show();
    }

    /**
     * 输入框，此处是例子，可根据需要自行配置
     *
     * @param context
     * @param content
     * @param hint
     * @param inputType
     * @param inputMinRange
     * @param inpitMaxRange
     * @param positiveText
     * @param positveCallback
     * @param inputCallback
     * @return
     */
    public static MaterialDialog showMDialog(Context context, String content, String hint, int inputType, int inputMinRange, int inpitMaxRange,
                                             String positiveText, MaterialDialog.SingleButtonCallback positveCallback, MaterialDialog.InputCallback inputCallback) {
        if (context == null || positveCallback == null || inputCallback == null) {
            return null;
        }
        return makeMDialog(context)
                .content("" + content)
                .inputType(inputType)
                .inputRange(inputMinRange, inpitMaxRange)
                .positiveText("" + positiveText)
                .onPositive(positveCallback)
                .input("" + hint, "", false, inputCallback)
                .show();
    }
    public static SheetDialog<String> showSheetDialog(Context context, String[] t, SheetDialog.OnItemClickListener<String> onItemClickListener){
        return new SheetDialog<String>(context)
                .setData(t)
                .isDataOnlyString(true)
                .setOnItemClickListener(onItemClickListener)
                .build()
                .show();
    }
    public static SheetDialog<String> showSheetDialog(Context context, List<String> t, SheetDialog.OnItemClickListener<String> onItemClickListener){
        return new SheetDialog<String>(context)
                .setData(t)
                .isDataOnlyString(true)
                .setOnItemClickListener(onItemClickListener)
                .build()
                .show();
    }
}
