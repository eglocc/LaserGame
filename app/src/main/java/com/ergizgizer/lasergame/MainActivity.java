package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MyStrings, ChessBoard.BoardListener, AngleListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BoardModel mBoardModel;
    private BoardFragment mBoardFragment;
    private LaserAngleFragment mLaserAngleFragment;
    private int mCurrentLaserAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardModel = new BoardModel(this);
        mLaserAngleFragment = (LaserAngleFragment) getFragmentManager().findFragmentById(R.id.laser_angle_fragment);
        mLaserAngleFragment.setmLaser(mBoardModel.getmLaser());

        updateBoard();

        Bitmap planet = BitmapFactory.decodeResource(getResources(), R.drawable.p56);
        Bitmap target = BitmapFactory.decodeResource(getResources(), R.drawable.t56);
        Bitmap mirror = BitmapFactory.decodeResource(getResources(), R.drawable.s56);

    }

    private void updateBoard() {
        mBoardFragment = new BoardFragment();
        mBoardFragment.setmBoard(mBoardModel);
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.board_container, mBoardFragment);
        ft.commit();
    }

    private void putMirrorPanel(Mirror[] mirrors) {
        MirrorAngleFragment mirrorAngleFragment = new MirrorAngleFragment();
        mirrorAngleFragment.setmMirror(mirrors);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mirror_panel_container, mirrorAngleFragment, MirrorAngleFragment.TAG);
        ft.commit();
    }

    private void removeMirrorPanel() {
        MirrorAngleFragment remove = (MirrorAngleFragment) getFragmentManager().findFragmentByTag(MirrorAngleFragment.TAG);
        if (remove != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction().remove(remove);
            ft.commit();
        }
    }

    @Override
    public void putMirror(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        mBoardModel.putMirror(row, col);
        updateBoard();
        if (mBoardModel.getmLevel().isAllMirrorsDeployed()) {
            Mirror[] mirrors = mBoardModel.getmMirrors();
            putMirrorPanel(mirrors);
        }
    }

    @Override
    public void pickMirror(int row, int col) {
        Log.d(TAG, sSquareClicked + "[" + sRow + row + "," + sColumn + col + "]");
        Mirror mirror = mBoardModel.pickMirror(row, col);
        updateBoard();
        removeMirrorPanel();
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

    @Override
    public void mirrorAngleChanged(int id, int angle) {
        Mirror mirror = mBoardModel.getmMirrors()[id];
        mirror.setmAngle(angle);
        updateBoard();
    }
}
