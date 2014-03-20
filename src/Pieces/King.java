package Pieces;

import java.awt.Color;

import Global.Coord;

public class King extends Piece {

	public King(Coord c, Color color) {
		// TODO Auto-generated constructor stub
		super(c, color);
		movements.add(new Move(0, -1, false));
		movements.add(new Move(0, 1, false));
		movements.add(new Move(1, 0, false));
		movements.add(new Move(-1, 0, false));
		movements.add(new Move(1, 1, false));
		movements.add(new Move(-1, -1, false));
		movements.add(new Move(-1, 1, false));
		movements.add(new Move(1, -1, false));
	}
	public King(Color color) {
		// TODO Auto-generated constructor stub
		super(color);
		movements.add(new Move(0, -1, false));
		movements.add(new Move(0, 1, false));
		movements.add(new Move(1, 0, false));
		movements.add(new Move(-1, 0, false));
		movements.add(new Move(1, 1, false));
		movements.add(new Move(-1, -1, false));
		movements.add(new Move(-1, 1, false));
		movements.add(new Move(1, -1, false));
	}



}
