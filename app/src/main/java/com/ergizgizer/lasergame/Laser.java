package com.ergizgizer.lasergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

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
        canvas.setBitmap(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
        Path path = new Path();
        if (start.getmColumnIndex() == 0)
            path.moveTo(start.left, start.centerY());
        else if (start.getmColumnIndex() == 9)
            path.moveTo(start.right, start.centerY());
        else if (start.getmRowIndex() == 0)
            path.moveTo(start.top, start.centerX());
        else if (start.getmRowIndex() == 9)
            path.moveTo(start.bottom, start.centerX());
        path.lineTo(start.centerX(), start.centerY());
        canvas.drawPath(path, color);
    }
}
