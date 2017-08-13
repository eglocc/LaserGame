package com.ergizgizer.lasergame;

import android.content.Context;
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

        void requestForLaser(int row, int col, float x0, float y0, float x1, float y1);
    }

    private Context mContext;
    private BoardModel mBoardModel;
    private BoardListener mController;
    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;
    private float mTileSize;

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
        Log.d(TAG, Float.toString(mTileSize));
        float width = mTileSize * COLS;
        float height = mTileSize * ROWS;
        x1 = (getMeasuredWidth() - width) / 2;
        y1 = 0;
        x2 = x1 + mTileSize * COLS;
        y2 = y1 + mTileSize * ROWS;
        Log.d(TAG, "onMeasure:boardstartX:" + x1);
        Log.d(TAG, "onMeasure:boardstartY:" + y1);
        Log.d(TAG, "onMeasure:boardendX:" + x2);
        Log.d(TAG, "onMeasure:boardendY:" + y2);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        //Log.d(TAG, Integer.toString(getWidth()));
        //Log.d(TAG, Integer.toString(getHeight()));
        mBoardModel.drawBoard(mContext, canvas, mTileSize, x1, y1);
        Laser laser = mBoardModel.getmLaser();
        if (laser != null && laser.isOn()) {
            Log.d(TAG, "onDraw:boardstartX:" + x1);
            Log.d(TAG, "onDraw:boardstartY:" + y1);
            Log.d(TAG, "onDraw:boardendX:" + x2);
            Log.d(TAG, "onDraw:boardendY:" + y2);
            canvas.drawLine(laser.x1, laser.y1, laser.x2, laser.y2, laser.getmBeam());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        if ((x > x1 && x < x2) && (y > y1 && y < y2)) {
            final int col = (int) ((x - x1) / mTileSize);
            final int row = (int) ((y - y1) / mTileSize);

            Level level = mBoardModel.getmLevel();
            char symbols[][] = level.getObjectLayer();

            if (symbols[row][col] == 'B' && !level.isAllMirrorsDeployed()) {
                mController.putMirror(row, col);
            } else if (symbols[row][col] == 'M') {
                mController.pickMirror(row, col);
            } else if ((row == 0 || row == 9 || col == 0 || col == 9)
                    && level.isAllMirrorsDeployed()) {
                Log.d(TAG, "onTouchEvent:boardstartX:" + x1);
                Log.d(TAG, "onTouchEvent:boardstartY:" + y1);
                Log.d(TAG, "onTouchEvent:boardendX:" + x2);
                Log.d(TAG, "onTouchEvent:boardendY:" + y2);
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

}
