package com.auro.application.core.util.uiwidget;

import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.AttributeSet;

public class DetailsTransition extends TransitionSet {
    public DetailsTransition() {
        init();
    }


    public DetailsTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform())
                .addTransition(new ChangeBounds());

    }
}
