package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MyStrings, ChessBoard.BoardListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BoardModel mBoardModel;
    private BoardFragment mBoardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardModel = new BoardModel();
        updateBoard();

    }

    private void updateBoard() {
        mBoardFragment = new BoardFragment();
        mBoardFragment.setmBoard(mBoardModel);
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.board_container, mBoardFragment);
        ft.commit();
    }

    @Override
    public void tileClicked(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        updateBoard();
    }
}
