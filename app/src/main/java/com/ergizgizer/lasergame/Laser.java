package com.ergizgizer.lasergame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

public class Laser {

    private static final String TAG = Laser.class.getSimpleName();

    protected float x1;
    protected float y1;
    protected float x2;
    protected float y2;

    private int mAngle;
    private boolean isOn;
    private BoardObject[][] mArea;
    private BoardObject mSourceTile;
    private Paint mBeam;
    private ArrayList<PointF> mPoints;
    private ArrayList<BoardObject> mTiles;
    private BoardObject mBlockingTile;


    public Laser(BoardObject[][] tiles) {
        mAngle = 90;
        mBeam = new Paint();
        mBeam.setAntiAlias(true);
        mBeam.setStrokeWidth(3F);
        mBeam.setColor(Color.RED);
        mBeam.setStyle(Paint.Style.STROKE);
        this.mArea = tiles;
    }

    //Public getters and setters

    public Paint getmBeam() {
        return mBeam;
    }

    public void setmBeam(Paint beam) {
        this.mBeam = beam;
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

    public void setmArea(BoardObject[][] area) {
        this.mArea = area;
    }

    public void setmSourceTile(int row, int col) {
        this.mSourceTile = mArea[row][col];
    }

    public ArrayList<PointF> getmPoints() {
        return mPoints;
    }

    public ArrayList<BoardObject> getmTiles() {
        return mTiles;
    }

    public BoardObject getmBlockingTile() {
        return mBlockingTile;
    }

    public void setLine(float x1, float y1, float x2, float startY, float endY) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = getSlope() * (x2 - x1) + y1;
        if (this.y2 > endY) {
            this.y2 = endY - 1;
            this.x2 = (y2 - y1) / getSlope() + x1;
        } else if (this.y2 < startY) {
            this.y2 = startY + 1;
            this.x2 = (y2 - y1) / getSlope() + x1;
        }

        mPoints = getPointsInInterval(x1, x2);
        Log.d(TAG, mPoints.toString());
    }

    public void setLine(float x1, float y1, float x2, float end) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = end;

        mPoints = getPointsInInterval(x1, x2);
    }

    //Main function of laser

    public void initLaser(float startX, float endX, float startY, float endY) {
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
        mBlockingTile = getBlockingTile(startX, endX, startY, endY);
        evaluateLaserState();
    }

    // Private getters

    private float getSlope() {
        return (float) Math.tan(Math.toRadians(mAngle));
    }

    private float getYIntercept() {
        return y1 - getSlope() * x1;
    }

    private float getYValue(float x) {
        return getSlope() * x + getYIntercept();
    }

    private float getXValue(float y) {
        return (y - getYIntercept()) / getSlope();
    }

    private ArrayList<PointF> getPointsInInterval(float x1, float x2) {
        ArrayList<PointF> points = new ArrayList<>();
        if (x1 < x2) {
            for (float i = x1; i <= x2; i++) {
                points.add(new PointF(i, getYValue(i)));
            }
        } else if (x1 > x2) {
            for (float i = x1; i >= x2; i--) {
                points.add(new PointF(i, getYValue(i)));
            }
        } else {
            if (y1 < y2) {
                for (float i = y1; i <= y2; i++) {
                    points.add(new PointF(x1, i));
                }
            } else {
                for (float i = y1; i >= y2; i--) {
                    points.add(new PointF(x1, i));
                }
            }
        }
        return points;
    }

    private boolean intersects(RectF rect) {
        for (PointF p : mPoints) {
            if (rect.contains(p.x, p.y))
                return true;
        }
        return false;
    }

    private ArrayList<BoardObject> getTileListLaserIsOn(float startX, float endX, float startY, float endY) {
        ArrayList<BoardObject> laserIsOn = new ArrayList<>();
        if (y1 == startY || y1 == endY) {
            for (int i = 0; i < mArea.length; i++) {
                for (int j = 0; j < mArea[i].length; j++) {
                    if (this.intersects(mArea[i][j]))
                        laserIsOn.add(mArea[i][j]);
                }
            }
        } else if (x1 == startX || x1 == endX) {
            for (int i = 0; i < mArea.length; i++) {
                for (int j = 0; j < mArea[i].length; j++) {
                    if (this.intersects(mArea[j][i]))
                        laserIsOn.add(mArea[j][i]);
                }
            }
        }
        return laserIsOn;
    }

    private BoardObject getBlockingTile(float startX, float endX, float startY, float endY) {
        if (y1 > y2) {
            if (x1 == endX) {
                for (int i = mArea.length - 1; i >= 0; i--) {
                    for (int j = mArea[i].length - 1; j >= 0; j--) {
                        if (this.intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                            return mArea[i][j];
                        }
                    }
                }
            } else {
                for (int i = mArea.length - 1; i >= 0; i--) {
                    for (int j = 0; j < mArea[i].length; j++) {
                        if (this.intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                            return mArea[i][j];
                        }
                    }
                }
            }
        } else if (y1 < y2) {
            for (int i = 0; i < mArea.length; i++) {
                for (int j = 0; j < mArea[i].length; j++) {
                    if (this.intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                        return mArea[i][j];
                    }
                }
            }
        } else {
            int i = mSourceTile.getmRowIndex();
            if (x1 > x2) {
                for (int j = mArea.length - 1; j >= 0; j--) {
                    if (this.intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                        return mArea[i][j];
                    }
                }
            } else {
                for (int j = 0; j < mArea.length; j++) {
                    if (this.intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                        return mArea[i][j];
                    }
                }
            }
        }
        return null;
    }

    private void evaluateLaserState() {
        if (mBlockingTile != null) {
            setLine(x1, y1, mBlockingTile.centerX(), mBlockingTile.centerY());
        }
    }
}
