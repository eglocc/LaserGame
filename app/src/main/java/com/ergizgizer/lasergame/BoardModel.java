package com.ergizgizer.lasergame;

public class BoardModel {

    private static final String TAG = BoardModel.class.getSimpleName();

    static final int ROWS = 10;
    static final int COLS = 10;

    private BoardObject[][] mTiles;
    private Level mLevel;

    public BoardModel() {
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = new Level();
        initBoard();
    }

    public BoardModel(Level level) {
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = level;
        initBoard();
    }

    public BoardObject[][] getObjects() {
        return mTiles;
    }

    public void setObjects(BoardObject[][] tiles) {
        this.mTiles = tiles;
    }

    public final Level getLevel() {
        return mLevel;
    }

    public void setLevel(Level level) {
        this.mLevel = level;
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
}
