package com.ergizgizer.lasergame;

public class Mirror extends BoardObject {

    private int angle;

    public Mirror(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
