package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Mirror extends BoardObject implements Rotatable {

    private int mAngle;
    private int mId;
    private Matrix mTransformationMatrix;
    private Bitmap mUnrotatedIcon;
    private Bitmap mRotatedIcon;

    public Mirror(Context context, int rowIndex, int columnIndex, int id) {
        super(context, rowIndex, columnIndex);
        this.mId = id;
        mUnrotatedIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.s56);
        setmIcon(mUnrotatedIcon);
        mTransformationMatrix = new Matrix();
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        mRotatedIcon = rotateBitmap(mAngle);
        setmIcon(mRotatedIcon);
        canvas.drawBitmap(getmIcon(), this.left, this.top, null);
        //desired to fix
        setmIcon(mUnrotatedIcon);
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        this.mId = id;
    }

    public int getAngle() {
        return mAngle;
    }

    public void setAngle(int angle) {
        this.mAngle = angle;
    }

    private Bitmap rotateBitmap(float angle) {
        mTransformationMatrix.reset();
        int width = getmIcon().getWidth();
        int height = getmIcon().getHeight();
        mTransformationMatrix.postRotate(mAngle, centerX(), centerY());
        return Bitmap.createBitmap(getmIcon(), 0, 0, width, height, mTransformationMatrix, true);
    }

}
