package com.example.chessboard;

public class Move {
    private int dx;
    private int dy;

    public Move(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    @Override
    public String toString() {
        return "Move : dx = "+dx +" dy = " +dy;
    }
}
