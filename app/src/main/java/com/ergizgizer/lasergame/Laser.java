package com.ergizgizer.lasergame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Laser {

    private int angle;
    private boolean isOn;
    private BoardObject start;
    private final Paint color;


    public Laser() {
        angle = 90;
        color = new Paint();
        color.setColor(Color.RED);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        this.isOn = on;
    }

    public BoardObject getStart() {
        return start;
    }

    public void setStart(BoardObject o) {
        if (isOn)
            this.start = o;
    }

    public void draw(Canvas canvas) {
        //Path path = new Path();
        //path.lineTo(.getmRect().centerY());
    }
}
