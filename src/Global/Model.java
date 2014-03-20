package Global;

import grids.Board;
import grids.Corner;
import grids.Grid;
import grids.Corner.CornerSide;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import chessGUI.CoordGraph;
import chessGUI.MouseManager;

import Pieces.*;
import Players.AbstractPlayer;
import Players.HumanPlayer;
import Players.PlayerListener;

//import org.jdom2.*;

public class Model implements AbstractModel, PlayerListener, Serializable{
	/**
	 * 
	 */
	private List<AbstractPlayer> listPlayers;
	private ListPieces setPieces;
	private Board board;
	private AbstractPlayer currentPlayer;
	private boolean gameOver = false;
	private boolean gameStopped = false;

	/*Constructors*/
	public Model() {
		this.listPlayers = new ArrayList<AbstractPlayer>();
		board = new Board();
		setPieces = new ListPieces();
		
		this.initializeBoard();
	}
	public Model(List<AbstractPlayer> listPlayers) 
	{		
		this.listPlayers = listPlayers;
		board = new Board();
		setPieces = new ListPieces();
		
		this.initializeBoard();
		
		currentPlayer = listPlayers.get(0);
		
		this.initializePieces();
	}

	/* Listener function */
	@Override
	public void hasPlayed(){
		if(listPlayers.get(0) == currentPlayer) {
			currentPlayer = listPlayers.get(1);
		}
		else {
			currentPlayer = listPlayers.get(0);
		}
		if(setPieces.getKing(currentPlayer) == null || (setPieces.getPiecesOf(currentPlayer).size() <= 1))
			endOfGame();
		/*else if(isCheck(currentPlayer)) {
			System.out.println(currentPlayer + " is in check !");
		}*/
	}
	
	/*end of the game*/
	@Override
	public boolean isGameOver() {
		return gameOver;
	}
	@Override
	public void endOfGame() {
		this.gameOver = true;
	}
	@Override
	public void stopGame() {
		gameStopped = true;
	}
	@Override
	public void newGame() {
		gameStopped = false;
	}
	@Override
	public boolean isStopped(){
		return gameStopped;
	}
	@Override
	public void initializePieces(){
		for(int i = 1; i <= 4; i++) {
			setPieces.add(new Pawn(new Coord(2,i,2), Color.WHITE));
		}
		Piece rook = new Rook(new Coord(0,0,3), Color.WHITE);
		setPieces.add(rook);
		/*rook.translate(new Coord(3,3,-1));*/
		/*rook.setCoordinates(new Coord(3,3,2));*/
		
		setPieces.add(new Queen(new Coord(0,1,3), Color.WHITE));
		setPieces.add(new Pawn(new Coord(1,0,3), Color.WHITE));
		setPieces.add(new Pawn(new Coord(1,1,3), Color.WHITE));
		
		setPieces.add(new Rook(new Coord(0,5,3), Color.WHITE));
		setPieces.add(new King(new Coord(0,4,3), Color.WHITE));
		setPieces.add(new Pawn(new Coord(1,4,3), Color.WHITE));
		setPieces.add(new Pawn(new Coord(1,5,3), Color.WHITE));
		
		setPieces.add(new Knight(new Coord(1,1,2), Color.WHITE));
		setPieces.add(new Knight(new Coord(1,4,2), Color.WHITE));
		setPieces.add(new Bishop(new Coord(1,2,2), Color.WHITE));
		setPieces.add(new Bishop(new Coord(1,3,2), Color.WHITE));
		
		for(int i = 1; i <= 4; i++) {
			setPieces.add(new Pawn(new Coord(7,i,6), Color.BLACK));
		}
		setPieces.add(new Rook(new Coord(9,0,7), Color.BLACK));
		setPieces.add(new Queen(new Coord(9,1,7), Color.BLACK));
		setPieces.add(new Pawn(new Coord(8,0,7), Color.BLACK));
		setPieces.add(new Pawn(new Coord(8,1,7), Color.BLACK));
		
		setPieces.add(new Rook(new Coord(9,5,7), Color.BLACK));
		setPieces.add(new King(new Coord(9,4,7), Color.BLACK));
		setPieces.add(new Pawn(new Coord(8,4,7), Color.BLACK));
		setPieces.add(new Pawn(new Coord(8,5,7), Color.BLACK));
		
		setPieces.add(new Knight(new Coord(8,1,6), Color.BLACK));
		setPieces.add(new Knight(new Coord(8,4,6), Color.BLACK));
		setPieces.add(new Bishop(new Coord(8,2,6), Color.BLACK));
		setPieces.add(new Bishop(new Coord(8,3,6), Color.BLACK));
        
	}
	@Override
	public void initializeBoard() {
		Grid fix1, fix2, fix3, mov1, mov2, mov3, mov4;
		fix1 = new Grid(new Coord(1,1,2));
		fix2 = new Grid(new Coord(3,1,4));
		fix3 = new Grid(new Coord(5,1,6));
		
		mov1 = new Grid("QL1");
		mov2 = new Grid("KL1");
		mov3 = new Grid("QL6");
		mov4 = new Grid("KL6");
		
		board.add(fix1);
		board.add(fix2);
		board.add(fix3);
		board.add(mov1);
		board.add(mov2);
		board.add(mov3);
		board.add(mov4);
	}
	
