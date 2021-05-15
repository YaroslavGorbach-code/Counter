package com.yaroslavgorbachh.counter.feature;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.yaroslavgorbachh.counter.feature.Accessibility;

public class FastCountButton implements View.OnTouchListener, Handler.Callback {

    public static final int MAX = 300;
    public static final int MIN = 30;
    private static final int FAST_COUNT_MSG = 0;

    private final int mFastCountInterval;
    private final Handler mHandler = new Handler(this);
    private final View mView;
    public boolean mFastCounting;
    private final Accessibility mAccessibility; // TODO: 5/15/2021 handle accessibility


    public FastCountButton(View view, Runnable action, SharedPreferences mSharedPreferences, Accessibility accessibility) {
        mView = view;
        mAccessibility = accessibility;
        action.run();
        view.setOnClickListener(v -> action.run());
        view.setOnTouchListener(this);
        mFastCountInterval = MAX - mSharedPreferences.getInt("fastCountSpeed", 200) + MIN;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mView.setPressed(true);
                mHandler.sendEmptyMessageDelayed(FAST_COUNT_MSG, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_UP:
                if (!mFastCounting) {
                    mView.performClick();
                }
                // no break
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeMessages(FAST_COUNT_MSG);
                mFastCounting = false;
                mView.setPressed(false);
                mView.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
            if (msg.what == FAST_COUNT_MSG) {
                if (!mFastCounting) {
                    mFastCounting = true;
                    mView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    mView.getParent().requestDisallowInterceptTouchEvent(true);
                }
                mView.performClick();
                mHandler.sendEmptyMessageDelayed(FAST_COUNT_MSG, mFastCountInterval);
            }

        return false;
    }

}