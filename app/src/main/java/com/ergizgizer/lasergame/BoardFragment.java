package com.ergizgizer.lasergame;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class BoardFragment extends Fragment {

    private static final String TAG = "BoardFragment";

    interface BoardListener {
        void squareClicked(int row, int column);
    }

    private Context mContext;
    private Board mBoard;
    private GridView mGridView;
    private BoardListener mListener;

    public void setmBoard(Board board) {
        this.mBoard = board;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        this.mListener = (BoardListener) mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        mGridView = (GridView) view.findViewById(R.id.board);
        mGridView.setAdapter(new BoardAdapter(mContext, mBoard, mListener));
        return view;
    }
}
