package com.semicolon.talkbackpluslibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by mohit on 10/31/2015.
 */
public class TBEditText extends EditText {

    Boolean canVibrate = true;
    TextToSpeech t1;
    String talkingText;
    boolean isTouchedTwice = false;
    OnClickListener listener;
    long lastVibrationTime = System.currentTimeMillis();

    boolean isInitialized = false;

    public TBEditText(Context context) {
        super(context);
    }

    public TBEditText(Context context, AttributeSet attrs) {
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

    public TBEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
                    //handleDoubleTap();
//                Toast.makeText(getContext(), "Raja", Toast.LENGTH_SHORT).show();
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
            //watchKey();
            //setTouchListenerOnParent();
            isInitialized = true;

            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TalkBackHandler.TalkBackMode) {
                        if (!t1.isSpeaking() && !(s + "").isEmpty()) {
                            t1.speak(s.charAt(s.length() - 1) + "", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        super.onDraw(canvas);
    }





    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        String s = String.valueOf(event.getKeyCharacterMap().getDisplayLabel(event.getKeyCode()));

        if (!t1.isSpeaking()) {
            t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }

        return super.onKeyPreIme(keyCode, event);
    }
}
