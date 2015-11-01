package com.semicolon.appusingtalkbackplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.semicolon.talkbackpluslibrary.TalkBackHandler;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TalkBackHandler tbh = new TalkBackHandler();
        tbh.init(this);
    }
}
