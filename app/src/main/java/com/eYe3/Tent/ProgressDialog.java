package com.eYe3.Tent;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class ProgressDialog {
    private Activity activity;
    private AlertDialog dialog;
    ProgressDialog(Activity myActivity){
        activity=myActivity;
    }

    void startProgressDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);

        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog_bar,null));
        builder.setCancelable(false);

        dialog=builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
