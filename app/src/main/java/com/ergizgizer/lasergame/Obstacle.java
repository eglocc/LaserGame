package com.ergizgizer.lasergame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Obstacle extends BoardObject {

    public Obstacle(Context context, int rowIndex, int columnIndex) {
        super(context, rowIndex, columnIndex);
        setmIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.p56));
    }

    @Override
    public void draw(Context context, final Canvas canvas) {
        super.draw(context, canvas);
        canvas.drawBitmap(getmIcon(), this.left, this.top, null);
    }
}
