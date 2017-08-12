package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Canvas;

public class BoardModel {

    private static final String TAG = BoardModel.class.getSimpleName();

    static final int ROWS = 10;
    static final int COLS = 10;

    private BoardObject[][] mTiles;
    private Level mLevel;
    private Laser mLaser;

    public BoardModel() {
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = new Level();
        initBoard();
        mLaser = new Laser();
    }

    public BoardModel(Level level) {
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = level;
        initBoard();
        mLaser = new Laser();
    }

    public BoardObject[][] getObjects() {
        return mTiles;
    }

    public void setObjects(BoardObject[][] tiles) {
        this.mTiles = tiles;
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
                    mTiles[i][j] = new Obstacle(i, j);
                } else if (mLevel.getObjectLayer()[i][j] == 'T') {
                    mTiles[i][j] = new Target(i, j);
                } else {
                    mTiles[i][j] = new Air(i, j);
                }
            }
        }
    }

    public void putMirror(int r, int c) {
        mLevel.putMirror(r, c);
        mTiles[r][c] = new Mirror(r, c);
    }

    public void pickMirror(int r, int c) {
        mLevel.pickMirror(r, c);
        mTiles[r][c] = new Air(r, c);
    }

    public void drawBoard(Context context, Canvas canvas, int tileSize, int offsetX, int offsetY) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final int xCoord = getXCoord(offsetX, col, tileSize);
                final int yCoord = getYCoord(offsetY, row, tileSize);
                BoardObject obj = mTiles[row][col];
                obj.setEdges(xCoord, yCoord, xCoord + tileSize, yCoord + tileSize);
                obj.draw(context, canvas);
            }
        }
    }

    private int getXCoord(final int offsetX, final int tileSize, final int col) {
        return offsetX + tileSize * col;
    }

    private int getYCoord(final int offsetY, final int tileSize, final int row) {
        return offsetY + tileSize * row;
    }
}
