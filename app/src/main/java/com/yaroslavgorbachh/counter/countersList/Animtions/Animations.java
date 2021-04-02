package com.yaroslavgorbachh.counter.countersList.Animtions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.google.android.material.button.MaterialButton;

public class Animations {

    public static void showButtons(MaterialButton mDecAllSelectedCounters_bt, MaterialButton mIncAllSelectedCounters_bt) {

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

    public static void hideButtons(MaterialButton mDecAllSelectedCounters_bt, MaterialButton mIncAllSelectedCounters_bt) {
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
}
