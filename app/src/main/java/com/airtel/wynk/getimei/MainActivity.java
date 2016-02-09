package com.airtel.wynk.getimei;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView lolipopText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lolipopText = (TextView) findViewById(R.id.imeiLolipop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] imeis = ImeiUtil.getImei(getApplicationContext());
        lolipopText.setText("imei 1:" + imeis[0] +
                " \nimei 2:" + imeis[1]);
    }
}
