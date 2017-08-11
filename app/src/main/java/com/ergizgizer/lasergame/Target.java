package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Target extends BoardObject {

    public Target(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.target);
        setmBitmap(bitmap);
        canvas.drawBitmap(bitmap, this.left, this.top, null);
    }
}
