package com.example.blockcertify_manufacturerapp.Extra;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.example.blockcertify_manufacturerapp.R;
import com.example.blockcertify_manufacturerapp.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by dev on 2019/8/1.
 */

public class LoadingDialog extends Dialog{

    AVLoadingIndicatorView loadingview;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        loadingview = findViewById(R.id.dialog_loading);
    }

//    @Override
//    public void show() {
//        super.show();
//        loadingview.show();
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//        loadingview.hide();
//    }
}
