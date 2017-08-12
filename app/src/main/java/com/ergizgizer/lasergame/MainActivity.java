package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MyStrings, ChessBoard.BoardListener, LaserAngleFragment.LaserAngleListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BoardModel mBoardModel;
    private BoardFragment mBoardFragment;
    private LaserAngleFragment mLaserAngleFragment;
    private int mCurrentLaserAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardModel = new BoardModel();
        mLaserAngleFragment = (LaserAngleFragment) getFragmentManager().findFragmentById(R.id.laser_angle_fragment);
        mLaserAngleFragment.setmLaser(mBoardModel.getmLaser());

        updateBoard();

    }

    private void updateBoard() {
        mBoardFragment = new BoardFragment();
        mBoardFragment.setmBoard(mBoardModel);
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.board_container, mBoardFragment);
        ft.commit();
    }

    private void putMirrorPanel(Mirror mirror) {
        MirrorAngleFragment mirrorAngleFragment = new MirrorAngleFragment();
        mirrorAngleFragment.setmMirror(mirror);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (mirror.getmId()) {
            case 1:
                ft.replace(R.id.mirror_1_slider, mirrorAngleFragment);
                break;
            case 2:
                ft.replace(R.id.mirror_2_slider, mirrorAngleFragment);
                break;
            case 3:
                ft.replace(R.id.mirror_3_slider, mirrorAngleFragment);
                break;
            default:
                Log.d(TAG, "Ooops! I think something's wrong!");
        }
        ft.commit();
    }

    private void removeMirrorPanel(Mirror mirror) {
        View mirrorPanel = null;
        switch (mirror.getmId()) {
            case 1:
                mirrorPanel = findViewById(R.id.mirror_1_slider);
                break;
            case 2:
                mirrorPanel = findViewById(R.id.mirror_2_slider);
                break;
            case 3:
                mirrorPanel = findViewById(R.id.mirror_3_slider);
                break;
            default:
                Log.d(TAG, "Ooops! I think something's wrong!");
        }

        if (mirrorPanel != null)
            mirrorPanel.setVisibility(View.GONE);
    }

    @Override
    public void putMirror(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        Mirror mirror = mBoardModel.putMirror(row, col);
        updateBoard();
        //putMirrorPanel(mirror);
    }

    @Override
    public void pickMirror(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        Mirror mirror = mBoardModel.pickMirror(row, col);
        updateBoard();
        //removeMirrorPanel();
    }

    @Override
    public void requestForLaser(int row, int col, int x0, int y0, int x1, int y1) {
        Log.d(TAG, sLaserRequested + "[" + sRow + row + "," + sColumn + col + "]");
        Laser laser = mBoardModel.getmLaser();
        boolean wasOn = laser.isOn();
        if (!wasOn) {
            laser.setmSourceTile(row, col);
            laser.setmAngle(mCurrentLaserAngle);
            laser.initLaser(x0, x1, y0, y1);
        } else {
            Laser newLaser = new Laser(mBoardModel.getObjects());
            newLaser.setmAngle(mCurrentLaserAngle);
            mBoardModel.setmLaser(newLaser);
        }
        laser.setOn(!wasOn);
        updateBoard();
    }

    @Override
    public void laserAngleChanged(int angle) {
        mCurrentLaserAngle = angle;
    }
}
