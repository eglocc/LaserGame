package com.ergizgizer.lasergame;

import android.widget.SeekBar;
import android.widget.TextView;

public class AngleSlider implements SeekBar.OnSeekBarChangeListener {

    private Laser mLaser;
    private int mAngle = 90;
    private TextView mAngleText;

    public AngleSlider(Laser laser, TextView tv) {
        this.mLaser = laser;
        this.mAngleText = tv;
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
        mLaser.setmAngle(mAngle);
    }
}
