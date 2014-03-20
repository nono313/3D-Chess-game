package Pieces;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;

import Global.Coord;
import Players.AbstractPlayer;

public class ListPieces extends ArrayList<Piece> {
	
	public ListPieces getPiecesOf(AbstractPlayer player) {
		ListPieces set = new ListPieces();
		Color playerColor = player.getColor();
		for(Piece p : this) {
			if(playerColor.equals(p.getColor()))
				set.add(p);
		}
		return set;
	}
	public ListPieces getLivingPieces(ListPieces set) {
		ListPieces livingPieces = new ListPieces();
		for(Piece p : set) {
			if(p.isAlive())
				livingPieces.add(p);
		}
		return livingPieces;
	}
	
	public ListPieces getDeadPieces(ListPieces set) {
		ListPieces deadPieces = new ListPieces();
		for(Piece p : set) {
			if(!p.isAlive())
				deadPieces.add(p);
		}
		return deadPieces;
	}
	
	public ListPieces getLivingPieces() {
		ListPieces livingPieces = new ListPieces();
		for(Piece p : this) {
			if(p.isAlive())
				livingPieces.add(p);
		}
		return livingPieces;
	}
	
	public ListPieces getDeadPieces() {
		ListPieces deadPieces = new ListPieces();
		for(Piece p : this) {
			if(!p.isAlive())
				deadPieces.add(p);
		}
		return deadPieces;
	}
	public Piece getPieceAt(Coord c) {
		Piece pieceAt = null;
		Iterator<Piece> it = this.iterator();
		if(it.hasNext()) {
			Piece p = null;
			do {
				p = it.next();
			}
			while(it.hasNext() && !p.getCoordinates().equals(c));
			if(p != null && p.getCoordinates() != null && p.getCoordinates().equals(c)) {
				pieceAt = p;
			}
		}
		return pieceAt;
	}
	public boolean putPieceAt(Piece p, Coord c) {
		if(p != null) {
			p.setCoordinates(c);
			this.add(p);
			return true;
		}
		else
			return false;
	}

	public Piece getKing(AbstractPlayer player) {
		ListPieces piecesOf = this.getPiecesOf(player);
		for(Piece p : piecesOf) {
			if(p instanceof King && p.isAlive()) {
				return p;
			}
		}
		return null;		
	}
	
}
