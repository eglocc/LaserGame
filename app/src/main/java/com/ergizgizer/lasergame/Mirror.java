package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Mirror extends BoardObject implements Rotatable {

    private int mAngle;
    private int mId;

    public Mirror(int rowIndex, int columnIndex, int id) {
        super(rowIndex, columnIndex);
        this.mId = id;
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.s72);
        setmBitmap(bitmap);
        canvas.rotate(mAngle);
        canvas.drawBitmap(bitmap, this.left, this.top, null);
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        this.mId = id;
    }

    public int getmAngle() {
        return mAngle;
    }

    public void setmAngle(int angle) {
        this.mAngle = angle;
    }
}
