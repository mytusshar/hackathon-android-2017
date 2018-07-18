package com.example.mytusshar.biotechv2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mytusshar on 09-Mar-17.
 */
public class ActivitySplash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Log.d("Exception", "Exception" + e);
                } finally {
                    //startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
                }

                finish();
            }
        };
        logoTimer.start();
    }
}
