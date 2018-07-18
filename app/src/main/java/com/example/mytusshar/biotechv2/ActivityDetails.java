package com.example.mytusshar.biotechv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class ActivityDetails extends Activity {

    FloatingActionButton fab;

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        setListeners();


    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    void setListeners(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getBaseContext(), ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}
