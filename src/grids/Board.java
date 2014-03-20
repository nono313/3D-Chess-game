package grids;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import Global.Coord;
import Pieces.Piece;

public class Board extends HashSet<Grid>{
	
	public void Board() {
		
	}
	public Board getAttackBoards() {
		Board tree = new Board();
		for(Grid g : this) {
			if(g.isAnAttackBoard())
				tree.add(g);
		}
		return tree;
	}
	
	public Board getMovableLevels() {
		Board tree = new Board();
		for(Grid g : this) {
			if(g.getLevel() % 2 == 1)
				tree.add(g);
		}
		return tree;
	}
	
	
	
	
	
	public Grid getGridContaining(Coord c) {
		Iterator<Grid> it = this.iterator();
		Grid g = null, tmp = null;
		while(it.hasNext() && g == null) {
			tmp = it.next();
			if(tmp.contains(c))
				g = tmp;
		}
		return g;
	}
	
	public Corner getCornerOfPiece(Piece p) {
		Grid g = getGridContaining(p.getCoordinates());
		if(g.isAnAttackBoard()) {
			return g.getCorner();
		}
		else
			return null;
	}
	
	
	public Grid getGridAtCorner(Corner cor) {
		Board treeAttack = this.getAttackBoards();
		Iterator<Grid> it = treeAttack.iterator();
		Grid g;
		do {
			g = it.next();
		}while(it.hasNext() && !g.getCorner().equals(cor));
		if(g.getCorner().equals(cor)) {
			return g;
		}
		else
			return null;
	}
	
	
	
	public Board getFixedLevels() {
		Board tree = new Board();
		for(Grid g : this) {
			if(g.getLevel() % 2 == 0)
				tree.add(g);
		}
		return tree;
	}
	
	public ArrayList<Grid> getGridAtLevel(int level) {
		ArrayList<Grid> list = new ArrayList<Grid>();
		for(Grid g : this) {
			if(g.getLevel() == level)
				list.add(g);
		}
		return list;
	}
	
	
	public Grid getGridAtCorner(String str) {
		return getGridAtCorner(new Corner(str));
	}
	
	public void addGrid(Grid g) {
		this.add(g);
	}
}
