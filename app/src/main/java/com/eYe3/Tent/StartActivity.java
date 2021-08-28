package com.eYe3.Tent;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.material.snackbar.Snackbar;

public class StartActivity extends AppCompatActivity {
    private long ms=0, splashTime=900;
    private boolean splashAction=true, paused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final RelativeLayout sslayout=findViewById(R.id.sslayout);

        Thread thread=new Thread(){
            public void run(){
                try{
                    while (splashAction && ms<splashTime){
                        if (!paused)
                            ms=ms+100;
                        sleep(100);

                    }
                } catch (Exception e) {

                }finally {
                    if (!isOnline()){
                        Snackbar sn=Snackbar
                                .make(sslayout,"No Internet Connection",Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recreate();
                                    }
                                });
                        sn.show();
                    }else{
                        goMain();
                    }
                }
            }
        };
        thread.start();
    }

    private boolean isOnline() {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void goMain() {
        // todo: Setup login activity intent
    }
}
