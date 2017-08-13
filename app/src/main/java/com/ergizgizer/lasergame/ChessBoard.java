package com.ergizgizer.lasergame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static com.ergizgizer.lasergame.BoardModel.COLS;
import static com.ergizgizer.lasergame.BoardModel.ROWS;

public class ChessBoard extends View {

    private static final String TAG = ChessBoard.class.getSimpleName();

    interface BoardListener {
        void putMirror(int row, int col);

        void pickMirror(int row, int col);

        void requestForLaser(int row, int col, int x0, int y0, int x1, int y1);
    }

    private Context mContext;
    private BoardModel mBoardModel;
    private BoardListener mController;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int mTileSize;

    public ChessBoard(Context context) {
        super(context);
        this.mContext = context;
        this.mController = (BoardListener) context;
    }

    public ChessBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mController = (BoardListener) context;
    }

    public void setmBoardModel(BoardModel model) {
        this.mBoardModel = model;
    }

    private void setMargins() {
        int measuredWidth = getMeasuredWidth();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, MeasureSpec.toString(widthMeasureSpec));
        Log.d(TAG, MeasureSpec.toString(heightMeasureSpec));
        mTileSize = Math.min(getTileSizeWidth(), getTileSizeHeight());
        Log.d(TAG, Integer.toString(mTileSize));
        int width = mTileSize * COLS;
        int height = mTileSize * ROWS;
        x1 = (getMeasuredWidth() - width) / 2;
        y1 = 0;
        x2 = x1 + mTileSize * COLS;
        y2 = y1 + mTileSize * ROWS;


    }

    @Override
    protected void onDraw(final Canvas canvas) {
        Log.d(TAG, Integer.toString(getWidth()));
        Log.d(TAG, Integer.toString(getHeight()));
        mBoardModel.drawBoard(mContext, canvas, mTileSize, x1, y1);
        Laser laser = mBoardModel.getmLaser();
        if (laser != null && laser.isOn())
            canvas.drawLine(laser.x1, laser.y1, laser.x2, laser.y2, laser.getmBeam());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        final int col = (x - x1) / mTileSize;
        final int row = (y - y1) / mTileSize;

        Level level = mBoardModel.getmLevel();
        char symbols[][] = level.getObjectLayer();

        if ((row >= 0 && col >= 0) && (row < ROWS && col < COLS)) {
            if (symbols[row][col] == 'B' && !level.isAllMirrorsDeployed()) {
                mController.putMirror(row, col);
            } else if (symbols[row][col] == 'M') {
                mController.pickMirror(row, col);
            } else if ((row == 0 || row == 9 || col == 0 || col == 9)
                    && level.isAllMirrorsDeployed()) {
                mController.requestForLaser(row, col, x1, y1, x2, y2);
            }
        }


        return super.onTouchEvent(event);
    }

    private int getTileSizeWidth() {
        return getMeasuredWidth() / COLS;
    }

    private int getTileSizeHeight() {
        return getMeasuredHeight() / ROWS;
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
