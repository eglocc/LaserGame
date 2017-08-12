package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyStrings, ChessBoard.BoardListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AngleSlider mLaserAngleListener;

    private SeekBar mLaserAngleSeekBar;
    private TextView angleInDegrees;

    private BoardModel mBoardModel;
    private BoardFragment mBoardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLaserAngleSeekBar = (SeekBar) findViewById(R.id.laser_angle_slider);
        angleInDegrees = (TextView) findViewById(R.id.laser_angle_in_degrees);

        mBoardModel = new BoardModel();
        mLaserAngleListener = new AngleSlider(mBoardModel.getmLaser(), angleInDegrees);
        mLaserAngleSeekBar.setOnSeekBarChangeListener(mLaserAngleListener);

        updateBoard();

    }

    private void updateBoard() {
        mBoardFragment = new BoardFragment();
        mBoardFragment.setmBoard(mBoardModel);
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.board_container, mBoardFragment);
        ft.commit();
    }

    @Override
    public void putMirror(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        mBoardModel.putMirror(row, col);
        updateBoard();
    }

    @Override
    public void pickMirror(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        mBoardModel.pickMirror(row, col);
        updateBoard();
    }

    @Override
    public void requestForLaser(int row, int col, int x0, int y0, int x1, int y1) {
        Log.d(TAG, sLaserRequested + "[" + sRow + row + "," + sColumn + col + "]");
        Laser laser = mBoardModel.getmLaser();
        boolean wasOn = laser.isOn();
        if (!wasOn) {
            laser.setmSourceTile(row, col);
            laser.setmAngle(mLaserAngleListener.getmAngle());
            laser.initLaser(x0, x1, y0, y1);
        } else {
            Laser newLaser = new Laser(mBoardModel.getObjects());
            newLaser.setmAngle(mLaserAngleListener.getmAngle());
            mBoardModel.setmLaser(newLaser);
        }
        laser.setOn(!wasOn);
        updateBoard();
    }
}
