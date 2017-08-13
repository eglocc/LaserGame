package com.ergizgizer.lasergame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import static com.ergizgizer.lasergame.Line.Direction.DOWNWARDS_LEFT;
import static com.ergizgizer.lasergame.Line.Direction.UPWARDS_LEFT;

public class Laser extends Line implements Rotatable {

    private static final String TAG = Laser.class.getSimpleName();

    private Paint mBeam;

    private int mAngle;
    private boolean isOn;
    private BoardObject[][] mArea;
    private BoardObject mSourceTile;
    private ArrayList<BoardObject> mTilesIAmFlowingUpon;
    private BoardObject mBlockingTile;
    private ArrayList<PointF> mPoints;
    private PointF mIntersectionPoint;

    public Laser(BoardObject[][] tiles) {
        super();
        mBeam = new Paint();
        mBeam.setAntiAlias(true);
        mBeam.setStrokeWidth(3F);
        mBeam.setColor(Color.RED);
        mBeam.setStyle(Paint.Style.STROKE);
        this.mArea = tiles;
    }

    //Public getters and setters

    @Override
    public int getAngle() {
        return mAngle;
    }

    @Override
    public void setAngle(int angle) {
        this.mAngle = angle;
    }

    public Paint getmBeam() {
        return mBeam;
    }

