package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static com.ergizgizer.lasergame.BoardModel.COLS;
import static com.ergizgizer.lasergame.BoardModel.ROWS;

public class ChessBoard extends View {

    private static final String TAG = ChessBoard.class.getSimpleName();

    interface BoardListener {
        void tileClicked(int row, int col);
    }

    private Context mContext;
    private BoardModel mBoardModel;
    private BoardListener mListener;
    private int x0 = 0;
    private int y0 = 0;
    private int mTileSize;

    public ChessBoard(Context context) {
        super(context);
        this.mContext = context;
        this.mListener = (BoardListener) context;
    }

    public ChessBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mListener = (BoardListener) context;
    }

    public void setmBoardModel(BoardModel model) {
        this.mBoardModel = model;
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
                BoardObject obj = mBoardModel.getObjects()[row][col];
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

        Level level = mBoardModel.getLevel();
        char symbols[][] = level.getObjectLayer();
        if (symbols[row][col] == 'B' && level.getNumberOfMirrors() < level.getNumberOfAllowedMirrors()) {
            mBoardModel.putMirror(row, col);
        } else if (symbols[row][col] == 'M')
            mBoardModel.pickMirror(row, col);
        if ((row == 0 || row == 9 || col == 0 || col == 9)
                && level.getNumberOfMirrors() == level.getNumberOfAllowedMirrors()) {
            Laser laser = mBoardModel.getLaser();
            boolean on = laser.isOn();
            laser.setOn(!on);
            laser.setStart(mBoardModel.getObjects()[row][col]);
        }

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