	@Override
	public void setGameOver(boolean bool)
	{
		gameOver = bool;
	}
	
	/*Piece movements*/
	@Override
	public TreeSet<Coord> accessibleSquares(Piece p) {
		boolean obstacle;
		Coord newCoord;
		Iterator<Grid> it;
		Piece pieceAt;
		TreeSet<Coord> tree = new TreeSet<Coord>();
		
		for(Move m : p.getMovements()) {
			it = board.iterator();
			obstacle = false;
			newCoord = p.getCoordinates();
			newCoord = newCoord.plus(m.getCoord());
			pieceAt = setPieces.getPieceAt(newCoord);
			if(pieceAt != null) {
				obstacle = true;
			}
			do {
				for(Grid g : board){
					if(g.contains2d(newCoord)) {
						pieceAt = setPieces.getPieceAt(new Coord(newCoord.getX(), newCoord.getY(), g.getLevel()));
						
						if((pieceAt == null && m.isMove()) || (pieceAt != null && pieceAt.isEnnemyOf(p) && m.isAttack())) {
							tree.add(new Coord(newCoord.getX(), newCoord.getY(), g.getLevel()));
							//System.out.println("tree add " + new Coord(newCoord.getX(), newCoord.getY(), g.getLevel()));
						}
						if(pieceAt != null) {
							obstacle = true;
						}
					}
				}
				newCoord = newCoord.plus(m.getCoord());
			}while(obstacle == false && m.isRepeat() && coordIsInTheSpace(newCoord));
		}
		if(p instanceof King && p.belongsTo(currentPlayer)) {
			checkFilterForKing(tree, currentPlayer);
		}
		return tree;
	}
	
	@Override
	public TreeSet<Coord> attackableSquares(Piece p) { /*can attack a piece here */
		boolean obstacle;
		Coord newCoord;
		Piece pieceAt;
		TreeSet<Coord> tree = new TreeSet<Coord>();
		
		for(Move m : p.getMovements()) {
			obstacle = false;
			newCoord = p.getCoordinates();
			newCoord = newCoord.plus(m.getCoord());
			pieceAt = setPieces.getPieceAt(newCoord);
			if(pieceAt != null) {
				obstacle = true;
			}
			do {
				for(Grid g : board){
					if(g.contains2d(newCoord)) {
						pieceAt = setPieces.getPieceAt(new Coord(newCoord.getX(), newCoord.getY(), g.getLevel()));
						
						if(m.isAttack() && (pieceAt == null || pieceAt.isEnnemyOf(p))) {
							tree.add(new Coord(newCoord.getX(), newCoord.getY(), g.getLevel()));
						}
						if(pieceAt != null) {
							obstacle = true;
						}
						
					}
				}
				newCoord = newCoord.plus(m.getCoord());
			}while(obstacle == false && m.isRepeat() && coordIsInTheSpace(newCoord));
		}
		if(p instanceof King && p.belongsTo(currentPlayer)) {
			checkFilterForKing(tree, currentPlayer);
		}
		return tree;
	}
	
	@Override
	public TreeSet<Coord> attackSquares(TreeSet<Coord> set) {
		TreeSet<Coord> tree = new TreeSet<Coord>();
		for(Coord c : set) {
			if(setPieces.getPieceAt(c) != null)
				tree.add(c);
		}
		return tree;
	}
	
	@Override
	public TreeSet<Coord> attackSquares(Piece p) {
		return attackSquares(accessibleSquares(p));
	}
	
