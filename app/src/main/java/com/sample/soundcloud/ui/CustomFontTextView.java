package com.sample.soundcloud.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sample.soundcloud.R;
import com.sample.soundcloud.utilities.TypefaceUtil;
import com.sample.soundcloud.utilities.Typefaces;

public class CustomFontTextView extends TextView {

    //region Constructors
    public CustomFontTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    //endregion

    // region Helper Methods
    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, 0, 0);
            try {
                Integer position = a.getInteger(R.styleable.CustomFontTextView_textFont, 10);
                setTypeface(TypefaceUtil.getTypeface(Typefaces.from(position)));
            } finally {
                a.recycle();
            }
        } else {
            setTypeface(TypefaceUtil.getTypeface(Typefaces.ROBOTO_REGULAR));
        }
    }
    // endregion
}
