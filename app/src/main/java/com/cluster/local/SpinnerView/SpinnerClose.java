package com.cluster.local.SpinnerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.cluster.local.R;


public class SpinnerClose extends View {

    public SpinnerClose(Context context) {
        super(context);

    }

    public SpinnerClose(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerClose(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpinnerClose(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void show() {
        setVisibility(VISIBLE);
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinner_close_in));
    }

    public void hide() {
        setVisibility(INVISIBLE);
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinner_close_out));
    }


}
