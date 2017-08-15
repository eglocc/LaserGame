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


    private Context mContext;
    private AngleListener mController;

    private SeekBar mLaserAngleSeekBar;
    private TextView mAngleInDegrees;

    private Laser mLaser;
    private AngleSlider mSeekBarListener;

    public void setmLaser(Laser laser) {
        this.mLaser = laser;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mController = (AngleListener) mContext;
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
        mSeekBarListener = new AngleSlider(mLaser, mAngleInDegrees, mController);
        mLaserAngleSeekBar.setOnSeekBarChangeListener(mSeekBarListener);
        if (savedInstanceState != null) {
            mAngleInDegrees.setText(savedInstanceState.getString("laser_angle_text"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("laser_angle_text", mAngleInDegrees.getText().toString());
    }
}