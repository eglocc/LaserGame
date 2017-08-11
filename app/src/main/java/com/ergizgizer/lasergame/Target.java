package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Target extends BoardObject {

    public Target(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        setmBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.target));
        Bitmap bitmap = getmBitmap();
        Rect rect = getmRect();
        canvas.drawBitmap(bitmap, rect.left, rect.top, null);
    }
}
