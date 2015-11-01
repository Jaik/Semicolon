package com.semicolon.talkbackpluslibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mohit on 10/31/2015.
 */
public class TalkBackHandler {

    boolean canVibrate = true;
    long lastVibrationTime = System.currentTimeMillis();
    Context context;

    public void init(Activity activity) {
        this.context = activity;
        final View[] view = new View[1];
        final ViewGroup decorLayout = ((ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));


        final ViewCoordinate[] viewCoordinates = new ViewCoordinate[decorLayout.getChildCount()];


        decorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                for (int i = 0; i < decorLayout.getChildCount(); i++) {
                    view[0] = decorLayout.getChildAt(i);
                    viewCoordinates[i] = new ViewCoordinate(view[0].getX(), view[0].getX() + view[0].getWidth(), view[0].getY()
                            , view[0].getY() + view[0].getHeight());

                }

                if (event.getAction() ==
                        MotionEvent.ACTION_MOVE) {
                    for (ViewCoordinate cordinate : viewCoordinates) {

                        if (cordinate.X1 <= event.getX() && event.getX() <= cordinate.X2
                                && cordinate.Y1 <= event.getY() && event.getY() <= cordinate.Y2) {
                            canVibrate = true;
                            vibrate();
                            break;
                        }
                    }

                }
                return true;
            }
        });

    }

    private void vibrate() {

        if (canVibrate && lastVibrationTime + 300 < System.currentTimeMillis()) {
            canVibrate = false;
            lastVibrationTime = System.currentTimeMillis();
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 50 milliseconds
            v.vibrate(50);
        }
    }


    public class ViewCoordinate {

        public float X1;
        public float X2;
        public float Y1;
        public float Y2;

        public ViewCoordinate(float x1, float x2, float y1, float y2) {
            this.X1 = x1;
            this.X2 = x2;
            this.Y1 = y1;
            this.Y2 = y2;
        }
    }
}
