package com.oboard.zero;

import android.view.animation.*;

public class ValueAnimation extends Animation {

    private OnAnimatorUpdateListener mAnimatorUpdateListener;
    private float[] mValuesF;
    private int[] mValuesI;
    private float mValue;
    private int mType;

    public ValueAnimation() {
    }

    public static ValueAnimation ofInt(int...values) {
        return new ValueAnimation().setIntValues(values);
    }

    public static ValueAnimation ofFloat(float...values) {
        return new ValueAnimation().setFloatValues(values);
    }

    public ValueAnimation setIntValues(int...values) {
        mType = 0;
        mValuesI = values.clone();
        return this;
    }

    public ValueAnimation setFloatValues(float...values) {
        mType = 1;
        mValuesF = values.clone();
        return this;
    }

    public float getAnimatedValue() {
        return mValue;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        switch (mType) {
            case 0:
                mValue = (mValuesI[1] - mValuesI[0]) * interpolatedTime + mValuesI[0];
                break;
            case 1:
                mValue = (mValuesF[1] - mValuesF[0]) * interpolatedTime + mValuesF[0];
                break;
        }
        mAnimatorUpdateListener.onAnimationUpdate(this);
    }


    //Callback
    public ValueAnimation addUpdateListener(final OnAnimatorUpdateListener onAnimatorUpdateListener) {
        mAnimatorUpdateListener = onAnimatorUpdateListener;
        return this;
    }
    public interface OnAnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimation animation);
    }

}

