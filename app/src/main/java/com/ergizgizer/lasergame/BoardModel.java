package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Arrays;
import java.util.Stack;

public class BoardModel {

    private static final String TAG = BoardModel.class.getSimpleName();

    static final int ROWS = 10;
    static final int COLS = 10;

    private Context mContext;
    private BoardObject[][] mTiles;
    private Level mLevel;
    private Laser mLaser;
    private Mirror[] mMirrors;
    private Stack<Integer> mMirrorBackStack;

    public BoardModel(Context context) {
        this.mContext = context;
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = new Level();
        initBoard();
        mLaser = new Laser(mTiles);
        mMirrors = new Mirror[mLevel.getNumberOfAllowedMirrors()];
        mMirrorBackStack = new Stack<>();
    }

    public BoardModel(Context context, Level level) {
        this.mContext = context;
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = level;
        initBoard();
        mLaser = new Laser(mTiles);
        mMirrors = new Mirror[mLevel.getNumberOfAllowedMirrors()];
        mMirrorBackStack = new Stack<>();
    }

    public BoardObject[][] getObjects() {
        return mTiles;
    }

    public void setObjects(BoardObject[][] tiles) {
        this.mTiles = tiles;
    }

    public Mirror[] getmMirrors() {
        return mMirrors;
    }

    public void setmMirrors(Mirror[] mirrors) {
        this.mMirrors = mirrors;
    }

    public final Level getmLevel() {
        return mLevel;
    }

    public void setmLevel(Level level) {
        this.mLevel = level;
    }

    public final Laser getmLaser() {
        return mLaser;
    }

    public void setmLaser(Laser laser) {
        this.mLaser = laser;
    }

    public void initBoard() {
        for (int i = 0; i < mTiles.length; i++) {
            for (int j = 0; j < mTiles[i].length; j++) {

                if (mLevel.getObjectLayer()[i][j] == 'O') {
                    mTiles[i][j] = new Obstacle(mContext, i, j);
                } else if (mLevel.getObjectLayer()[i][j] == 'T') {
                    mTiles[i][j] = new Target(mContext, i, j);
                } else {
                    mTiles[i][j] = new Air(mContext, i, j);
                }
            }
        }
    }

    public Mirror putMirror(int r, int c) {
        mLevel.putMirror(r, c);

        if (mMirrorBackStack.empty())
            mTiles[r][c] = new Mirror(mContext, r, c, mLevel.getNumberOfMirrors());
        else
            mTiles[r][c] = new Mirror(mContext, r, c, mMirrorBackStack.pop());

        Mirror mirror = (Mirror) mTiles[r][c];
        mMirrors[mirror.getmId() - 1] = mirror;


        Log.d(TAG, mMirrorBackStack.toString());
        Log.d(TAG, Arrays.toString(mMirrors));
        return (Mirror) mTiles[r][c];
    }

    public Mirror pickMirror(int r, int c) {
        mLevel.pickMirror(r, c);

        Mirror mirror = (Mirror) mTiles[r][c];
        int id = mirror.getmId();
        mMirrorBackStack.push(id);
        mMirrors[id - 1] = null;

        mTiles[r][c] = new Air(mContext, r, c);

        Log.d(TAG, mMirrorBackStack.toString());
        Log.d(TAG, Arrays.toString(mMirrors));
        return mirror;
    }

    public void drawBoard(Context context, Canvas canvas, float tileSize, float offsetX, float offsetY) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final float xCoord = getXCoord(offsetX, tileSize, col);
                final float yCoord = getYCoord(offsetY, tileSize, row);
                BoardObject obj = mTiles[row][col];
                obj.setEdges(xCoord, yCoord, xCoord + tileSize, yCoord + tileSize);
                obj.draw(context, canvas);
            }
        }
    }

    private float getXCoord(final float offsetX, final float tileSize, final int col) {
        return offsetX + tileSize * col;
    }

    private float getYCoord(final float offsetY, final float tileSize, final int row) {
        return offsetY + tileSize * row;
    }
}
