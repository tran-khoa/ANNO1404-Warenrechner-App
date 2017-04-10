package de.ktran.anno1404warenrechner.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

public abstract class AnimationFinishListener implements AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public abstract void onAnimationEnd(Animator animation);

    @Override
    public void onAnimationCancel(Animator animation) {}

    @Override
    public void onAnimationRepeat(Animator animation) {}
}
