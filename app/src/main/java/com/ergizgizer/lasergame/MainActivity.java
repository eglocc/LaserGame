package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyStrings, ChessBoard.BoardListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SeekBar mLaserAngleSlider;

    private BoardModel mBoardModel;
    private BoardFragment mBoardFragment;
    private int mAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLaserAngleSlider = (SeekBar) findViewById(R.id.laser_angle_slider);

        mBoardModel = new BoardModel();

        mLaserAngleSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            TextView angleInDegrees = (TextView) findViewById(R.id.laser_angle_in_degrees);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mAngle = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                angleInDegrees.setText(Integer.toString(mAngle) + "°");
                mBoardModel.getmLaser().setmAngle(mAngle);
            }
        });

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
            laser.initLaser(x0, x1, y0, y1);
        } else {
            Laser newLaser = new Laser(mBoardModel.getObjects());
            newLaser.setmAngle(mAngle);
            mBoardModel.setmLaser(newLaser);
        }
        laser.setOn(!wasOn);
        updateBoard();
    }
}
