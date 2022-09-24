package com.app.roktoDorkar.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.app.roktoDorkar.R;

public class ProgressDialogRecover {
    private Activity activity;
    private AlertDialog dialog;

    ProgressDialogRecover(Activity myActivity){
        activity=myActivity;

    }
    void startLoadingDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialogue_recover,null));
        builder.setCancelable(true);
        dialog=builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}