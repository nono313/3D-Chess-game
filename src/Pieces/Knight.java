package Pieces;

import java.awt.Color;

import Global.Coord;

public class Knight extends Piece {

	public Knight(Coord c, Color color) {
		// TODO Auto-generated constructor stub
		super(c, color);
		movements.add(new Move(2, 1, false));
		movements.add(new Move(1, 2, false));
		movements.add(new Move(-2, 1, false));
		movements.add(new Move(-1, 2, false));
		movements.add(new Move(2, -1, false));
		movements.add(new Move(1, -2, false));
		movements.add(new Move(-2, -1, false));
		movements.add(new Move(-1, -2, false));

	}
	public Knight(Color color) {
		// TODO Auto-generated constructor stub
		super(color);
		movements.add(new Move(2, 1, false));
		movements.add(new Move(1, 2, false));
		movements.add(new Move(-2, 1, false));
		movements.add(new Move(-1, 2, false));
		movements.add(new Move(2, -1, false));
		movements.add(new Move(1, -2, false));
		movements.add(new Move(-2, -1, false));
		movements.add(new Move(-1, -2, false));
		
	}

	 
}
