package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class BoardModel implements Parcelable {

    private static final String TAG = BoardModel.class.getSimpleName();

    static final int ROWS = 10;
    static final int COLS = 10;

    private RectF mBoardDimension;
    private BoardObject[][] mTiles;
    private Level mLevel;
    private ArrayList<Laser> mLaserSegments;
    private Mirror[] mMirrors;
    private Stack<Integer> mMirrorBackStack;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mLaserSegments.get(0), flags);
        dest.writeInt(mMirrors.length);
        dest.writeTypedArray(mMirrors, flags);
        dest.writeSerializable(mMirrorBackStack);
    }

    private BoardModel(Parcel source) {
        mLaserSegments = source.readArrayList(Laser.class.getClassLoader());
        mMirrors = new Mirror[source.readInt()];
        source.readTypedArray(mMirrors, Mirror.CREATOR);
        mMirrorBackStack = (Stack<Integer>) source.readSerializable();
        mTiles = new BoardObject[ROWS][COLS];
        mLevel = new Level();
        initBoard();
        int count = 0;
        for (int i = 0; i < mMirrors.length; i++) {
            if (mMirrors[i] != null) {
                int row = mMirrors[i].getmRowIndex();
                int col = mMirrors[i].getmColumnIndex();
                mTiles[row][col] = mMirrors[i];
                count++;
            }
        }
        mLevel.setNumberOfMirrors(count);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new BoardModel(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    public BoardModel() {
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = new Level();
        initBoard();
        mLaserSegments = new ArrayList<>();
        mLaserSegments.add(new Laser(this));
        mMirrors = new Mirror[mLevel.getNumberOfAllowedMirrors()];
        mMirrorBackStack = new Stack<>();
    }

    public BoardModel(Level level) {
        this.mTiles = new BoardObject[ROWS][COLS];
        this.mLevel = level;
        initBoard();
        mLaserSegments = new ArrayList<>();
        mLaserSegments.add(new Laser(this));
        mMirrors = new Mirror[mLevel.getNumberOfAllowedMirrors()];
        mMirrorBackStack = new Stack<>();
    }

    public RectF getmBoardDimension() {
        return mBoardDimension;
    }

    public void setmBoardDimension(RectF dimension) {
        this.mBoardDimension = dimension;
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

    public ArrayList<Laser> getmLaserSegments() {
        return mLaserSegments;
    }

    public void clearLaserSegments() {
        mLaserSegments.clear();
    }

    public final Laser getmLaserSegment(int index) {
        return mLaserSegments.get(index);
    }

    public void addNewLaserSegment(Laser laser) {
        mLaserSegments.add(laser);
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

    public Mirror putMirror(int r, int c) {
        mLevel.putMirror(r, c);

        if (mMirrorBackStack.empty())
            mTiles[r][c] = new Mirror(r, c, mLevel.getNumberOfMirrors());
        else
            mTiles[r][c] = new Mirror(r, c, mMirrorBackStack.pop());

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

        mTiles[r][c] = new Air(r, c);

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
