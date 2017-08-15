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

public class MirrorAngleFragment extends Fragment {

    static final String TAG = MirrorAngleFragment.class.getSimpleName();

    private AngleListener mController;

    private SeekBar[] mMirrorAngleSeekBars;
    private TextView[] mAnglesInDegrees;
    private AngleSlider[] mAngleSliders;

    private Mirror[] mMirrors;

    public void setmMirror(Mirror[] mirrors) {
        this.mMirrors = mirrors;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mController = (AngleListener) context;
    }

    private void onPrepare(View view) {
        mMirrorAngleSeekBars = new SeekBar[mMirrors.length];
        mAnglesInDegrees = new TextView[mMirrors.length];
        mAngleSliders = new AngleSlider[mMirrors.length];

        for (int i = 0; i < mMirrors.length; i++) {
            switch (i) {
                case 0:
                    mMirrorAngleSeekBars[i] = (SeekBar) view.findViewById(R.id.mirror_angle_seekbar_1);
                    mAnglesInDegrees[i] = (TextView) view.findViewById(R.id.mirror_angle_in_degrees_1);
                    break;
                case 1:
                    mMirrorAngleSeekBars[i] = (SeekBar) view.findViewById(R.id.mirror_angle_seekbar_2);
                    mAnglesInDegrees[i] = (TextView) view.findViewById(R.id.mirror_angle_in_degrees_2);
                    break;
                case 2:
                    mMirrorAngleSeekBars[i] = (SeekBar) view.findViewById(R.id.mirror_angle_seekbar_3);
                    mAnglesInDegrees[i] = (TextView) view.findViewById(R.id.mirror_angle_in_degrees_3);
                    break;
            }
            mAngleSliders[i] = new AngleSlider(mMirrors[i], i, mAnglesInDegrees[i], mController);
            mMirrorAngleSeekBars[i].setOnSeekBarChangeListener(mAngleSliders[i]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mirror_angle_sliders, container, false);
        if (savedInstanceState != null) {
            mMirrors = (Mirror[]) savedInstanceState.getParcelableArray("mirrors");
        }
        onPrepare(view);
        if (savedInstanceState != null) {
            for (int i = 0; i < mMirrors.length; i++) {
                mAnglesInDegrees[i].setText(savedInstanceState.getString("mirror_" + (i + 1) + "_text"));
                mMirrorAngleSeekBars[i].setProgress(savedInstanceState.getInt("mirror_" + (i + 1) + "_progress"));
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray("mirrors", mMirrors);
        for (int i = 0; i < mMirrors.length; i++) {
            outState.putString("mirror_" + (i + 1) + "_text", mAnglesInDegrees[i].getText().toString());
            outState.putInt("mirror_" + (i + 1) + "_progress", mMirrorAngleSeekBars[i].getProgress());
        }
    }
}
