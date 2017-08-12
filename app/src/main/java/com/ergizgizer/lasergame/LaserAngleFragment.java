package com.ergizgizer.lasergame;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class LaserAngleFragment extends Fragment {

    interface LaserAngleListener {
        void laserAngleChanged(int angle);
    }


    private Context mContext;
    private LaserAngleListener mMainListener;

    private SeekBar mLaserAngleSeekBar;
    private TextView mAngleInDegrees;

    private Laser mLaser;

    public void setmLaser(Laser laser) {
        this.mLaser = laser;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mMainListener = (LaserAngleListener) mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.laser_angle_slider, container, false);
        mLaserAngleSeekBar = (SeekBar) view.findViewById(R.id.laser_angle_seekbar);
        mAngleInDegrees = (TextView) view.findViewById(R.id.laser_angle_in_degrees);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AngleSlider angleSlider = new AngleSlider(mLaser, mAngleInDegrees, mMainListener);
        mLaserAngleSeekBar.setOnSeekBarChangeListener(angleSlider);
    }
}