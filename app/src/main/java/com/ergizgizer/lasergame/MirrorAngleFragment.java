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

    private Context mContext;
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
        mContext = context;
        mController = (AngleListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMirrorAngleSeekBars = new SeekBar[mMirrors.length];
        mAnglesInDegrees = new TextView[mMirrors.length];
        mAngleSliders = new AngleSlider[mMirrors.length];
    }

    private void onPrepare(View view) {
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
        onPrepare(view);
        return view;
    }
}
