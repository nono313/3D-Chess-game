package Global;


import chessGUI.CoordGraph;

public class Coord implements Comparable<Coord>{
	/*protected int x;
	protected int y;
	
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
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Coord(Coord c) {
		this.x = c.x;
		this.y = c.y;
	}
	public String toString() {
		return "("+x+";"+y+")";
	}
	public boolean equals(Coord c) {
		return (this.x == c.x && this.y == c.y);
	}
	public int compareTo(Coord arg0) {
	    if (this.x < arg0.x || (this.x == arg0.x && this.y < arg0.y))
	        return -1;
	    else if (this.x == arg0.x && this.y == arg0.y)
	        return 0;
	    else
	        return 1;
	}
	
	public Coord plus(Coord c) {
		return new Coord(x + c.x, y + c.y);
	}
	public boolean isPositive() {
		return (x >= 0 && y >= 0);
	}
	public boolean isOnBoard() {
		return (x >= 0 && x < 10 && y >= 0 && y < 10);
	}*/

	/* Attributs */
	private int x;
	private int y;
	private int z;

	/* Constructor(s) */
	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		// TODO Auto-generated constructor stub
	}
	public Coord() {
		
		// TODO Auto-generated constructor stub
	}
	public Coord(Coord c) {
		this.x = c.x;
		this.y = c.y;
		this.z = c.z;
		// TODO Auto-generated constructor stub
	}

	/* Getters & Setters */
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
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

	/* Functions */
	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
	public CoordGraph toCoordGraph() {
		int i,j;
		i = x;
		j = y;
		if(z >= 4)
			i+=4;
		if(z >= 6)
			i+=4;
		if(!isAnAttackBoard()) {
			j+=1;
		}
		else {
			if(y >= 4)	/*left <= 1 / right >= 4*/
				j+=2;				
		}
		return new CoordGraph(i,j);
	}
	
	public boolean equals(Coord c) {
		return (this.z == c.z && this.x == c.x && this.y == c.y);
	}	
	public int compare(Coord o1, Coord o2) {
		return o1.compareTo(o2);
	}
	public int compareTo(Coord c) {
		if(this.z == c.z) {
			if(this.x < c.x)
				return -1;
			else if(this.x > c.x)
				return 1;
			else {
				if(this.y < c.y)
					return -1;
				else if(this.y > c.y)
					return 1;
				else 
					return 0;
			}
		}
		else if(this.z < c.z)
			return -1;
		else return 1;
	}
	
	public Coord plus(Coord c) {
		return new Coord(x + c.x, y + c.y, z + c.z);
	}
	public Coord minus(Coord c) {
		return new Coord(x - c.x, y - c.y, z - c.z);
	}
	public void translate(Coord c) {
		x += c.x;
		y += c.y;
		z += c.z;
	}

	public boolean isAnAttackBoard() {
		return (z%2 == 1);
	}
	public boolean isInSpace() {	/*coord is in the total space of all the game. Doesn't mean the square actually exists ! */
		return (x >= 0 && x < 10 && y >= 0 && y < 10 && z > 0 && z < 8);
	}	
}
