package com.example.chessboard;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Position> positions;
    private List<Move> moves;

    public Path(List<Position> positions, List<Move> moves) {
        this.positions = new ArrayList<>(positions);
        this.moves = new ArrayList<>(moves);
    }

    public Path(Path otherPath){
        this.positions = new ArrayList<>(otherPath.getPositions());
        this.moves = new ArrayList<>(otherPath.getMoves());
    }

    public void addPosition(Position position){
        this.positions.add(position);
    }

    public void addMove(Move move){
        this.moves.add(move);
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<Move> getMoves() {
        return moves;
    }
}
