package tm.nsfantom.piechart.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public final class DisplayUtil {

    public static int dpToPx(Resources resources, int dp) {
        // Converts dip into its equivalent px
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }

    public static int dpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        // Converts dip into its equivalent px
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }

}
