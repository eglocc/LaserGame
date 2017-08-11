package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import static com.ergizgizer.lasergame.BoardModel.COLS;
import static com.ergizgizer.lasergame.BoardModel.ROWS;

public class ChessBoard extends View {

    interface BoardListener {
        void tileClicked(int row, int col);
    }

    private Context mContext;
    private BoardModel mBluePrint;
    private BoardListener mListener;
    private int x0 = 0;
    private int y0 = 0;
    private int mTileSize;

    public ChessBoard(Context context, BoardModel blueprint) {
        super(context);
        this.mContext = context;
        this.mListener = (BoardListener) context;
        this.mBluePrint = blueprint;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final int width = getWidth();
        final int height = getHeight();
        mTileSize = Math.min(getTileSizeWidth(width), getTileSizeHeight(height));

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final int xCoord = getXCoord(col);
                final int yCoord = getYCoord(row);
                final Rect tileRect = new Rect(xCoord, yCoord, xCoord + mTileSize, yCoord + mTileSize);
                BoardObject obj = mBluePrint.getObjects()[row][col];
                obj.setmRect(tileRect);
                obj.draw(mContext, canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        final int col = x / mTileSize;
        final int row = y / mTileSize;

        mListener.tileClicked(row, col);

        return super.onTouchEvent(event);
    }

    private int getTileSizeWidth(int width) {
        return width / COLS;
    }

    private int getTileSizeHeight(int height) {
        return height / ROWS;
    }

    private int getXCoord(final int col) {
        return x0 + mTileSize * col;
    }

    private int getYCoord(final int row) {
        return y0 + mTileSize * row;
    }


}
