package com.ergizgizer.lasergame;

import android.widget.SeekBar;
import android.widget.TextView;

public class AngleSlider implements SeekBar.OnSeekBarChangeListener {

    private AngleListener mListener;
    private Rotatable mRotatable;
    private int mAngle;
    private TextView mAngleText;
    private int mMirrorId;

    public AngleSlider(Rotatable object, TextView tv, AngleListener angleListener) {
        this.mRotatable = object;
        this.mAngleText = tv;
        this.mListener = angleListener;
        this.mAngle = object.getAngle();
    }

    public AngleSlider(Rotatable object, int id, TextView tv, AngleListener angleListener) {
        this.mRotatable = object;
        this.mMirrorId = id;
        this.mAngleText = tv;
        this.mListener = angleListener;
        if (object != null)
            this.mAngle = object.getAngle();
    }

    public int getmAngle() {
        return mAngle;
    }

    //public void setmAngle(int angle) { this.mAngle = angle; }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            mAngle = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mAngleText.setText(Integer.toString(mAngle) + "°");
        //mLaser.setmAngle(mAngle);
        if (mRotatable instanceof Laser)
            mListener.laserAngleChanged(mAngle);
        else if (mRotatable instanceof Mirror) {
            mListener.mirrorAngleChanged(mMirrorId, mAngle);
        }

    }
}
