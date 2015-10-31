package com.semicolon.findhouse;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mohit on 10/31/2015.
 */
public class TalkBackHandler {

    public  static void init(Activity activity){
       ViewGroup decorLayout = ((ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
        for(int i =0;i<decorLayout.getChildCount();i++){
            View v = decorLayout.getChildAt(i);
            if(v instanceof TBEditText){
               String s = ((TBEditText) v).talkingText;
            }
        }

    }
}