    public void setmBeam(Paint beam) {
        this.mBeam = beam;
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

    public ArrayList<BoardObject> getmTiles() {
        return mTilesIAmFlowingUpon;
    }

    public BoardObject getmBlockingTile() {
        return mBlockingTile;
    }

    public ArrayList<PointF> getmPoints() {
        return mPoints;
    }

    //Main function of laser

    public void initLaser(final float startX, final float endX, final float startY, final float endY) {
        int row = mSourceTile.getmRowIndex();
        int col = mSourceTile.getmColumnIndex();
        if (col == 0) {
            setAngle(mAngle + 270);
            setLine(mSourceTile.left, mSourceTile.centerY(), endX, startX, endX, startY, endY);
        } else if (col == 9) {
            setAngle((mAngle + 270) * (-1));
            setLine(mSourceTile.right, mSourceTile.centerY(), startX, startX, endX, startY, endY);
        } else if (row == 0) {
            if (mAngle == 90) {
                setLine(mSourceTile.centerX(), mSourceTile.top, mSourceTile.centerX(), endY);
            } else if (mAngle > 90 && mAngle < 180) {
                setLine(mSourceTile.centerX(), mSourceTile.top, startX, startX, endX, startY, endY);
            } else {
                setLine(mSourceTile.centerX(), mSourceTile.top, endX, startX, endX, startY, endY);
            }
        } else if (row == 9) {
            if (mAngle == 90) {
                setLine(mSourceTile.centerX(), mSourceTile.bottom, mSourceTile.centerX(), startY);
            } else if (mAngle > 90 && mAngle < 180) {
                setAngle(mAngle * (-1));
                setLine(mSourceTile.centerX(), mSourceTile.bottom, startX, startX, endX, startY, endY);
            } else {
                setAngle(mAngle * (-1));
                setLine(mSourceTile.centerX(), mSourceTile.bottom, endX, startX, endX, startY, endY);
            }
        }
        setmDirection(startX, endX, startY, endY);
        mBlockingTile = getBlockingTile(startX, endX, startY, endY);
        Log.d(TAG, getmDirection().toString());
        mIntersectionPoint = getIntersectionPoint();
        evaluateLaserState();
    }

    // Private methods

    @Override
    public void setLine(float x1, float y1, float x2, float y2) {
        super.setLine(x1, y1, x2, y2);
        mPoints = getPointsInInterval(x1, x2);
    }

    private void setLine(float x1, float y1, float x2, float startX, float endX, float startY, float endY) {
        super.setLine(x1, y1, x2, getSlope() * (x2 - x1) + y1);
        if (this.y2 > endY) {
            this.y2 = endY - 1;
            this.x2 = (this.y2 - this.y1) / getSlope() + this.x1;
        } else if (this.y2 < startY) {
            this.y2 = startY + 1;
            this.x2 = (this.y2 - this.y1) / getSlope() + this.x1;
        } else if (this.x2 > endX) {
            this.x2 = endX - 1;
            this.y2 = (this.x2 - this.x1) * getSlope() + this.y1;

        } else if (this.x2 < startX) {
            this.x2 = startX + 1;
            this.y2 = (this.x2 - this.x1) * getSlope() + this.y1;
        }
        mPoints = getPointsInInterval(x1, x2);
    }

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

    private ArrayList<BoardObject> getTileListLaserIsOn(final float startX, final float endX, final float startY, final float endY) {
        ArrayList<BoardObject> laserIsOn = new ArrayList<>();
        if (y1 == startY || y1 == endY) {
            for (int i = 0; i < mArea.length; i++) {
                for (int j = 0; j < mArea[i].length; j++) {
                    if (intersects(mArea[i][j]))
                        laserIsOn.add(mArea[i][j]);
                }
            }
        } else if (x1 == startX || x1 == endX) {
            for (int i = 0; i < mArea.length; i++) {
                for (int j = 0; j < mArea[i].length; j++) {
                    if (intersects(mArea[j][i]))
                        laserIsOn.add(mArea[j][i]);
                }
            }
        }
        return laserIsOn;
    }

    @Nullable
    private BoardObject getBlockingTile(final float startX, final float endX, final float startY, final float endY) {
        BoardObject blockingObject = null;
        if (x1 == startX) {
            switch (getmDirection()) {
                case DOWNWARDS_RIGHT:
                    for (int i = 0; i < mArea.length; i++) {
                        for (int j = 0; j < mArea[i].length; j++) {
                            if (intersects(mArea[j][i]) && !(mArea[j][i] instanceof Air)) {
                                blockingObject = mArea[j][i];
                                //For breaking out both loops when this condition is met
                                i = mArea.length;
                                break;
                            }
                        }
                    }
                    break;
                case UPWARDS_LEFT:
                    for (int i = 0; i < mArea.length; i++) {
                        for (int j = mArea[i].length - 1; j >= 0; j--) {
                            if (intersects(mArea[j][i]) && !(mArea[j][i] instanceof Air)) {
                                blockingObject = mArea[j][i];
                                //For breaking out both loops when this condition is met
                                i = mArea.length;
                                break;
                            }
                        }
                    }
                    break;
                case CONSTANT_RIGHT:
                    int j = mSourceTile.getmRowIndex();
                    for (int i = 0; i < mArea[j].length; i++) {
                        if (intersects(mArea[j][i]) && !(mArea[j][i] instanceof Air)) {
                            blockingObject = mArea[j][i];
                            break;
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "What are the odds?");
            }
        } else if (y1 == startY) {
            switch (getmDirection()) {
                case DOWNWARDS_RIGHT:
                    for (int i = 0; i < mArea.length; i++) {
                        for (int j = 0; j < mArea[i].length; j++) {
                            if (intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                                blockingObject = mArea[i][j];
                                //For breaking out both loops when this condition is met
                                i = mArea.length;
                                break;
                            }
                        }
                    }
                    break;
                case DOWNWARDS_LEFT:
                    for (int i = 0; i < mArea.length; i++) {
                        for (int j = mArea.length - 1; j >= 0; j--) {
                            if (intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                                blockingObject = mArea[i][j];
                                //For breaking out both loops when this condition is met
                                i = mArea.length;
                                break;
                            }
                        }
                    }
                    break;
                case CONSTANT_DOWN:
                    int j = mSourceTile.getmColumnIndex();
                    for (int i = 0; i < mArea.length; i++) {
                        if (intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                            blockingObject = mArea[i][j];
                            break;
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "What are the odds?");
            }
        } else if (x1 == endX) {
            switch (getmDirection()) {
                case DOWNWARDS_LEFT:
                    for (int i = mArea.length - 1; i >= 0; i--) {
                        for (int j = 0; j < mArea[i].length; j++) {
                            if (intersects(mArea[j][i]) && !(mArea[j][i] instanceof Air)) {
                                blockingObject = mArea[j][i];
                                //For breaking out both loops when this condition is met
                                i = -1;
                                break;
                            }
                        }
                    }
                    break;
                case UPWARDS_RIGHT:
                    for (int i = mArea.length - 1; i >= 0; i--) {
                        for (int j = mArea[i].length - 1; j >= 0; j--) {
                            if (intersects(mArea[j][i]) && !(mArea[j][i] instanceof Air)) {
                                blockingObject = mArea[j][i];
                                //For breaking out both loops when this condition is met
                                i = -1;
                                break;
                            }
                        }
                    }
                    break;
                case CONSTANT_LEFT:
                    int j = mSourceTile.getmRowIndex();
                    for (int i = mArea[j].length; i >= 0; i--) {
                        if (intersects(mArea[j][i]) && !(mArea[j][i] instanceof Air)) {
                            blockingObject = mArea[j][i];
                            break;
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "What are the odds?");
            }
        } else if (y1 == endY) {
            switch (getmDirection()) {
                case UPWARDS_RIGHT:
                    for (int i = mArea.length - 1; i >= 0; i--) {
                        for (int j = 0; j < mArea[i].length; j++) {
                            if (intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                                blockingObject = mArea[i][j];
                                //For breaking out both loops when this condition is met
                                i = -1;
                                break;
                            }
                        }
                    }
                    break;
                case UPWARDS_LEFT:
                    for (int i = mArea.length - 1; i >= 0; i--) {
                        for (int j = mArea[i].length - 1; j >= 0; j--) {
                            if (intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                                blockingObject = mArea[i][j];
                                //For breaking out both loops when this condition is met
                                i = -1;
                                break;
                            }
                        }
                    }
                    break;
                case CONSTANT_UP:
                    int j = mSourceTile.getmColumnIndex();
                    for (int i = mArea.length - 1; i >= 0; i--) {
                        if (intersects(mArea[i][j]) && !(mArea[i][j] instanceof Air)) {
                            blockingObject = mArea[i][j];
                            break;
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "What are the odds?");
            }
        }

        return blockingObject;
    }

    @Nullable
    private PointF getIntersectionPoint() {
        PointF intersectionPoint = null;

        if (mBlockingTile == null) {
            return null;
        }

        Line left = new Line(mBlockingTile.left, mBlockingTile.top, mBlockingTile.left, mBlockingTile.bottom);
        Line top = new Line(mBlockingTile.left, mBlockingTile.top, mBlockingTile.right, mBlockingTile.top);
        Line right = new Line(mBlockingTile.right, mBlockingTile.top, mBlockingTile.right, mBlockingTile.bottom);
        Line bottom = new Line(mBlockingTile.left, mBlockingTile.bottom, mBlockingTile.right, mBlockingTile.bottom);

        switch (getmDirection()) {
            case CONSTANT_DOWN:
                intersectionPoint = new PointF(mBlockingTile.centerX(), mBlockingTile.top);
                break;
            case CONSTANT_LEFT:
                intersectionPoint = new PointF(mBlockingTile.right, mBlockingTile.centerY());
                break;
            case CONSTANT_RIGHT:
                intersectionPoint = new PointF(mBlockingTile.left, mBlockingTile.centerY());
                break;
            case CONSTANT_UP:
                intersectionPoint = new PointF(mBlockingTile.centerX(), mBlockingTile.bottom);
                break;
            case DOWNWARDS_LEFT:
                if (intersectsLine(top)) {
                    final float y = mBlockingTile.top;
                    final float x = getXValue(y);
                    intersectionPoint = new PointF(x, y);
                } else if (intersectsLine(right)) {
                    final float x = mBlockingTile.right;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                }
                break;
            case DOWNWARDS_RIGHT:
                if (intersectsLine(top)) {
                    final float y = mBlockingTile.top;
                    final float x = getXValue(y);
                    intersectionPoint = new PointF(x, y);
                } else if (intersectsLine(left)) {
                    final float x = mBlockingTile.left;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                }
                break;
            case UPWARDS_LEFT:
                if (intersectsLine(bottom)) {
                    final float y = mBlockingTile.bottom;
                    final float x = getXValue(y);
                    intersectionPoint = new PointF(x, y);
                } else if (intersectsLine(left)) {
                    final float x = mBlockingTile.left;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                } else if (intersectsLine(right)) {
                    final float x = mBlockingTile.right;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                }
                break;
            case UPWARDS_RIGHT:
                if (intersectsLine(bottom)) {
                    final float y = mBlockingTile.bottom;
                    final float x = getXValue(y);
                    intersectionPoint = new PointF(x, y);
                } else if (intersectsLine(left)) {
                    final float x = mBlockingTile.left;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                } else if (intersectsLine(right)) {
                    final float x = mBlockingTile.right;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                }
                break;

        }

        return intersectionPoint;
    }

    private PointF getBlockingPoint() {
        PointF blockingPoint = null;

        if (mBlockingTile != null) {
            if (mIntersectionPoint.x == mBlockingTile.left) {
                Iterator<PointF> it = getPointsInInterval(mBlockingTile.left, mBlockingTile.right).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                        }
                    }
                }
            } else if (mIntersectionPoint.x == mBlockingTile.right) {
                Iterator<PointF> it = getPointsInInterval(mBlockingTile.right, mBlockingTile.left).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                        }
                    }
                }
            } else if ((mIntersectionPoint.y == mBlockingTile.top || mIntersectionPoint.y == mBlockingTile.bottom)
                    && (getmDirection() == Direction.DOWNWARDS_RIGHT || getmDirection() == Direction.UPWARDS_RIGHT)) {
                Iterator<PointF> it = getPointsInInterval(mIntersectionPoint.x, mBlockingTile.right).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                        }
                    }
                }
            } else if ((mIntersectionPoint.y == mBlockingTile.top || mIntersectionPoint.y == mBlockingTile.bottom)
                    && (getmDirection() == DOWNWARDS_LEFT || getmDirection() == UPWARDS_LEFT)) {
                Iterator<PointF> it = getPointsInInterval(mIntersectionPoint.x, mBlockingTile.left).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                        }
                    }
                }
            } else if ((mIntersectionPoint.y == mBlockingTile.top || mIntersectionPoint.y == mBlockingTile.bottom)
                    && (getmDirection() == Direction.CONSTANT_DOWN || getmDirection() == Direction.CONSTANT_UP)) {
                Iterator<PointF> it = getPointsInInterval(mIntersectionPoint.x, mIntersectionPoint.x).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                        }
                    }
                }
            }
        }

        return blockingPoint;
    }

    private void evaluateLaserState() {
        if (mBlockingTile != null && mIntersectionPoint != null) {
            setLine(x1, y1, mIntersectionPoint.x, mIntersectionPoint.y);
        }
    }

    private static boolean isTransparent(Bitmap bitmap, int x, int y) {
        int pixel = bitmap.getPixel(x, y);
        if ((pixel >> 24) == 0) {
            return true;
        }
        return false;
    }
}
