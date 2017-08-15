package com.ergizgizer.lasergame;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;

import java.util.ArrayList;

public class Line {

    protected float x1;
    protected float y1;
    protected float x2;
    protected float y2;


    enum Direction {
        UPWARDS_LEFT, UPWARDS_RIGHT, DOWNWARDS_LEFT, DOWNWARDS_RIGHT, CONSTANT_LEFT,
        CONSTANT_RIGHT, CONSTANT_UP, CONSTANT_DOWN, POINT
    }

    private ArrayList<PointF> mPoints;
    private Direction mDirection;

    public Line() {
    }

    Line(Parcel source) {

    }

    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public void setLine(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Direction getmDirection() {
        return mDirection;
    }

    public void setmDirection(RectF boardDimension) {
        if (y1 > y2) {
            if (x1 == boardDimension.left)
                mDirection = Direction.UPWARDS_LEFT;
            else if (x1 == boardDimension.right)
                mDirection = Direction.UPWARDS_RIGHT;
            else {
                if (x1 > x2)
                    mDirection = Direction.UPWARDS_LEFT;
                else if (x1 < x2)
                    mDirection = Direction.UPWARDS_RIGHT;
                else
                    mDirection = Direction.CONSTANT_UP;
            }
        } else if (y1 < y2) {
            if (x1 > x2)
                mDirection = Direction.DOWNWARDS_LEFT;
            else if (x1 < x2)
                mDirection = Direction.DOWNWARDS_RIGHT;
            else
                mDirection = Direction.CONSTANT_DOWN;
        } else {
            if (x1 > x2)
                mDirection = Direction.CONSTANT_LEFT;
            else if (x1 < x2)
                mDirection = Direction.CONSTANT_RIGHT;
            else
                mDirection = Direction.POINT;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public boolean intersects(BoardObject r) {
        return r.intersectsLine(x1, y1, x2, y2);
    }

    public static int relativeCCW(double x1, double y1,
                                  double x2, double y2,
                                  double px, double py) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        double ccw = px * y2 - py * x2;
        if (ccw == 0.0) {
            // The point is colinear, classify based on which side of
            // the segment the point falls on.  We can calculate a
            // relative value using the projection of px,py onto the
            // segment - a negative value indicates the point projects
            // outside of the segment in the direction of the particular
            // endpoint used as the origin for the projection.
            ccw = px * x2 + py * y2;
            if (ccw > 0.0) {
                // Reverse the projection to be relative to the original x2,y2
                // x2 and y2 are simply negated.
                // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                //    from them (based on the original values)
                // Since we really want to get a positive answer when the
                //    point is "beyond (x2,y2)", then we want to calculate
                //    the inverse anyway - thus we leave x2 & y2 negated.
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if (ccw < 0.0) {
                    ccw = 0.0;
                }
            }
        }
        return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
    }

    /**
     * Returns an indicator of where the specified point
     * {@code (px,py)} lies with respect to this line segment.
     * See the method comments of
     * {@link #relativeCCW(double, double, double, double, double, double)}
     * to interpret the return value.
     *
     * @param px the X coordinate of the specified point
     *           to be compared with this <code>Line2D</code>
     * @param py the Y coordinate of the specified point
     *           to be compared with this <code>Line2D</code>
     * @return an integer that indicates the position of the specified
     * coordinates with respect to this <code>Line2D</code>
     * @see #relativeCCW(double, double, double, double, double, double)
     * @since 1.2
     */
    public int relativeCCW(double px, double py) {
        return relativeCCW(x1, y1, x2, y2, px, py);
    }

    /**
     * Returns an indicator of where the specified <code>Point2D</code>
     * lies with respect to this line segment.
     * See the method comments of
     * {@link #relativeCCW(double, double, double, double, double, double)}
     * to interpret the return value.
     *
     * @param p the specified <code>Point2D</code> to be compared
     *          with this <code>Line2D</code>
     * @return an integer that indicates the position of the specified
     * <code>Point2D</code> with respect to this <code>Line2D</code>
     * @see #relativeCCW(double, double, double, double, double, double)
     * @since 1.2
     */
    public int relativeCCW(PointF p) {
        return relativeCCW(x1, y1, x2, y2,
                p.x, p.y);
    }

    /**
     * Tests if the line segment from {@code (x1,y1)} to
     * {@code (x2,y2)} intersects the line segment from {@code (x3,y3)}
     * to {@code (x4,y4)}.
     *
     * @param x1 the X coordinate of the start point of the first
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the first
     *           specified line segment
     * @param x2 the X coordinate of the end point of the first
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the first
     *           specified line segment
     * @param x3 the X coordinate of the start point of the second
     *           specified line segment
     * @param y3 the Y coordinate of the start point of the second
     *           specified line segment
     * @param x4 the X coordinate of the end point of the second
     *           specified line segment
     * @param y4 the Y coordinate of the end point of the second
     *           specified line segment
     * @return <code>true</code> if the first specified line segment
     * and the second specified line segment intersect
     * each other; <code>false</code> otherwise.
     * @since 1.2
     */
    public static boolean linesIntersect(double x1, double y1,
                                         double x2, double y2,
                                         double x3, double y3,
                                         double x4, double y4) {
        return ((relativeCCW(x1, y1, x2, y2, x3, y3) *
                relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
                && (relativeCCW(x3, y3, x4, y4, x1, y1) *
                relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
    }

    /**
     * Tests if the specified line segment intersects this line segment.
     *
     * @param l the specified <code>Line2D</code>
     * @return <code>true</code> if this line segment and the specified line
     * segment intersect each other;
     * <code>false</code> otherwise.
     * @since 1.2
     */
    public boolean intersectsLine(Line l) {
        return linesIntersect(l.x1, l.y1, l.x2, l.y2,
                x1, y1, x2, y2);
    }
}
