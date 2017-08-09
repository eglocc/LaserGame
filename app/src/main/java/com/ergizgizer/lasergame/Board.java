package com.ergizgizer.lasergame;

public class Board {

    static final Dimension SIZE = new Dimension(10, 10);

    private BoardObject[][] objects;
    private Level level;

    public Board() {
        objects = new BoardObject[SIZE.getWidth()][SIZE.getHeight()];
        level = new Level();
        initBoard();
    }

    public BoardObject[][] getObjects() {
        return objects;
    }

    public void setObjects(BoardObject[][] objects) {
        this.objects = objects;
    }

    public final Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void initBoard() {
        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j < objects[0].length; j++) {

                if (level.getObjectLayer()[i][j] == 'O') {
                    objects[i][j] = new Obstacle(i, j);
                } else if (level.getObjectLayer()[i][j] == 'T') {
                    objects[i][j] = new Target(i, j);
                } else {
                    objects[i][j] = new Square(i, j);
                }


            }
        }
    }

    public void putMirror(int r, int c) {
        level.putMirror(r, c);
        objects[r][c] = new Mirror(r, c);
    }

    public void pickMirror(int r, int c) {
        level.pickMirror(r, c);
        objects[r][c] = new Square(r, c);
    }
}
