package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public abstract class BoardObject extends RectF implements MyStaticVariables {

	private static final String TAG = BoardObject.class.getSimpleName();

	private Context mContext;
	private final int mRowIndex;
	private final int mColumnIndex;
	private final String mCode;

	private Bitmap mIcon;
	private final Paint mBackgroundColor;

	public BoardObject(Context context, int rowIndex, int columnIndex) {
		this.mContext = context;
		this.mRowIndex = rowIndex;
		this.mColumnIndex = columnIndex;
		this.mCode = ("" + (char) ('A' + mColumnIndex) + (mRowIndex + 1));

		this.mBackgroundColor = new Paint();
		mBackgroundColor.setColor(isDark() ? Color.BLACK : Color.WHITE);
	}

	public int getmRowIndex() {
		return mRowIndex;
	}

	public int getmColumnIndex() {
		return mColumnIndex;
	}

	public String getmCode() {
		return mCode;
	}

	public boolean isDark() {
		return (mRowIndex + mColumnIndex) % 2 == 0;
	}

	public void setEdges(float left, float top, float right, float bottom) {
		set(left, top, right, bottom);
	}

	public float getWidth() {
		return right - left;
	}

	public float getHeight() {
		return bottom - top;
	}

	public Bitmap getmIcon() {
		return mIcon;
	}

	public void setmIcon(Bitmap mIcon) {
		this.mIcon = mIcon;
	}

	public void draw(Context context, final Canvas canvas) {
		canvas.drawRect(this, mBackgroundColor);
	}

	public boolean isTouched(int x, int y) {
		return this.contains(x, y);
	}

	public void handleTouch() {
		Log.d(TAG, "clicked:" + toString());
	}

	@Override
	public String toString() {
		return String.format("<%s : %d,%d>", getClass().getSimpleName(), mRowIndex, mColumnIndex);
	}

	/**
	 * Tests if the specified line segment intersects the interior of this
	 * <code>Rectangle2D</code>.
	 *
	 * @param x1 the X coordinate of the start point of the specified
	 *           line segment
	 * @param y1 the Y coordinate of the start point of the specified
	 *           line segment
	 * @param x2 the X coordinate of the end point of the specified
	 *           line segment
	 * @param y2 the Y coordinate of the end point of the specified
	 *           line segment
	 * @return <code>true</code> if the specified line segment intersects
	 * the interior of this <code>Rectangle2D</code>; <code>false</code>
	 * otherwise.
	 * @since 1.2
	 */
	public boolean intersectsLine(float x1, float y1, float x2, float y2) {
		int out1, out2;
		if ((out2 = outcode(x2, y2)) == 0) {
			return true;
		}
		while ((out1 = outcode(x1, y1)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				float x = left;
				if ((out1 & OUT_RIGHT) != 0) {
					x += getWidth();
				}
				y1 = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
				x1 = x;
			} else {
				float y = top;
				if ((out1 & OUT_BOTTOM) != 0) {
					y += getHeight();
				}
				x1 = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
				y1 = y;
			}
		}
		return true;
	}

	public int outcode(float x, float y) {
			/*
             * Note on casts to double below.  If the arithmetic of
             * x+w or y+h is done in float, then some bits may be
             * lost if the binary exponents of x/y and w/h are not
             * similar.  By converting to double before the addition
             * we force the addition to be carried out in double to
             * avoid rounding error in the comparison.
             *
             * See bug 4320890 for problems that this inaccuracy causes.
             */
		int out = 0;
		if (getWidth() <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (x < left) {
			out |= OUT_LEFT;
		} else if (x > left + getWidth()) {
			out |= OUT_RIGHT;
		}
		if (getHeight() <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (y < top) {
			out |= OUT_TOP;
		} else if (y > top + getHeight()) {
			out |= OUT_BOTTOM;
		}
		return out;
	}
}
