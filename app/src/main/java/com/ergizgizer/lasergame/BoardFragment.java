package com.ergizgizer.lasergame;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class BoardFragment extends Fragment {

    private static final String TAG = BoardFragment.class.getSimpleName();

    private Context mContext;
    private BoardModel mBlueprint;
    private ChessBoard mBoard;

    public void setmBoard(BoardModel blueprint) {
        this.mBlueprint = blueprint;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        RelativeLayout boardContainer = (RelativeLayout) view.findViewById(R.id.board_container);
        mBoard = new ChessBoard(mContext, mBlueprint);
        boardContainer.addView(mBoard);
        return view;
    }
}
