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

    static interface BoardListener {
        void squareClicked(int row, int column);
    }

    private Context mContext;
    private Board mBoard;
    private GridView mGridView;
    private BoardListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        this.mListener = (BoardListener) mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoard = new Board();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        mGridView = (GridView) view.findViewById(R.id.board);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGridView.setAdapter(new BoardAdapter(mContext, mBoard, mListener));
    }
}
