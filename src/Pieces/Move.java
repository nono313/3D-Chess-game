package Pieces;

import Global.Coord;

public class Move 
{
	/* Structure */	
	public enum Type {
		ONLY_MOVE,
		ONLY_ATTACK,
		MOVE_AND_ATTACK;
	}
	
	/* Attributs */
	private int front;
	private int right;
	private boolean repeat;
	private Type attack;

	/* Constructors */
	public Move(int front, int right, boolean repeat, Type attack) {
		this.front = front;
		this.right = right;
		this.repeat = repeat;
		this.attack = attack;
	}
	public Move(int front, int right) {
		this(front, right, false, Type.MOVE_AND_ATTACK);
	}
	public Move(int front, int right, boolean repeat) {
		this(front, right, repeat, Type.MOVE_AND_ATTACK);
	}

	/* Getters & Setters */
	public int getFront() {
		return front;
	}
	public void setFront(int front) {
		this.front = front;
	}
	
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	
	public boolean isRepeat() {
		return repeat;
	}	
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	public Type getAttack() {
		return attack;
	}
	public void setAttack(Type attack) {
		this.attack = attack;
	}

	public Coord getCoord() {
		return new Coord(front, right, 0);
	}
	
	/* Functions */
	public boolean isAttackAndMove() {
		return (attack == Type.MOVE_AND_ATTACK);
	}
	public boolean isAttackOnly() {
		return (attack == Type.ONLY_ATTACK);
	}
	
	public boolean isAttack() {
		return (attack == Type.MOVE_AND_ATTACK || attack == Type.ONLY_ATTACK);
	}
	public boolean isMove() {
		return (attack == Type.MOVE_AND_ATTACK || attack == Type.ONLY_MOVE);
	}

	public boolean equals(Move m) {
		return (m.front == front && m.right == right);
	}
	public int compareTo(Move m) {
		if(front < m.front)
			return -1;
		else if(front > m.front)
			return 1;
		else {
			if(right < m.right)
				return -1;
			else if(right > m.right)
				return 1;
			else
				return 0;
		}
	}	

	public Move times(int n) {
		return new Move(this.front*n, this.right*n, this.repeat, this.attack);
	}
	public String toString() {
		return front + "/" + right;
	}
	
}
