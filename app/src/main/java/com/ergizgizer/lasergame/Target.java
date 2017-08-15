package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Target extends BoardObject {

    static Bitmap sTargetIcon;

    public Target(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        canvas.drawBitmap(sTargetIcon, this.left, this.top, null);
    }
}
