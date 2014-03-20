package Pieces;

import grids.Grid;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Global.Coord;
import Players.AbstractPlayer;


public abstract class Piece 
{
	/* Attributs */	
	protected Coord coordinates;
	protected Color color;
	protected ArrayList<Move> movements;
	protected boolean alive;
	protected ImageIcon image;

	/* Constructors */
	public Piece(Coord coor, Color col) {
		movements = new ArrayList<Move>();
		coordinates = coor;
		color = col;
		alive = true;
	}
	public Piece(Color color) {
		movements = new ArrayList<Move>();
		this.color = color;
		alive = true;
	}

	/* Getters & Setters */
	public Coord getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coord coordinates) {
		this.coordinates = coordinates;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public ArrayList<Move> getMovements() {
		return movements;
	}
	public void setMovements(ArrayList<Move> movements) {
		this.movements = movements;
	}
	
	public int getX() {
		return coordinates.getX();
	}
	public int getY() {
		return coordinates.getY();
	}
	
	/* Functions */
	public boolean isAlive() {
		return alive;
	}
	public boolean isEnnemyOf(Piece p) {
		return (color != p.color);
	}

	public boolean belongsTo(AbstractPlayer player) {
		return (player.getColor() == this.color);
	}
	public void kill() {
		alive = false;
		coordinates = new Coord(0,0,0);
	}
	
 	public String toString() {
		return this.getClass().getSimpleName() + "(" + color + ") /" + coordinates.toString();
	}	
 	public void translate(Coord c) {
		/*coordinates.translate(c);*/
 		//System.out.println("origin : " + coordinates);
 		setCoordinates(coordinates.plus(c));
 		//System.out.println("new : " + coordinates);
	}
 }
