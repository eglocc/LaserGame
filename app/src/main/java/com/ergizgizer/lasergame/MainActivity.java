package com.ergizgizer.lasergame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity implements MyStrings, BoardFragment.BoardListener {

    private Board mBoard;
    private GridView mBoardView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBoard = new Board();
    }

    @Override
    public void squareClicked(int row, int column) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + column + "]");
    }
}
