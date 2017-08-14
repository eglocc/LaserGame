package com.ergizgizer.lasergame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_BOTTOM;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_LEFT;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_RIGHT;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_TOP;
import static com.ergizgizer.lasergame.Line.Direction.DOWNWARDS_LEFT;
import static com.ergizgizer.lasergame.Line.Direction.UPWARDS_LEFT;

public class Laser extends Line implements Rotatable, MyStaticVariables {

    private static final String TAG = Laser.class.getSimpleName();

    enum IntersectionDirection {FROM_LEFT, FROM_RIGHT, FROM_TOP, FROM_BOTTOM}

    private Paint mBeam;

    private int mAngle;
    private int mRelativeAngle; // Differs from angle, it is relative to the starting point
    private boolean isOn;
    private BoardModel mBoard;
    private BoardObject[][] mArea;
    private BoardObject mSourceTile;
    private ArrayList<BoardObject> mTilesIAmFlowingUpon;
    private BoardObject mBlockingTile;
    private ArrayList<PointF> mPoints;
    private IntersectionDirection mIntersectionDirection;
    private PointF mIntersectionPoint;
    private PointF mBlockingPoint;

    public Laser(BoardModel model) {
        super();
        mBeam = new Paint();
        mBeam.setAntiAlias(true);
        mBeam.setStrokeWidth(3F);
        mBeam.setColor(Color.RED);
        mBeam.setStyle(Paint.Style.STROKE);
        this.mBoard = model;
        this.mArea = model.getObjects();
    }

    // Public getters and setters

    /**
     * Implemented from Rotatable interface
     *
     * @return
     */
    @Override
    public int getAngle() {
        return mAngle;
    }

    /**
     * Implemented from Rotatable interface
     *
     * @param angle
     */
    @Override
    public void setAngle(int angle) {
        this.mAngle = angle; }

    public int getmRelativeAngle() {
        return mRelativeAngle;
    }

