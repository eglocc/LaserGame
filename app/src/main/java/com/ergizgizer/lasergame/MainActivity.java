package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MyStrings, BoardFragment.BoardListener {

    private static final String TAG = "MainActivity";

    private Board mBoard;
    private BoardFragment mBoardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            mBoard = new Board();
        updateBoard();

    }

    @Override
    public void squareClicked(int row, int column) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + column + "]");
        updateBoard();
    }

    private void updateBoard() {
        mBoardFragment = new BoardFragment();
        mBoardFragment.setmBoard(mBoard);
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.board_container, mBoardFragment);
        ft.commit();
    }
}
