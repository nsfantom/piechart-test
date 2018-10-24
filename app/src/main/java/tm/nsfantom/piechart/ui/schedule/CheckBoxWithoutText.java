package tm.nsfantom.piechart.ui.schedule;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

public class CheckBoxWithoutText extends AppCompatCheckBox {
    private Drawable buttonDrawable;

    public CheckBoxWithoutText(Context context) {
        super(context);
    }

    public CheckBoxWithoutText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getCompoundPaddingLeft() + getCompoundPaddingRight();
        } else {
            return buttonDrawable == null ? 0 : buttonDrawable.getIntrinsicWidth();
        }
    }

    @Override
    public void setButtonDrawable(Drawable d) {
        buttonDrawable = d;
        super.setButtonDrawable(d);
    }
}