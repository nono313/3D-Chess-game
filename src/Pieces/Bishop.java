package Pieces;

import java.awt.Color;

import Global.Coord;

public class Bishop extends Piece {

	public Bishop(Coord c, Color color) {
		// TODO Auto-generated constructor stub
		super(c, color);
		movements.add(new Move(1, 1, true));
		movements.add(new Move(1, -1, true));
		movements.add(new Move(-1, 1, true));
		movements.add(new Move(-1, -1, true));
	}
	public Bishop(Color color) {
		// TODO Auto-generated constructor stub
		super(color);
		movements.add(new Move(1, 1, true));
		movements.add(new Move(1, -1, true));
		movements.add(new Move(-1, 1, true));
		movements.add(new Move(-1, -1, true));
	}

	
}
