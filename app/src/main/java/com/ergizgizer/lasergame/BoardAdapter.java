package com.ergizgizer.lasergame;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

public class BoardAdapter extends BaseAdapter {

    private static final String TAG = "BoardAdapter";

    private Context mContext;
    private BoardFragment.BoardListener mListener;
    private Board mBoard;
    private BoardObject[][] mBoardObjects;
    private Level mLevel;

    public BoardAdapter(Context c, Board board, BoardFragment.BoardListener boardListener) {
        this.mContext = c;
        this.mListener = boardListener;
        this.mBoard = board;
        this.mBoardObjects = board.getObjects();
        this.mLevel = board.getLevel();
    }

    @Override
    public int getCount() {
        return Board.SIZE.getWidth() * Board.SIZE.getHeight();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.board_object, null);
        }

        final ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.square);

        final int column = position % Board.SIZE.getWidth();
        final int row = position / Board.SIZE.getHeight();
        BoardObject boardObject = mBoardObjects[row][column];
        final int colorIndex = boardObject.getBackgroundColorIndex();

        switch (colorIndex) {
            case 0:
                imageButton.setBackgroundColor(mContext.getColor(R.color.black));
                break;
            case 1:
                imageButton.setBackgroundColor(mContext.getColor(R.color.white));
                break;
        }

        if (boardObject instanceof Obstacle) {
            imageButton.setBackground(mContext.getDrawable(R.drawable.obstacle));
        }
        if (boardObject instanceof Target) {
            imageButton.setBackground(mContext.getDrawable(R.drawable.target));
        }
        if (boardObject instanceof Mirror) {
            imageButton.setBackground(mContext.getDrawable(R.drawable.satellite0));
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLevel.getObjectLayer()[row][column] == 'B' && mLevel.getNumberOfMirrors() < mLevel.getNumberOfAllowedMirrors()) {
                    //mLevel.printLevel(); for debugging
                    mBoard.putMirror(row, column);
                    mLevel.putMirror(row, column);
                    Log.d(TAG, Integer.toString(mLevel.getNumberOfMirrors()));
                    //mLevel.printLevel(); for debugging
                } else if (mLevel.getObjectLayer()[row][column] == 'M' && mLevel.getNumberOfMirrors() > 0) {
                    //mLevel.printLevel(); for debugging
                    mBoard.pickMirror(row, column);
                    mLevel.pickMirror(row, column);
                    Log.d(TAG, Integer.toString(mLevel.getNumberOfMirrors()));
                    //mLevel.printLevel(); for debugging
                }
                mListener.squareClicked(row, column);
            }
        });

        return convertView;
    }
}
