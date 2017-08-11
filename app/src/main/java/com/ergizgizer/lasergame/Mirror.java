package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Mirror extends BoardObject {

    private int angle;

    public Mirror(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
        setmBitmap(BitmapFactory.decodeFile("app\\src\\main\\res\\drawable-mdpi\\satellite0.png"));
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite0);
        setmBitmap(bitmap);
        canvas.drawBitmap(bitmap, this.left, this.top, null);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    /*
    @Override
    public void draw(final Canvas canvas){
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, mRect.left, mRect.top,null);
    }
    */
}
