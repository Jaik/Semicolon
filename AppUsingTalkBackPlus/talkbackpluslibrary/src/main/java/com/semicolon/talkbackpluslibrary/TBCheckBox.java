package com.semicolon.talkbackpluslibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Locale;

/**
 * Created by mohit on 10/31/2015.
 */
public class TBCheckBox extends CheckBox {

    Boolean canVibrate = true;
    TextToSpeech t1;
    public String talkingText;
    boolean isTouchedTwice = false;
    boolean setRadioButton = false;
    OnClickListener listener;
    long lastVibrationTime = System.currentTimeMillis();
    boolean isInitialized = false;
    boolean currentState = false;

    public TBCheckBox(Context context) {
        super(context);
    }

    public TBCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        talkingText = context.obtainStyledAttributes(attrs, R.styleable.TBEditText, 0, 0).getString(0);

        t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });


    }

    public TBCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(TalkBackHandler.TalkBackMode) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    canVibrate = true;
                    vibrate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    canVibrate = false;
                    break;
                case MotionEvent.ACTION_UP:

                    break;
            }
        }
        return super.onTouchEvent(event);
    }




    private void vibrate() {
        if (canVibrate && lastVibrationTime + 300 < System.currentTimeMillis()) {
            canVibrate = false;
            lastVibrationTime = System.currentTimeMillis();
            talkBack();
            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(50);
        }
    }

    private void talkBack() {
        if (!t1.isSpeaking()) {
            t1.speak(talkingText, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.listener = l;
        //super.setOnClickListener(l);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInitialized) {
            //setTouchListenerOnParent();
            isInitialized = true;
        }

        setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (TalkBackHandler.TalkBackMode) {
                    if (isTouchedTwice) {
                        setChecked(isChecked);
                    } else {
                        setChecked(!isChecked);
                        isTouchedTwice = true;
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isTouchedTwice = false;
                            }
                        }, 500);
                    }
                }
            }
        });

        super.onDraw(canvas);
    }




}
