package com.godex.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Loading extends Activity {
    private Activity activity;
    Loading.OnDialogRespond onDialogRespond;
    TextView tbLoadingText, tbLoadingText2;
    ProgressBar progressBar;
    public Loading() {
    }
    public Loading(Activity activity) {
        this.activity = activity;
    }
    private AlertDialog dialog;
    AlertDialog.Builder mBuilder;
    Button btn_Cancel;
    Handler handler;

    public void showDialog() {
        Timer t = new Timer();
        //setTimerTask(t);
        mBuilder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_loading, null);
        mBuilder.setView(view);
        mBuilder.setCancelable(true);
        tbLoadingText = view.findViewById(R.id.tb_Loading_Text);
        tbLoadingText2 = view.findViewById(R.id.tb_Loading_Text2);
        progressBar = view.findViewById(R.id.progressBar);
        btn_Cancel = view.findViewById(R.id.btn_Loading_Cancel);
        btn_Cancel.setVisibility(View.INVISIBLE);
        btn_Cancel.setOnClickListener(v -> dialog.dismiss());
        dialog = mBuilder.create();
        dialog.show();
        handler = new Handler(msg -> {
            btn_Cancel.setVisibility(View.VISIBLE);
            return false;
        });
    }

    private void setTimerTask(Timer mTimer) {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                handler.sendMessage(msg);
            }
        }, 3000);
    }

    public void setLoadingText(String text){
        tbLoadingText.setText(text);
    }

    public void setLoadingText2(String text){
        tbLoadingText2.setText(text);
    }

    public void setProgressBar(boolean visible){
        progressBar.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    public void setCancelBtn(boolean visible){
        btn_Cancel.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    public void closeDialog() {
        dialog.cancel();
    }

    interface OnDialogRespond {
        void onRespond(String selected);
    }
}