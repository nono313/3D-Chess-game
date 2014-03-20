package Pieces;

import java.awt.Color;

import Global.Coord;
import Pieces.Move.Type;



public class Pawn extends Piece {
	private int nMoves;
	
	public Pawn(Coord c, Color color) {
		super(c, color);
		if(color == Color.WHITE) {
			movements.add(new Move(2, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(1, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(1, 1, false, Type.ONLY_ATTACK));
			movements.add(new Move(1, -1, false, Type.ONLY_ATTACK));
		}
		else {
			movements.add(new Move(-2, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(-1, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(-1, 1, false, Type.ONLY_ATTACK));
			movements.add(new Move(-1, -1, false, Type.ONLY_ATTACK));
		}
		nMoves = 0;
	}
	public Pawn(Color color) {
		super(color);
		if(color == Color.WHITE) {
			movements.add(new Move(2, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(1, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(1, 1, false, Type.ONLY_ATTACK));
			movements.add(new Move(1, -1, false, Type.ONLY_ATTACK));
		}
		else {
			movements.add(new Move(-2, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(-1, 0, false, Type.ONLY_MOVE));
			movements.add(new Move(-1, 1, false, Type.ONLY_ATTACK));
			movements.add(new Move(-1, -1, false, Type.ONLY_ATTACK));

		}
		nMoves = 0;
	}
	
	
	

	public int getnMoves() {
		return nMoves;
	}
	public void setnMoves(int nMoves) {
		this.nMoves = nMoves;
	}
	public int incrementNMoves() {
		return ++this.nMoves;
	}
	public void removeFirstMove() {
		movements.remove(0);
	}
	
	
}
