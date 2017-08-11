package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public abstract class BoardObject {

	private static final String TAG = BoardObject.class.getSimpleName();

	private final int mRowIndex;
	private final int mColumnIndex;
	private final String mCode;

	private final Paint mBackgroundColor;
	private Rect mRect;
	private Bitmap mBitmap;

    public BoardObject(int rowIndex, int columnIndex) {
		this.mRowIndex = rowIndex;
		this.mColumnIndex = columnIndex;
		this.mCode = ("" + (char) ('A' + mColumnIndex) + (mRowIndex + 1));

		this.mBackgroundColor = new Paint();
		mBackgroundColor.setColor(isDark() ? Color.BLACK : Color.WHITE);
	}

	public String getmCode() {
		return mCode;
	}

	public boolean isDark() {
		return (mRowIndex + mColumnIndex) % 2 == 0;
	}

	public Rect getmRect() {
		return mRect;
	}

	public void setmRect(final Rect rect) {
		this.mRect = rect;
	}

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public void setmBitmap(final Bitmap bitmap) {
		this.mBitmap = bitmap;
	}

	public void draw(Context context, final Canvas canvas) {
		canvas.drawRect(mRect, mBackgroundColor);
	}

	public boolean isTouched(int x, int y) {
		return mRect.contains(x, y);
	}

	public void handleTouch() {
		Log.d(TAG, "clicked:" + toString());
	}

	@Override
	public String toString() {
		return String.format("<%s : %d,%d>", getClass().getSimpleName(), mRowIndex, mColumnIndex);
	}
}
