package com.ergizgizer.lasergame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import static android.R.attr.endX;
import static android.R.attr.endY;
import static android.R.attr.startX;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_BOTTOM;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_LEFT;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_RIGHT;
import static com.ergizgizer.lasergame.Laser.IntersectionDirection.FROM_TOP;

public class Laser extends Line implements Rotatable, Parcelable, MyStaticVariables {

    private static final String TAG = Laser.class.getSimpleName();

    enum IntersectionDirection {FROM_LEFT, FROM_RIGHT, FROM_TOP, FROM_BOTTOM}

    private Paint mBeam;

    private int mAngle;
    private int mRelativeAngle; // Differs from angle, it is relative to the starting point
    private boolean isOn;
    private boolean targetHit;
    private BoardModel mBoard;
    private BoardObject[][] mArea;
    private RectF mAreaDimension;
    private BoardObject mSourceTile;
    private ArrayList<BoardObject> mTilesIAmFlowingUpon;
    private BoardObject mBlockingTile;
    private ArrayList<PointF> mPoints;
    private IntersectionDirection mIntersectionDirection;
    private PointF mIntersectionPoint;
    private PointF mBlockingPoint;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAngle);
        dest.writeByte((byte) (isOn ? 1 : 0));
        dest.writeByte((byte) (targetHit ? 1 : 0));
        dest.writeParcelable(mSourceTile, flags);
        dest.writeParcelable(mBlockingTile, flags);
    }

    private Laser(Parcel source) {
        super(source);
        mAngle = source.readInt();
        isOn = source.readByte() != 0;
        targetHit = source.readByte() != 0;
        mSourceTile = source.readParcelable(BoardObject.class.getClassLoader());
        mBlockingTile = source.readParcelable(BoardObject.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Laser(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    public Laser(BoardModel model) {
        super();
        mBeam = new Paint();
        mBeam.setAntiAlias(true);
        mBeam.setStrokeWidth(3F);
        mBeam.setColor(Color.RED);
        mBeam.setStyle(Paint.Style.STROKE);
        this.mBoard = model;
        this.mArea = model.getObjects();
        this.mAreaDimension = model.getmBoardDimension();
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

    public boolean isTargetHit() {
        return targetHit;
    }

    public void setmArea(BoardObject[][] area) {
        this.mArea = area;
    }

    public void setmAreaDimension(RectF dimension) {
        this.mAreaDimension = dimension;
    }

    public BoardObject getmSourceTile() {
        return mSourceTile;
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
     */
    public void initLaser() {
        int row = mSourceTile.getmRowIndex();
        int col = mSourceTile.getmColumnIndex();
        if (col == 0) {
            mRelativeAngle = mAngle + 270;
            setLine(mSourceTile.left, mSourceTile.centerY(), mAreaDimension.right);
        } else if (col == 9) {
            mRelativeAngle = (mAngle + 270) * (-1);
            setLine(mSourceTile.right, mSourceTile.centerY(), mAreaDimension.left);
        } else if (row == 0) {
            mRelativeAngle = mAngle;
            if (mAngle == 90) {
                setLine(mSourceTile.centerX(), mSourceTile.top, mSourceTile.centerX(), mAreaDimension.bottom);
            } else if (mAngle > 90 && mAngle < 180) {
                setLine(mSourceTile.centerX(), mSourceTile.top, mAreaDimension.left);
            } else {
                setLine(mSourceTile.centerX(), mSourceTile.top, mAreaDimension.right);
            }
        } else if (row == 9) {
            if (mAngle == 90) {
                mRelativeAngle = mAngle;
                setLine(mSourceTile.centerX(), mSourceTile.bottom, mSourceTile.centerX(), mAreaDimension.top);
            } else if (mAngle > 90 && mAngle < 180) {
                mRelativeAngle = mAngle * (-1);
                setLine(mSourceTile.centerX(), mSourceTile.bottom, mAreaDimension.left);
            } else {
                mRelativeAngle = mAngle * (-1);
                setLine(mSourceTile.centerX(), mSourceTile.bottom, mAreaDimension.right);
            }
        }
        setmDirection(mAreaDimension);
        mBlockingTile = getBlockingTile();
        Log.d(TAG, getmDirection().toString());
        mIntersectionPoint = getIntersectionPoint();
        mBlockingPoint = getBlockingPoint();
        evaluateLaserState();
    }

    // Private methods

    /**
     * Helper method for calculating blocking objects
     *
     */
    private void evaluateLaserState() {
        if (mBlockingTile != null && mBlockingPoint != null) {
            setLine(x1, y1, mBlockingPoint.x, mBlockingPoint.y);
            if (mBlockingTile instanceof Mirror) {
                reflect();
            } else if (mBlockingTile instanceof Target) {
                hitTarget();
            }
        }
    }

    private void reflect() {
        Log.d(TAG, "reflectedWith:" + mAngle + "°");
        Mirror mirror = (Mirror) mBlockingTile;
        Laser newSegment = new Laser(mBoard);
        mBoard.addNewLaserSegment(newSegment);
        newSegment.setOn(true);
        switch (mIntersectionDirection) {
            case FROM_LEFT:
                newSegment.setmSourceTile(this.mBlockingTile);
                newSegment.setAngle(this.mAngle);
                newSegment.setmRelativeAngle((this.mAngle + 270) * (-1));
                newSegment.setLine(this.mBlockingPoint.x, this.mBlockingPoint.y, mAreaDimension.left);
                break;
            //TODO add new cases
        }
    }

    private void hitTarget() {
        targetHit = true;
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

    private void setLine(float x1, float y1, float x2) {
        super.setLine(x1, y1, x2, getSlope() * (x2 - x1) + y1);
        if (this.y2 > mAreaDimension.bottom) {
            this.y2 = mAreaDimension.bottom;
            this.x2 = (this.y2 - this.y1) / getSlope() + this.x1;
        } else if (this.y2 < mAreaDimension.top) {
            this.y2 = mAreaDimension.top;
            this.x2 = (this.y2 - this.y1) / getSlope() + this.x1;
        } else if (this.x2 > mAreaDimension.right) {
            this.x2 = mAreaDimension.right;
            this.y2 = (this.x2 - this.x1) * getSlope() + this.y1;

        } else if (this.x2 < mAreaDimension.left) {
            this.x2 = mAreaDimension.left;
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
     * @return
     */
    private ArrayList<BoardObject> getTileListLaserIsOn() {
        ArrayList<BoardObject> laserIsOn = new ArrayList<>();
        if (y1 == mAreaDimension.top || y1 == mAreaDimension.bottom) {
            for (int i = 0; i < mArea.length; i++) {
                for (int j = 0; j < mArea[i].length; j++) {
                    if (intersects(mArea[i][j]))
                        laserIsOn.add(mArea[i][j]);
                }
            }
        } else if (x1 == mAreaDimension.left || x1 == mAreaDimension.right) {
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
     * @return
     */
    @Nullable
    private BoardObject getBlockingTile() {
        BoardObject blockingObject = null;
        if (x1 == mAreaDimension.left) {
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
        } else if (y1 == mAreaDimension.top) {
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
        } else if (x1 == mAreaDimension.right) {
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
        } else if (y1 == mAreaDimension.bottom) {
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
     * @return
     */
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

    /**
     * Uses blocking point algorithm for laser
     *
     * @return
     */
    private PointF getBlockingPoint() {
        PointF blockingPoint = null;

        if (mBlockingTile != null) {
            if (mIntersectionPoint.x == mBlockingTile.left) {
                blockingPoint = findBlockingPointInInterval(mBlockingTile.left, mBlockingTile.right, FROM_LEFT);
            } else if (mIntersectionPoint.x == mBlockingTile.right) {
                blockingPoint = findBlockingPointInInterval(mBlockingTile.right, mBlockingTile.left, FROM_RIGHT);
            } else if (mIntersectionPoint.y == mBlockingTile.top) {
                switch (getmDirection()) {
                    case DOWNWARDS_RIGHT:
                        blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mBlockingTile.right, FROM_TOP);
                        break;
                    case DOWNWARDS_LEFT:
                        blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mBlockingTile.left, FROM_TOP);
                        break;
                    case CONSTANT_DOWN:
                        blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mIntersectionPoint.x, FROM_TOP);
                        break;
                    default:
                        Log.d(TAG, "I made a terrible mistake!");
                }
            } else if (mIntersectionPoint.y == mBlockingTile.bottom) {
                switch (getmDirection()) {
                    case UPWARDS_RIGHT:
                        if (x1 == endX) {
                            blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mBlockingTile.left, FROM_BOTTOM);
                        } else if (y1 == endY) {
                            blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mBlockingTile.right, FROM_BOTTOM);
                        }
                        break;
                    case UPWARDS_LEFT:
                        if (x1 == startX) {
                            blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mBlockingTile.right, FROM_BOTTOM);
                        } else if (y1 == endY) {
                            blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mBlockingTile.left, FROM_BOTTOM);
                        }
                        break;
                    case CONSTANT_UP:
                        blockingPoint = findBlockingPointInInterval(mIntersectionPoint.x, mIntersectionPoint.x, FROM_BOTTOM);
                        break;
                    default:
                        Log.d(TAG, "I made a terrible mistake!");
                }
            }
        }

        return blockingPoint;
    }

    /** Returns true if given pixel in Bitmap is transparent else false
     *
     * @param bitmap
     * @param x
     * @param y
     * @return
     */
    private static boolean isTransparent(Bitmap bitmap, int x, int y) {
        int pixel = bitmap.getPixel(x, y);
        if ((pixel >> 24) == 0) {
            return true;
        }
        return false;
    }

    /**
     * Blocking point algorithm: searches a specific interval and returns first untransparent pixel/point
     *
     * @param x1
     * @param x2
     * @param d
     * @return
     */
    private PointF findBlockingPointInInterval(final float x1, final float x2, IntersectionDirection d) {
        PointF blockingPoint = null;
        Iterator<PointF> it = getPointsInInterval(x1, x2).iterator();
        while (it.hasNext()) {
            PointF p = it.next();
            if (mBlockingTile.contains(p.x, p.y)) {
                Bitmap bitmap = null;
                if (mBlockingTile instanceof Target) {
                    bitmap = Target.sTargetIcon;
                } else if (mBlockingTile instanceof Obstacle) {
                    bitmap = Obstacle.sObstacleIcon;
                } else if (mBlockingTile instanceof Mirror) {
                    bitmap = ((Mirror) mBlockingTile).rotateBitmap();
                }
                if (!isTransparent(bitmap, (int) (p.x - mBlockingTile.left), (int) (p.y - mBlockingTile.top))) {
                    blockingPoint = p;
                    mIntersectionDirection = d;
                    break;
                }
            }
        }
        return blockingPoint;
    }
}
