package com.ergizgizer.lasergame;

import android.content.Context;

public class Air extends BoardObject {

    private boolean mValidForMirror;

    public Air(Context context, int rowIndex, int columnIndex) {
        super(context, rowIndex, columnIndex);
    }
}
