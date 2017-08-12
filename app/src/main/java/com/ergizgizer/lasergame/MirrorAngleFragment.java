package com.ergizgizer.lasergame;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MirrorAngleFragment extends Fragment {

    //interface MirrorAngleListener

    private SeekBar mMirrorAngleSeekBar;
    private TextView mAngleInDegrees;

    private Mirror mMirror;
    private int mId;

    public void setmMirror(Mirror mirror) {
        this.mMirror = mirror;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mirror_angle_slider, container, false);
        mMirrorAngleSeekBar = (SeekBar) view.findViewById(R.id.mirror_angle_seekbar);
        mAngleInDegrees = (TextView) view.findViewById(R.id.mirror_angle_in_degrees);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //AngleSlider angleSlider = new AngleSlider(mMirror, mAngleInDegrees)
    }
}
