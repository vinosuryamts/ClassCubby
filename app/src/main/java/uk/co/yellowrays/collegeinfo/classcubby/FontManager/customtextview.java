package uk.co.yellowrays.collegeinfo.classcubby.FontManager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Vino on 9/21/2017.
 */

public class customtextview extends android.support.v7.widget.AppCompatTextView  {

    public customtextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public customtextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public customtextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome.ttf");
        setTypeface(tf ,1);

    }
}
