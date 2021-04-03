package com.yaroslavgorbachh.counter.Animtions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Animations {

    public static void showButtonsMultiSelection(MaterialButton mDecAllSelectedCounters_bt, MaterialButton mIncAllSelectedCounters_bt) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDecAllSelectedCounters_bt, View.TRANSLATION_Y, 100, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mIncAllSelectedCounters_bt, View.TRANSLATION_Y, 100, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animator2);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mDecAllSelectedCounters_bt.setVisibility(View.VISIBLE);
                mIncAllSelectedCounters_bt.setVisibility(View.VISIBLE);
            }
        });
        animatorSet.start();

    }

    public static void hideButtonsMultiSelection(MaterialButton mDecAllSelectedCounters_bt, MaterialButton mIncAllSelectedCounters_bt) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDecAllSelectedCounters_bt, View.TRANSLATION_Y, 0, 500);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mIncAllSelectedCounters_bt, View.TRANSLATION_Y, 0, 500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animator2);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDecAllSelectedCounters_bt.setVisibility(View.GONE);
                mIncAllSelectedCounters_bt.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

    public static void hideSwipeHelper(View linearLayout) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.addUpdateListener(animation -> linearLayout.setAlpha((Float) animation.getAnimatedValue()));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                linearLayout.setVisibility(View.GONE);
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    public static void hideSwipeHelperWithDelay(View linearLayout){
        new Handler(Looper.getMainLooper()).postDelayed(() -> hideSwipeHelper(linearLayout), 3000);
    }
}
