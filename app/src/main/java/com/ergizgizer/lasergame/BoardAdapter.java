package com.ergizgizer.lasergame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

public class BoardAdapter extends BaseAdapter {

    private Context mContext;
    private Board mBoard;
    private BoardFragment.BoardListener mListener;
    private BoardObject[][] mBoardObjects;

    public BoardAdapter(Context c, Board board, BoardFragment.BoardListener boardListener) {
        this.mContext = c;
        this.mBoard = board;
        this.mListener = boardListener;
        this.mBoardObjects = board.getObjects();
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
        int colorIndex = boardObject.getBackgroundColorIndex();

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
                mListener.squareClicked(row, column);
            }
        });

        return convertView;
    }
}
