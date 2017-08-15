package com.ergizgizer.lasergame;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoardFragment extends Fragment {

    static final String TAG = BoardFragment.class.getSimpleName();

    private BoardModel mBoardModel;
    private ChessBoard mBoard;
    private boolean mLaserWasOn;

    public void setmBoard(BoardModel model) {
        this.mBoardModel = model;
    }

    public void setmLaserWasOn(boolean wasOn) {
        this.mLaserWasOn = wasOn;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        mBoard = (ChessBoard) view.findViewById(R.id.board);
        mBoard.setmBoardModel(mBoardModel);
        mBoard.setmLaserWasOn(mLaserWasOn);
        return view;
    }
}
