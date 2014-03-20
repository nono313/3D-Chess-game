package grids;

import java.awt.Color;
import java.util.TreeSet;

import Global.Coord;
import Players.AbstractPlayer;

public class Grid {
	private int level;
	private Coord minCoord;
	private Coord maxCoord;
	private boolean attackBoard;
	private Corner corner;
	private Color defaultOwnersColor;
	public Corner getCorner() {
		return corner;
	}
	public void setCorner(Corner corner) {
		this.corner = corner;
	}
	
	public Grid() {}
	public Grid(Coord begin) {
		this.level = begin.getZ();
		
		minCoord = begin;
		int size = ((level%2==0) ? 4 : 2);
		maxCoord = new Coord(begin.getX() + size - 1, begin.getY() + size - 1, level);
		if(level%2 == 1)
			attackBoard = true;
		else
			attackBoard = false;
		defaultOwnersColor = originalOwner();
	}
	public Grid(String str) { /*only for attack grids */
		this.corner = new Corner(str);
		this.setCoordFromCorner(corner);
		if(level%2 == 1)
			attackBoard = true;
		else
			attackBoard = false;
		defaultOwnersColor = originalOwner();
	}
	public Grid(String str, Color colOwner) { /*only for attack grids */
		this.corner = new Corner(str);
		this.setCoordFromCorner(corner);
		if(level%2 == 1)
			attackBoard = true;
		else
			attackBoard = false;
		defaultOwnersColor = colOwner;
	}
	public Grid(Coord begin, Coord end) {
		this.level = begin.getZ();
		minCoord = begin;
		maxCoord = end;
		if(level%2 == 1)
			attackBoard = true;
		else
			attackBoard = false;
		defaultOwnersColor = originalOwner();
	}
	public Grid(Grid g) {
		this.level = g.level;
		this.maxCoord = g.maxCoord;
		this.minCoord = g.minCoord;
		this.attackBoard = g.attackBoard;
		this.defaultOwnersColor = g.defaultOwnersColor;
		this.corner = g.corner;
	}
	public void setCoordFromCorner(Corner cor) {
		this.corner = cor;
		this.level = corner.getLevel();
		this.minCoord = corner.getMinCoord();
		this.maxCoord = new Coord(minCoord.getX() + 1, minCoord.getY() + 1, level);
	}
	
	/*USELESS...
	 * public Grid(Grid g) {
		super(g);
		this.level = g.level;
		this.size = g.size;
		this.grid = (TreeMap<Coord, Piece>) g.grid.clone();
	}*/
	
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getRows() {
		return maxCoord.getX()-minCoord.getX();
	}
	
	public boolean isAnAttackBoard() {
		return attackBoard;
	}
	
	
	/*public void printMovesFrom(Coord c) {
		StringMap tmp = this.toStringMap();
		Coord newCoord;
		
		if(this.getPieceAt(c) != null) {
			for(Move m : this.getPieceAt(c).movements) {
				if(m.isRepeat()) {
					newCoord = new Coord(c.getX() + m.getFront(), c.getY() + m.getRight());
					while(this.containsKey(newCoord) && this.get(newCoord) == null) {
						if(this.get(newCoord) == null && m.isMove()) {
							tmp.put(newCoord, "X");
						}
						else if(this.get(newCoord) != null && m.isAttack()) {
							tmp.put(newCoord, "X (" + tmp.get(newCoord) + ")");
						}
						newCoord.setX(newCoord.getX() + m.getFront());
						newCoord.setY(newCoord.getY() + m.getRight());						
					}
					if(this.containsKey(newCoord) && this.get(newCoord).color != this.get(c).color && m.isAttack()) {
						tmp.put(newCoord, "X=" + tmp.get(newCoord));
					}
				}else {
					newCoord = new Coord(c.getX() + m.getFront(), c.getY() + m.getRight());
					if(this.get(newCoord) == null && m.isMove()) {
						tmp.put(newCoord, "X");
					}
					else if(this.get(newCoord) != null && this.get(newCoord).color != this.get(c).color && m.isAttack()) {
						tmp.put(newCoord, "X=" + tmp.get(newCoord));
					}
					
				}
			}
			tmp.print();
		}
		
	}*/
	public boolean contains(Coord c) {
		return (c.getZ() == this.level && c.getX() >= minCoord.getX() && c.getX() <= maxCoord.getX() && c.getY() >= minCoord.getY() && c.getY() <= maxCoord.getY());
	}
	public boolean contains2d(Coord c) {
		return (c.getX() >= minCoord.getX() && c.getX() <= maxCoord.getX() && c.getY() >= minCoord.getY() && c.getY() <= maxCoord.getY());
	}

	public boolean isOnTheLeft() {
		return minCoord.getY() == 0;
	}
	public boolean isOnFront() {
		return minCoord.getX() == 0;
	}
	
	/*public void printMovesFrom(Piece piece) {
		
		
		Piece p = null;
		int n = minCoord.getY();
		
		for(int i = minCoord.getX(); i < maxCoord.getX(); i++) {
			for(int j = 0; j <  n; j++) {
	        	System.out.print("\t");
	        }
			for(int j = minCoord.getY(); j < maxCoord.getY(); j++) {
				System.out.print("|");
				p = getPieceAt(new Coord(i,j));
				if(p == null) {
					if(piece.canGoTo(new Coord(i,j)))
						System.out.print("X\t");
					else
						System.out.print("_\t");
				} else
					System.out.print(p + "\t");
			}
			System.out.println("|");
			
		}
		System.out.println();
	}*/
	
	public Coord getMinCoord() {
		return minCoord;
	}

	public void setMinCoord(Coord minCoord) {
		this.minCoord = minCoord;
	}
	public Coord getMaxCoord() {
		return maxCoord;
	}


	public void setMaxCoord(Coord maxCoord) {
		this.maxCoord = maxCoord;
	}
	
	public Color originalOwner() {
		if(level < 4)
			return Color.WHITE;
		else if (level > 4)
			return Color.BLACK;
		else
			return null;
	}
	public Color getDefaultOwnersColor() {
		return defaultOwnersColor;
	}
	public String toString() {
		return "Grid : " + minCoord + " to " + maxCoord;
	}
	
	public boolean belongsTo(AbstractPlayer player) {
		return (player.getColor() == this.defaultOwnersColor);
	}
}
