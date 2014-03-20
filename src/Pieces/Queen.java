package Pieces;

import java.awt.Color;

import Global.Coord;

public class Queen extends Piece {

	public Queen(Coord c, Color color) {
		// TODO Auto-generated constructor stub
		super(c, color);
		movements.add(new Move(0, -1, true));
		movements.add(new Move(0, 1, true));
		movements.add(new Move(1, 0, true));
		movements.add(new Move(-1, 0, true));
		movements.add(new Move(1, 1, true));
		movements.add(new Move(-1, -1, true));
		movements.add(new Move(-1, 1, true));
		movements.add(new Move(1, -1, true));
	}
	public Queen(Color color) {
		// TODO Auto-generated constructor stub
		super(color);
		movements.add(new Move(0, -1, true));
		movements.add(new Move(0, 1, true));
		movements.add(new Move(1, 0, true));
		movements.add(new Move(-1, 0, true));
		movements.add(new Move(1, 1, true));
		movements.add(new Move(-1, -1, true));
		movements.add(new Move(-1, 1, true));
		movements.add(new Move(1, -1, true));
	}
	
	

}
