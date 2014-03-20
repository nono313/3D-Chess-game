package chessGUI;

import Global.Coord;

public class CoordGraph implements Comparable<CoordGraph>
{
	/* Attributs */
	protected int x;
	protected int y;

	/* Getters & Setters */
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	/* Constructor(s) */
	public CoordGraph(int x, int y){
		this.x = x;
		this.y = y;
	}

	/* Comparison methods */
	public boolean equals(CoordGraph coord) {
		if(this==null && coord == null)
			return true;
		else if(this==null || coord == null)
			return false;
		else
			return (x==coord.x) && (y==coord.y);
	}
	public int compareTo(CoordGraph coord) {
		if(x < coord.x)
			return -1;
		else if(x > coord.x)
			return 1;
		else {
			if(y < coord.y)
				return -1;
			else if(y > coord.y)
				return 1;
			else 
				return 0;
		}
	}
	public String toString() {
		return "" + x + ";" + y;
	}
	public Coord toCoord() {
		int i,j,k;
		if(x >= 12) {
			k = 6;
			i = x-8;
		}
		else if(x >= 6) {
			k = 4;
			i = x-4;
		}
		else {
			k = 2;
			i = x;
		}
		
		if(y >= 2 && y <= 5) { /*fixed grid*/
			j = y-1;
		}
		else if(y <= 1) {
			j = y;
			k++;
		}
		else {
			j = y-2;
			k++;
		}
		
		/*i = x;
		j = y;
		if(z >= 4)
			i+=4;
		if(z >= 6)
			i+=4;
		if(!isAnAttackBoard()) {
			j+=1;
		}
		else {
			if(y >= 4)
				j+=2;				
		}*/
		return new Coord(i,j,k);
	}
}
