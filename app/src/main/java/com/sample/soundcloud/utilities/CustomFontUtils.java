package com.sample.soundcloud.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sample.soundcloud.R;

/**
 * Created by etiennelawlor on 12/14/15.
 */
public class CustomFontUtils {

    // region Constants
    private static final int MAVEN_PRO_BLACK = 0;
    private static final int MAVEN_PRO_BOLD = 1;
    private static final int MAVEN_PRO_MEDIUM = 2;
    private static final int MAVEN_PRO_REGULAR = 3;
    // endregion

    public static void applyCustomFont(TextView customFontTextView, Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomFontTextView);

        try {
            int font = attributeArray.getInteger(R.styleable.CustomFontTextView_textFont, 2);
            Typeface customFont = getTypeface(context, font);
            customFontTextView.setTypeface(customFont);
        } finally {
            attributeArray.recycle();
        }
    }

    private static Typeface getTypeface(Context context, int font) {
        switch (font) {
            case MAVEN_PRO_BLACK:
                return FontCache.getTypeface("MavenPro-Black.ttf", context);
            case MAVEN_PRO_BOLD:
                return FontCache.getTypeface("MavenPro-Bold.ttf", context);
            case MAVEN_PRO_MEDIUM:
                return FontCache.getTypeface("MavenPro-Medium.ttf", context);
            case MAVEN_PRO_REGULAR:
                return FontCache.getTypeface("MavenPro-Regular.ttf", context);
            default:
                // no matching font found
                // return null so Android just uses the standard font (Roboto)
                return null;
        }
    }
}
