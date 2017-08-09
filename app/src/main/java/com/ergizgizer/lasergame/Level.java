package com.ergizgizer.lasergame;

public class Level {

    private static final String TAG = "Level";

    private char[][] objectLayer = {
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'O', 'O', 'O', 'O', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'O', 'T', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'O', 'O', 'O', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'}};

    private int numberOfAllowedMirrors;
    private int numberOfMirrors;

    public Level() {
        numberOfAllowedMirrors = 3;
        numberOfMirrors = 0;
        initLevel();
    }

    public int getNumberOfMirrors() {
        return numberOfMirrors;
    }

    public void setNumberOfMirrors(int numberOfMirrors) {
        this.numberOfMirrors = numberOfMirrors;
    }

    public char[][] getObjectLayer() {
        return objectLayer;
    }

    public void setObjectLayer(char[][] objectLayer) {
        this.objectLayer = objectLayer;
    }

    public int getNumberOfAllowedMirrors() {
        return numberOfAllowedMirrors;
    }

    public void setNumberOfAllowedMirrors(int numberOfAllowedMirrors) {
        this.numberOfAllowedMirrors = numberOfAllowedMirrors;
    }

    public void incrementNumberOfMirrors() {
        numberOfMirrors++;
    }

    public void decrementNumberOfMirrors() {
        numberOfMirrors--;
    }

    public void initLevel() {

        for (int i = 0; i < objectLayer.length; i++) {
            for (int j = 0; j < objectLayer[i].length; j++) {
                if (objectLayer[i][j] == 'O' || objectLayer[i][j] == 'T' || objectLayer[i][j] == 'M') {
                    blockRow(i);
                    blockColumn(j);
                }
            }
        }
    }

    public void putMirror(int r, int c) {
        if (objectLayer[r][c] == 'B') {
            numberOfMirrors++;
            blockRow(r);
            blockColumn(c);
            objectLayer[r][c] = 'M';
        }
    }

    public void pickMirror(int r, int c) {
        if (objectLayer[r][c] == 'M') {
            numberOfMirrors--;
            unblockRow(r);
            unblockColumn(c);
            objectLayer[r][c] = 'B';
            initLevel();
        }
    }

    public void printLevel() {
        System.out.println("---------------------");
        for (int i = 0; i < objectLayer.length; i++) {
            for (int j = 0; j < objectLayer[i].length; j++) {
                System.out.print(objectLayer[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void blockRow(int r) {
        for (int i = 0; i < objectLayer[r].length; i++) {
            if (objectLayer[r][i] == 'B')
                objectLayer[r][i] = 'X';
        }
    }

    private void blockColumn(int c) {
        for (int i = 0; i < objectLayer.length; i++) {
            if (objectLayer[i][c] == 'B')
                objectLayer[i][c] = 'X';
        }
    }

    private void unblockRow(int r) {
        for (int i = 0; i < objectLayer[r].length; i++) {
            if (objectLayer[r][i] == 'X')
                objectLayer[r][i] = 'B';
        }
    }

    private void unblockColumn(int c) {
        for (int i = 0; i < objectLayer.length; i++) {
            if (objectLayer[i][c] == 'X')
                objectLayer[i][c] = 'B';
        }
    }
}
