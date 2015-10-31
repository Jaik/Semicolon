package com.semicolon.findhouse;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        talkingText = context.obtainStyledAttributes(attrs, R.styleable.TBButton, 0, 0).getString(0);

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
        final float viewX1 = this.getX();
        final float viewX2 = viewX1 + this.getWidth();
        final float viewY1 = this.getY();
        final float viewY2 = viewY1 + this.getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canVibrate = true;
                vibrate();
                break;
            case MotionEvent.ACTION_MOVE:
                canVibrate = false;
                break;
            case MotionEvent.ACTION_UP:
                handleDoubleTap();
//                Toast.makeText(getContext(), "Raja", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onTouchEvent(event);
    }


    private void handleDoubleTap() {
        if (isTouchedTwice) {
            listener.onClick(this);
            Toast.makeText(getContext(), talkingText, Toast.LENGTH_SHORT).show();
        } else {
            isTouchedTwice = true;

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isTouchedTwice = false;
                }
            }, 500);
        }
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
            watchKey();
            setTouchListenerOnParent();
            isInitialized = true;
        }

        super.onDraw(canvas);
    }

    private void setTouchListenerOnParent() {
        final float viewX1 = this.getX();
        final float viewX2 = viewX1 + this.getWidth();
        final float viewY1 = this.getY();
        final float viewY2 = viewY1 + this.getHeight();

        ((View) this.getParent()).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_MOVE) {
                    if (viewX1 <= event.getX() && event.getX() <= viewX2
                            && viewY1 <= event.getY() && event.getY() <= viewY2) {
                        canVibrate = true;
                        vibrate();
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private void watchKey() {
        this.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 1;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                return true;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return true;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });

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
