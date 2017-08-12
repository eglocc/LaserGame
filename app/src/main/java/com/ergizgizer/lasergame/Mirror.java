package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Mirror extends BoardObject {

    private int angle;

    public Mirror(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.s72);
        setmBitmap(bitmap);
        canvas.drawBitmap(bitmap, this.left, this.top, null);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
