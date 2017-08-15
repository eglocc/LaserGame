package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;

public class Mirror extends BoardObject implements Rotatable {

    static Bitmap sUnrotatedMirrorIcon;

    private int mAngle;
    private int mId;
    private Matrix mTransformationMatrix;
    private Bitmap mRotatedIcon;
    private boolean mBottomHit;
    private boolean mTopHit;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAngle);
        dest.writeInt(mId);
    }

    private Mirror(Parcel source) {
        super(source);
        mAngle = source.readInt();
        mId = source.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Mirror(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    public Mirror(int rowIndex, int columnIndex, int id) {
        super(rowIndex, columnIndex);
        this.mId = id;
        mTransformationMatrix = new Matrix();
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        mRotatedIcon = rotateBitmap();
        canvas.drawBitmap(mRotatedIcon, this.left, this.top, null);
        //desired to fix
        mRotatedIcon = sUnrotatedMirrorIcon;
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

    Bitmap rotateBitmap() {
        mTransformationMatrix.reset();
        int width = sUnrotatedMirrorIcon.getWidth();
        int height = sUnrotatedMirrorIcon.getHeight();
        mTransformationMatrix.postRotate(mAngle, centerX(), centerY());
        return Bitmap.createBitmap(sUnrotatedMirrorIcon, 0, 0, width, height, mTransformationMatrix, true);
    }

}
