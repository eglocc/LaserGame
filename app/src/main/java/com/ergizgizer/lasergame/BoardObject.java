package com.ergizgizer.lasergame;

public abstract class BoardObject {

    private int rowIndex;
    private int columnIndex;
    private int backgroundColorIndex;
    private String code;

    public BoardObject(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.backgroundColorIndex = (rowIndex + columnIndex) % 2;
        this.code = ("" + (char) ('A' + columnIndex) + (rowIndex + 1));
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getBackgroundColorIndex() {
        return backgroundColorIndex;
    }

    public void setBackgroundColorIndex(int backgroundColorIndex) {
        this.backgroundColorIndex = backgroundColorIndex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
