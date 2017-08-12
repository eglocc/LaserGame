package com.ergizgizer.lasergame;

import android.graphics.Color;
import android.graphics.Paint;

public class Laser {

    private int mAngle;
    private boolean isOn;
    private BoardObject mSourceTile;
    protected final Paint mColor;
    protected float x1;
    protected float y1;
    protected float x2;
    protected float y2;


    public Laser() {
        mAngle = 90;
        mColor = new Paint();
        mColor.setAntiAlias(true);
        mColor.setStrokeWidth(3F);
        mColor.setColor(Color.RED);
        mColor.setStyle(Paint.Style.STROKE);
    }

    public int getmAngle() {
        return mAngle;
    }

    public void setmAngle(int angle) {
        this.mAngle = angle;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        this.isOn = on;
    }

    public BoardObject getmSourceTile() {
        return mSourceTile;
    }

    public void setmSourceTile(BoardObject sourceTile) {
        this.mSourceTile = sourceTile;
    }

    public void setLine(float x1, float y1, float x2, float startY, float endY) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = (float) (Math.ceil(Math.tan(Math.toRadians(this.mAngle))) * (x2 - x1) + y1);
        if (this.y2 > endY) {
            this.y2 = endY - 1;
            this.x2 = (float) (Math.ceil((y2 - y1) / Math.tan(Math.toRadians(mAngle)) + x1));
        } else if (this.y2 < startY) {
            this.y2 = startY + 1;
            this.x2 = (float) (Math.ceil((y2 - y1) / Math.tan(Math.toRadians(mAngle)) + x1));
        }
    }

    public void setLine(float x1, float y1, float x2, float end) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = end;
    }

    public void initLaser(float startX, float endX, float startY, float endY) {
        mSourceTile = getmSourceTile();
        int row = mSourceTile.getmRowIndex();
        int col = mSourceTile.getmColumnIndex();
        if (col == 0) {
            setmAngle(mAngle + 270);
            setLine(mSourceTile.left, mSourceTile.centerY(), endX, startY, endY);
        } else if (col == 9) {
            setmAngle((mAngle + 270) * (-1));
            setLine(mSourceTile.right, mSourceTile.centerY(), startX, startY, endY);
        } else if (row == 0) {
            if (mAngle == 90) {
                setLine(mSourceTile.centerX(), mSourceTile.top, mSourceTile.centerX(), endY);
            } else if (mAngle > 90 && mAngle < 180) {
                setLine(mSourceTile.centerX(), mSourceTile.top, startX, startY, endY);
            } else {
                setLine(mSourceTile.centerX(), mSourceTile.top, endY, startY, endY);
            }
        } else if (row == 9) {
            if (mAngle == 90) {
                setLine(mSourceTile.centerX(), mSourceTile.bottom, mSourceTile.centerX(), startY);
            } else if (mAngle > 90 && mAngle < 180) {
                setmAngle(mAngle * (-1));
                setLine(mSourceTile.centerX(), mSourceTile.bottom, startX, startY, endY);
            } else {
                setmAngle(mAngle * (-1));
                setLine(mSourceTile.centerX(), mSourceTile.bottom, endY, startY, endY);
            }
        }
    }


}
