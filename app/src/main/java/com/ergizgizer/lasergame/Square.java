package com.ergizgizer.lasergame;

public class Square extends BoardObject {

    public Square(int rowIndex, int columnIndex) {
        super(rowIndex, columnIndex);
    }

    public String toString() {
        return String.format("%s, (%d,%d)", super.getCode(), super.getRowIndex(), super.getColumnIndex());
    }
}
