package com.ergizgizer.lasergame;

public class Dimension {

    private int mWidth;
    private int mHeight;

    public Dimension(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int w) {
        mWidth = w;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int h) {
        mHeight = h;
    }
}
