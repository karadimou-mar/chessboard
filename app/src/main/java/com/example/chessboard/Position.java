package com.example.chessboard;

public class Position {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isValidMove(int dx, int dy, int N) {
        if (x + dx > 0 && y + dy > 0 && x + dx <= N && y + dy <= N) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }

        Position position = (Position) obj;
        return this.getX() == position.getX() && this.getY() == position.getY();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Position : x = "+x +" y = " +y;
    }

}