	@Override
	public boolean moveFromCoordTo(Coord origin, Coord dest) {
		Piece p = setPieces.getPieceAt(origin);
		if(p != null) {
			Piece pTo = setPieces.getPieceAt(dest);
			if(pTo != null) {
				pTo.kill();
			}
			p.setCoordinates(dest);
			if(p instanceof Pawn && (((Pawn) p).incrementNMoves() == 1)) {
				((Pawn) p).removeFirstMove();
			}
			//System.out.println("Move " + p.toString().charAt(0) + " from " + origin + " to " + dest);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public TreeSet<Coord> checkFilterForKing(TreeSet<Coord> casesOfKing, AbstractPlayer player) {
		ListPieces piecesOfEnnemy = setPieces.getPiecesOf(getOtherPlayer(player));
		if(casesOfKing.size() > 0) {
			for(Piece p : piecesOfEnnemy) {
				TreeSet<Coord> tmpAccessCases = attackableSquares(p);
				for(Coord c : tmpAccessCases) {
					if(casesOfKing.contains(c))
						casesOfKing.remove(c);
				}
			}
		}
		return casesOfKing;
	}
	
	/*Get several sets of pieces*/
	public ListPieces getPieces() {
		return setPieces;
	}
	
	@Override
	public ListPieces getPiecesOf(Color color) {
		AbstractPlayer player;
		if(color.equals(listPlayers.get(0).getColor()))
			player = listPlayers.get(0);
		else
			player = listPlayers.get(1);
		return setPieces.getPiecesOf(player);
	}	
	
	@Override
	public void resetPieces() {
		setPieces = new ListPieces();
	}
	
	@Override
	public void addPiece(Piece p) {
		setPieces.add(p);
	}

	
	/*Board manager*/
	
	@Override
	public Board getBoard() {
		return board;
	}
	
	@Override
	public void resetBoard() {
		board = new Board();
	}
	
	/*Corner manager*/
	
	
	
	@Override
	public ListPieces getPiecesOnGrid(Grid g) {
        ListPieces tree = new ListPieces();
        for(Piece p : setPieces) {
                if(g.contains(p.getCoordinates()))
                        tree.add(p);
        }
        /* System.out.println("getPieces : " + tree); */
        return tree;
	}
	@Override
	public boolean gridIsMovable(Grid g) {
        if(!g.isAnAttackBoard())
                return false;
        else {
                ListPieces tree = getPiecesOnGrid(g);
                Iterator<Piece> it = tree.iterator();
                Piece pFirst = null; 
                if(it.hasNext())
                        pFirst = it.next();
                return ((pFirst != null && tree.size() == 1 && currentPlayer.getColor() == pFirst.getColor()) || 
                                (tree.size() == 0 && currentPlayer.getColor() == g.getDefaultOwnersColor()));
        }
	}
	@Override
	public boolean gridCanGoToPosition(Grid g, String strCor) {
		if(gridIsMovable(g)) {
			Corner newCorn = new Corner(strCor);
			if(g.getCorner().isAccessible(newCorn)) {
				Board treeAttack = board.getAttackBoards();
				boolean free = true;
				for(Grid gTmp : treeAttack) {
					if(gTmp.getCorner() == newCorn)
						free = false;
				}
				return free;
			}
			else
				return false;
		}
		else
			return false;
	}
	@Override
	public boolean moveGridTo(String strCor1, Color col, String strCor2) {
		return moveGridTo(board.getGridAtCorner(strCor1), new Corner(strCor2));
	}
	@Override
	public boolean gridCanGoToPosition(Corner original, Corner newCorn) {
		Grid g = board.getGridAtCorner(original);
		if(g != null && gridIsMovable(g)) {
			if(g.getCorner().isAccessible(newCorn)) {
				Board treeAttack = board.getAttackBoards();
				boolean free = true;
				for(Grid gTmp : treeAttack) {
					if(gTmp.getCorner().equals(newCorn))
						free = false;
				}
				return free;
			}
			else
				return false;
		}
		else
			return false;
	}
	@Override
	public boolean moveGridTo(Grid grid, Corner newCorner) {
		if(gridCanGoToPosition(grid.getCorner(), newCorner)) {
			ListPieces pieces = getPiecesOnGrid(grid);
			//System.out.println("PIECES ON  !!! " + pieces);
			Coord translation = newCorner.getMinCoord().minus(grid.getMinCoord());
			//System.out.println("TRANSLATE !!! " + translation);
			for(Piece p : pieces) {
				p.translate(translation);
			}
			//System.out.println("PIECES ON  !!! " + pieces);
			
			grid.setCoordFromCorner(newCorner);
			//System.out.println("level : " + newCorner.getLevel());
			//System.out.println("-----------------BOARD : " + board);
			return true;
		}
		else
			return false;
	}
	@Override
	public ArrayList<Corner> accessibleCornerForGrid(Grid g) {
		ArrayList<Corner> tree = new ArrayList<Corner>();
		Corner gridCorner = g.getCorner();
		List<Corner> allPossibleCorner = Corner.getAllCornerPossible();
		Board treeAttack = board.getAttackBoards();		
		
		boolean free;
		
		
		if(this.gridIsMovable(g)) {
			for(Corner tmpCorner: allPossibleCorner) {
				if(gridCorner.isAccessible(tmpCorner)) {
					free = true;
					for(Grid gTmp : treeAttack) 
					{
						if(gTmp.getCorner().equals(tmpCorner))
								free = false;
					}
					if(free)
						tree.add(tmpCorner);									
				}
			}
		}
		return tree;
	}
	@Override
	public Board getMovableGrids() {	/*for the current player*/
		Board movableGrids = new Board();
		Board listOfAttackBoards = board.getAttackBoards();
		for(Grid g : listOfAttackBoards) {
			if(accessibleCornerForGrid(g).size() > 0)
				movableGrids.add(g);
		}
		return movableGrids;		
	}
	
	@Override
	public boolean gridCanGoToPosition(String strCorn1, String strCor) {
		Corner original = new Corner(strCorn1);
		Grid g = board.getGridAtCorner(original);
		if(g != null && gridIsMovable(g)) {
			Corner newCorn = new Corner(strCor);
			if(g.getCorner().isAccessible(newCorn)) {
				Board treeAttack = board.getAttackBoards();
				boolean free = true;
				for(Grid gTmp : treeAttack) {
					if(gTmp.getCorner().equals(newCorn))
						free = false;
				}
				return free;
			}
			else
				return false;
		}
		else
			return false;
	}
	@Override
	public boolean allLevelsFreeAt(int x, int y) {
		boolean b = true;
		Iterator<Grid> it = board.iterator();
		Grid g;
		while(it.hasNext()) {
			g = it.next();
			b = b || isEmptyAt(new Coord(x,y,g.getLevel()));
		}
		return b;
	}
	@Override
	public boolean allLevelsFreeAt(Coord c) {
		return allLevelsFreeAt(c.getX(), c.getY());
	}
	
	/*Player manager*/
	
	@Override
	public List<AbstractPlayer> getListPlayers() {
		return listPlayers;
	}
	
	@Override
	public AbstractPlayer getOtherPlayer(AbstractPlayer player) {
		if(player == listPlayers.get(0))
			return listPlayers.get(1);
		else
			return listPlayers.get(0);
	}
	
	@Override
	public void resetListPlayers() {
		listPlayers = new ArrayList<AbstractPlayer>();
	}
	
	@Override
	public void addPlayer(AbstractPlayer player) {
		listPlayers.add(player);
		if(listPlayers.size() == 1)
			currentPlayer = listPlayers.get(0);
		
	}
	
	@Override
	public void setCurrentPlayer(AbstractPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	@Override
	public AbstractPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	/*Searching a square*/
	
	@Override
	public boolean isEmptyAt(Coord c) {
		return (setPieces.getPieceAt(c) == null);
	}
	
	@Override
	public boolean coordIsOnAGrid(Coord c) {
		return (board.getGridContaining(c) != null);
	}
	
	@Override
	public boolean coordIsInTheSpace(Coord c) {
		return (c.getX() >= 0 && c.getX() <= 10 && c.getY() >= 0 && c.getY() <= 5);
	}
	
	@Override
	public boolean isCheck(AbstractPlayer player) {
		ListPieces piecesOfEnnemy = setPieces.getLivingPieces(setPieces.getPiecesOf(getOtherPlayer(player)));
		Piece king = setPieces.getKing(player);
		if(king != null) {
			for(Piece p : piecesOfEnnemy) {
				TreeSet<Coord> tmpAccessCases = accessibleSquares(p);
				if(tmpAccessCases.contains(king.getCoordinates()))
					return true;
			}
		}
		return false;
	}
	
	
	

}

