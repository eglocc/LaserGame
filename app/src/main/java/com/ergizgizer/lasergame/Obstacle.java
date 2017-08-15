package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Obstacle extends BoardObject {

    static Bitmap sObstacleIcon;

    public Obstacle(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        canvas.drawBitmap(sObstacleIcon, this.left, this.top, null);
    }
}
