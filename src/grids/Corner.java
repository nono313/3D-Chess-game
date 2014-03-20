package grids;

import java.util.ArrayList;
import java.util.List;

import Global.Coord;

public class Corner {
	/* Structure */
	public enum CornerSide {
		KL,
		QL;
	}

	/* Attributs */
	private CornerSide side;
	private int number;

	public Corner() {}
	/* Constructors */
	public Corner(CornerSide side, int number) {
		this.side = side;
		this.number = number;
	}
	public Corner(String string) {
		this.side = CornerSide.valueOf(string.substring(0, 2));
		this.number = string.charAt(2) - '0';
	}

	/* Getters & Setters */
	public int getLevel() {
		int level = 0;
		switch((number-1)/ 2) {
		case 0 : level = 3;
			break;
		case 1 : level = 5;
			break;
		case 2 : level = 7;
			break;
		}
		return level;
	}
	
	public CornerSide getSide() {
		return side;
	}
	public void setSide(CornerSide side) {
		this.side = side;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public Coord getMinCoord() {
		Coord coord = new Coord();
		if(side == CornerSide.QL)
			coord.setY(0);
		else
			coord.setY(4);
		coord.setZ(getLevel());
		if(number%2 == 1) {
			coord.setX(number-1);
		}
		else
			coord.setX(number+2);
		return coord;
	}
	
	public boolean containsCoord(Coord c) {
		return this.getMinCoord().compareTo(c) <= 0 && this.getMinCoord().plus(new Coord(1,1,0)).compareTo(c) >= 0;
	}
	
	/* Functions */
	public boolean isAccessible(Corner c) {
		if(c.number == number || (Math.abs(c.number - number) <= 2 && c.side == side)) {
			return true;
		}
		else
			return false;
	}
	public boolean equals(Corner c) {
		return (c.number == number && c.side == side);
	}
	public String toString() {
		return side.toString() + number;
	}
	public static List<Corner> getAllCornerPossible() {
		List<Corner> list = new ArrayList<Corner>();
		for(int i=1; i<=6 ; i++) {
			for(CornerSide cPrs : CornerSide.values()) {
				list.add(new Corner(cPrs, i));
			}
		}
		return list;
	}
}
