package com.ergizgizer.lasergame;

import android.widget.SeekBar;
import android.widget.TextView;

public class AngleSlider implements SeekBar.OnSeekBarChangeListener {

    private LaserAngleFragment.LaserAngleListener mListener;
    private Laser mLaser;
    private int mAngle;
    private TextView mAngleText;

    public AngleSlider(Rotatable object, TextView tv, LaserAngleFragment.LaserAngleListener laserAngleListener) {
        this.mLaser = (Laser) object;
        this.mAngleText = tv;
        this.mListener = laserAngleListener;
        this.mAngle = object.getmAngle();
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
        mListener.laserAngleChanged(mAngle);
    }
}