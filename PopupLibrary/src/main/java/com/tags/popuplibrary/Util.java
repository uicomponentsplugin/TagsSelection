package com.tags.popuplibrary;

import android.content.Context;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Util {

    public static void setLayoutParamsMargin(LinearLayout.LayoutParams layoutParams, float dimensionInDp) {
        int dimensionInDpIntValue = (int) dimensionInDp;
        layoutParams.setMargins(dimensionInDpIntValue, dimensionInDpIntValue, dimensionInDpIntValue, dimensionInDpIntValue);
    }

    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
