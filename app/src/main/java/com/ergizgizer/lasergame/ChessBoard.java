package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import static com.ergizgizer.lasergame.BoardModel.COLS;
import static com.ergizgizer.lasergame.BoardModel.ROWS;

public class ChessBoard extends View {

    private static final String TAG = ChessBoard.class.getSimpleName();

    interface BoardListener {
        void putMirror(int row, int col);

        void pickMirror(int row, int col);

        void requestForLaser(int row, int col);

        void setBoardDimension(float x1, float x2, float y1, float y2);

        void targetHit();
    }

    private Context mContext;
    private BoardModel mBoardModel;
    private BoardListener mController;
    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;
    private float mTileSize;
    private boolean mLaserWasOn;

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

    public void setmLaserWasOn(boolean wasOn) {
        this.mLaserWasOn = wasOn;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, MeasureSpec.toString(widthMeasureSpec));
        Log.d(TAG, MeasureSpec.toString(heightMeasureSpec));
        mTileSize = Math.min(getTileSizeWidth(), getTileSizeHeight());
        Log.d(TAG, "tileSize:" + Float.toString(mTileSize));
        float width = mTileSize * COLS;
        float height = mTileSize * ROWS;
        x1 = (getMeasuredWidth() - width) / 2;
        y1 = 0;
        x2 = x1 + mTileSize * COLS;
        y2 = y1 + mTileSize * ROWS;
        mController.setBoardDimension(x1, x2, y1, y2);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        mBoardModel.drawBoard(mContext, canvas, mTileSize, x1, y1);
        ArrayList<Laser> segments = mBoardModel.getmLaserSegments();
        Laser sourceSegment = segments.get(0);
        if (sourceSegment.isOn()) {
            sourceSegment.initLaser();
            for (Laser laser : segments) {
                if (laser != null && laser.isOn()) {
                    canvas.drawLine(laser.x1, laser.y1, laser.x2, laser.y2, laser.getmBeam());
                    if (laser.isTargetHit()) {
                        mController.targetHit();
                    }
                }
            }
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
                mController.requestForLaser(row, col);
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
