package com.daomaidaomai.islandtrading.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daomaidaomai.islandtrading.R;


public class Set extends Activity {
    private RelativeLayout Personal;
    private RelativeLayout Aboutus;
    private LinearLayout Back;

    public View.OnClickListener mylistener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.personal: {
                    Intent i = new Intent(Set.this, Personal.class);
                    startActivity(i);
                    break;
                }
                case R.id.aboutus: {
                    Intent i = new Intent(Set.this, Aboutus.class);
                    startActivity(i);
                    break;
                }
                case R.id.back:
                    Set.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.set);
        Personal = (RelativeLayout) findViewById(R.id.personal);
        Aboutus = (RelativeLayout) findViewById(R.id.aboutus);
        Back = (LinearLayout) findViewById(R.id.back);

        Personal.setOnClickListener(mylistener);
        Aboutus.setOnClickListener(mylistener);
        Back.setOnClickListener(mylistener);


    }
}
