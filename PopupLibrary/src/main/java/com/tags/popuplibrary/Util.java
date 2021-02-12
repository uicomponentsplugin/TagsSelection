package com.tags.popuplibrary;

import android.content.Context;
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
}
