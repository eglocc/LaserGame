package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public abstract class BoardObject extends RectF {

	private static final String TAG = BoardObject.class.getSimpleName();

	private final int mRowIndex;
	private final int mColumnIndex;
	private final String mCode;

	private final Paint mBackgroundColor;
	private Bitmap mBitmap;

    public BoardObject(int rowIndex, int columnIndex) {
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

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public void setmBitmap(final Bitmap bitmap) {
		this.mBitmap = bitmap;
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
}