    public void setmRelativeAngle(int relativeAngle) {
        this.mRelativeAngle = relativeAngle;
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

    public void setmSourceTile(BoardObject object) {
        this.mSourceTile = object;
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

    /** Main function for laser, combines all calculations
     *
     * @param startX Left of board
     * @param endX Right of board
     * @param startY Top of board
     * @param endY Bottom of board
     */
    public void initLaser(final float startX, final float endX, final float startY, final float endY) {
        int row = mSourceTile.getmRowIndex();
        int col = mSourceTile.getmColumnIndex();
        if (col == 0) {
            mRelativeAngle = mAngle + 270;
            setLine(mSourceTile.left, mSourceTile.centerY(), endX, startX, endX, startY, endY);
        } else if (col == 9) {
            mRelativeAngle = (mAngle + 270) * (-1);
            setLine(mSourceTile.right, mSourceTile.centerY(), startX, startX, endX, startY, endY);
        } else if (row == 0) {
            mRelativeAngle = mAngle;
            if (mAngle == 90) {
                setLine(mSourceTile.centerX(), mSourceTile.top, mSourceTile.centerX(), endY);
            } else if (mAngle > 90 && mAngle < 180) {
                setLine(mSourceTile.centerX(), mSourceTile.top, startX, startX, endX, startY, endY);
            } else {
                setLine(mSourceTile.centerX(), mSourceTile.top, endX, startX, endX, startY, endY);
            }
        } else if (row == 9) {
            if (mAngle == 90) {
                mRelativeAngle = mAngle;
                setLine(mSourceTile.centerX(), mSourceTile.bottom, mSourceTile.centerX(), startY);
            } else if (mAngle > 90 && mAngle < 180) {
                mRelativeAngle = mAngle * (-1);
                setLine(mSourceTile.centerX(), mSourceTile.bottom, startX, startX, endX, startY, endY);
            } else {
                mRelativeAngle = mAngle * (-1);
                setLine(mSourceTile.centerX(), mSourceTile.bottom, endX, startX, endX, startY, endY);
            }
        }
        setmDirection(startX, endX, startY, endY);
        mBlockingTile = getBlockingTile(startX, endX, startY, endY);
        Log.d(TAG, getmDirection().toString());
        mIntersectionPoint = getIntersectionPoint(startX, endX, startY, endY);
        mBlockingPoint = getBlockingPoint();
        evaluateLaserState(startX, endX, startY, endY);
    }

    // Private methods

    /**
     * Helper method for calculating blocking objects
     *
     * @param startX Left of board
     * @param endX   Right of board
     * @param startY Top of board
     * @param endY   Bottom of board
     */
    private void evaluateLaserState(final float startX, final float endX, final float startY, final float endY) {
        if (mBlockingTile != null && mBlockingPoint != null) {
            setLine(x1, y1, mBlockingPoint.x, mBlockingPoint.y);
            if (mBlockingTile instanceof Mirror) {
                reflect(mBlockingPoint.x, mBlockingPoint.y, startX, endX, startY, endY);
            }
        }
    }

    private void reflect(final float fromX, final float fromY, final float startX, final float endX, final float startY, final float endY) {
        Log.d(TAG, "reflectedWith:" + mAngle + "°");
        Laser newSegment = new Laser(mBoard);
        mBoard.addNewLaserSegment(newSegment);
        newSegment.setOn(true);
        switch (mIntersectionDirection) {
            case FROM_LEFT:
                newSegment.setmSourceTile(mBlockingTile);
                newSegment.setAngle(mAngle);
                newSegment.setLine(mBlockingPoint.x, mBlockingPoint.y, startX, startX, endX, startY, endY);
                setLine(mSourceTile.right, mSourceTile.centerY(), startX, startX, endX, startY, endY);
        }


    }

    /** Overridden from parent class Line
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    @Override
    public void setLine(float x1, float y1, float x2, float y2) {
        super.setLine(x1, y1, x2, y2);
        mPoints = getPointsInInterval(x1, x2);
    }

    private void setLine(float x1, float y1, float x2, float startX, float endX, float startY, float endY) {
        super.setLine(x1, y1, x2, getSlope() * (x2 - x1) + y1);
        if (this.y2 > endY) {
            this.y2 = endY;
            this.x2 = (this.y2 - this.y1) / getSlope() + this.x1;
        } else if (this.y2 < startY) {
            this.y2 = startY;
            this.x2 = (this.y2 - this.y1) / getSlope() + this.x1;
        } else if (this.x2 > endX) {
            this.x2 = endX;
            this.y2 = (this.x2 - this.x1) * getSlope() + this.y1;

        } else if (this.x2 < startX) {
            this.x2 = startX;
            this.y2 = (this.x2 - this.x1) * getSlope() + this.y1;
        }
        mPoints = getPointsInInterval(x1, x2);
    }

    /** Returns the slope of the laser
     *
     * @return
     */
    private float getSlope() {
        return (float) Math.tan(Math.toRadians(mRelativeAngle));
    }

    /** Returns the Y-Intercept of the laser
     *
     * @return
     */
    private float getYIntercept() {
        return y1 - getSlope() * x1;
    }

    /** Finds the Y-value for any X-value
     *  Same as y = f(x)
     *
     * @param x
     * @return
     */
    private float getYValue(float x) {
        return getSlope() * x + getYIntercept();
    }

    /** Finds the X-value for any Y-value
     * Same as inverse of f(x)
     *
     * @param y
     * @return
     */
    private float getXValue(float y) {
        return (y - getYIntercept()) / getSlope();
    }

    /** Self-explanatory
     *
     * @param x1
     * @param x2
     * @return
     */
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

    /** This method finds all tiles, on which the laser is beamed. I was discouraged to use that.
     *
     * @param startX Left of board
     * @param endX Right of board
     * @param startY Top of board
     * @param endY Bottom of board
     * @return
     */
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

    /** Self explanatory, but in case of emergency: This method finds the blocking tile.
     *
     * @param startX Left of board
     * @param endX Right of board
     * @param startY Top of board
     * @param endY Bottom of board
     * @return
     */
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
                        for (int j = mArea[i].length - 1; j >= 0; j--) {
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
                    for (int i = mArea[j].length - 1; i >= 0; i--) {
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

    /** This method finds the intersection point of the laser and the blocking tile.
     *
     * @param startX Left of board
     * @param endX Right of board
     * @param startY Top of board
     * @param endY Bottom of board
     * @return
     */
    @Nullable
    private PointF getIntersectionPoint(final float startX, final float endX, final float startY, final float endY) {
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
                // when y1 = starty and x1 = endx;
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
                // when y1 = starty and x1 = startx
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
                //when y1 = endy and x1 = startx
                if (intersectsLine(bottom)) {
                    final float y = mBlockingTile.bottom;
                    final float x = getXValue(y);
                    intersectionPoint = new PointF(x, y);
                } else if (x1 == startX && intersectsLine(left)) {
                    final float x = mBlockingTile.left;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                } else if (y1 == endY && intersectsLine(right)) {
                    final float x = mBlockingTile.right;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                }
                break;
            case UPWARDS_RIGHT:
                //when y1 = endy and  x1 = endx
                if (intersectsLine(bottom)) {
                    final float y = mBlockingTile.bottom;
                    final float x = getXValue(y);
                    intersectionPoint = new PointF(x, y);
                } else if (y1 == endY && intersectsLine(left)) {
                    final float x = mBlockingTile.left;
                    final float y = getYValue(x);
                    intersectionPoint = new PointF(x, y);
                } else if (x1 == endX && intersectsLine(right)) {
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
                        if (!isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                            mIntersectionDirection = FROM_LEFT;
                            break;
                        }
                    }
                }
            } else if (mIntersectionPoint.x == mBlockingTile.right) {
                Iterator<PointF> it = getPointsInInterval(mBlockingTile.right, mBlockingTile.left).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (!isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                            mIntersectionDirection = FROM_RIGHT;
                            break;
                        }
                    }
                }
            } else if ((mIntersectionPoint.y == mBlockingTile.top || mIntersectionPoint.y == mBlockingTile.bottom)
                    && (getmDirection() == Direction.DOWNWARDS_RIGHT || getmDirection() == Direction.UPWARDS_RIGHT)) {
                Iterator<PointF> it = getPointsInInterval(mIntersectionPoint.x, mBlockingTile.right).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (!isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                            if (mIntersectionPoint.y == mBlockingTile.top)
                                mIntersectionDirection = FROM_TOP;
                            else if (mIntersectionPoint.y == mBlockingTile.bottom)
                                mIntersectionDirection = FROM_BOTTOM;
                            break;
                        }
                    }
                }
            } else if ((mIntersectionPoint.y == mBlockingTile.
                    top || mIntersectionPoint.y == mBlockingTile.bottom)
                    && (getmDirection() == DOWNWARDS_LEFT || getmDirection() == UPWARDS_LEFT)) {
                Iterator<PointF> it = getPointsInInterval(mIntersectionPoint.x, mBlockingTile.left).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (!isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                            if (mIntersectionPoint.y == mBlockingTile.top)
                                mIntersectionDirection = FROM_TOP;
                            else if (mIntersectionPoint.y == mBlockingTile.bottom)
                                mIntersectionDirection = FROM_BOTTOM;
                            break;
                        }
                    }
                }
            } else if ((mIntersectionPoint.y == mBlockingTile.top || mIntersectionPoint.y == mBlockingTile.bottom)
                    && (getmDirection() == Direction.CONSTANT_DOWN || getmDirection() == Direction.CONSTANT_UP)) {
                Iterator<PointF> it = getPointsInInterval(mIntersectionPoint.x, mIntersectionPoint.x).iterator();
                while (it.hasNext()) {
                    PointF p = it.next();
                    if (mBlockingTile.contains(p.x, p.y)) {
                        if (!isTransparent(mBlockingTile.getmIcon(), (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                            blockingPoint = p;
                            if (mIntersectionPoint.y == mBlockingTile.top)
                                mIntersectionDirection = FROM_TOP;
                            else if (mIntersectionPoint.y == mBlockingTile.bottom)
                                mIntersectionDirection = FROM_BOTTOM;
                            break;
                        }
                    }
                }
            }
        }

        return blockingPoint;
    }

    private static boolean isTransparent(Bitmap bitmap, int x, int y) {
        int pixel = bitmap.getPixel(x, y);
        if ((pixel >> 24) == 0) {
            return true;
        }
        return false;
    }
}
