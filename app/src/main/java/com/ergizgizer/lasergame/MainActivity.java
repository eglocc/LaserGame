package com.ergizgizer.lasergame;

import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyStaticVariables, ChessBoard.BoardListener, AngleListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BoardModel mBoardModel;
    private BoardFragment mBoardFragment;
    private LaserAngleFragment mLaserAngleFragment;
    private int mCurrentLaserAngle;
    private RectF mCurrentBoardDimension;
    private boolean mTargetHit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Target.sTargetIcon = BitmapFactory.decodeResource(getResources(), R.drawable.t56);
        Obstacle.sObstacleIcon = BitmapFactory.decodeResource(getResources(), R.drawable.p56);
        Mirror.sUnrotatedMirrorIcon = BitmapFactory.decodeResource(getResources(), R.drawable.s56);


        if (savedInstanceState == null) {
            mBoardModel = new BoardModel();
            mCurrentLaserAngle = sDefaultLaserAngle;
        }

        mLaserAngleFragment = (LaserAngleFragment) getFragmentManager().findFragmentById(R.id.laser_angle_fragment);

        if (savedInstanceState != null) {
            mBoardModel = savedInstanceState.getParcelable(sBoardModelKey);
            mCurrentLaserAngle = savedInstanceState.getInt(sLaserAngleKey);
        }

        Laser sourceSegment = mBoardModel.getmLaserSegment(0);
        mLaserAngleFragment.setmLaser(sourceSegment);

        updateBoard();
    }

    private void updateBoard() {
        mBoardFragment = new BoardFragment();
        mBoardFragment.setmBoard(mBoardModel);
        mBoardFragment.setmLaserWasOn(mBoardModel.getmLaserSegment(0).isOn());
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.board_container, mBoardFragment, BoardFragment.TAG);
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
    public void requestForLaser(int row, int col) {
        Log.d(TAG, sLaserRequested + "[" + sRow + row + "," + sColumn + col + "]");
        ArrayList<Laser> segments = mBoardModel.getmLaserSegments();
        Laser sourceSegment = segments.get(0);
        //sourceSegment.setmAreaDimension(mCurrentBoardDimension);
        boolean wasOn = sourceSegment.isOn();
        if (!wasOn) {
            sourceSegment.setmSourceTile(row, col);
            sourceSegment.setAngle(mCurrentLaserAngle);
            //sourceSegment.initLaser();
        } else {
            Laser newLaser = new Laser(mBoardModel);
            newLaser.setAngle(mCurrentLaserAngle);
            mBoardModel.clearLaserSegments();
            mBoardModel.addNewLaserSegment(newLaser);
        }
        sourceSegment.setOn(!wasOn);
        updateBoard();
    }

    @Override
    public void setBoardDimension(float x1, float x2, float y1, float y2) {
        mCurrentBoardDimension = new RectF(x1, y1, x2, y2);
        mBoardModel.setmBoardDimension(mCurrentBoardDimension);
        ArrayList<Laser> segments = mBoardModel.getmLaserSegments();
        Laser sourceSegment = segments.get(0);
        sourceSegment.setmAreaDimension(mCurrentBoardDimension);
    }

    @Override
    public void laserAngleChanged(int angle) {
        mCurrentLaserAngle = angle;
    }

    @Override
    public void mirrorAngleChanged(int id, int angle) {
        Mirror mirror = mBoardModel.getmMirrors()[id];
        mirror.setAngle(angle);
        updateBoard();
    }

    @Override
    public void targetHit() {
        Log.d(TAG, "target hit");
        //TODO do whatever you wanna do
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(sLaserAngleKey, mCurrentLaserAngle);
        savedInstanceState.putParcelable(sBoardModelKey, mBoardModel);
        savedInstanceState.putBoolean(sIsAllMirrorsDeployed, mBoardModel.getmLevel().isAllMirrorsDeployed());
    }
}
