package Global;

import grids.Board;
import grids.Corner;
import grids.Grid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import Pieces.ListPieces;
import Pieces.Piece;
import Players.AbstractPlayer;

public interface AbstractModel {

	/* Listener function */
	public void hasPlayed();

	/*end of the game*/
	public boolean isGameOver();

	public void endOfGame();

	public void stopGame();

	public void newGame();

	public boolean isStopped();

	public void initializePieces();

	public void initializeBoard();

	public void setGameOver(boolean bool);
	
	/*Piece movements*/
	public TreeSet<Coord> accessibleSquares(Piece p);

	public TreeSet<Coord> attackableSquares(Piece p);

	public TreeSet<Coord> attackSquares(TreeSet<Coord> set);

	public TreeSet<Coord> attackSquares(Piece p);

	public boolean moveFromCoordTo(Coord origin, Coord dest);

	public TreeSet<Coord> checkFilterForKing(TreeSet<Coord> casesOfKing,
			AbstractPlayer player);

	public ListPieces getPieces();

	public void resetPieces();

	public void addPiece(Piece p);

	public Board getBoard();

	public void resetBoard();

	public ListPieces getPiecesOnGrid(Grid g);

	public boolean gridIsMovable(Grid g);

	public boolean gridCanGoToPosition(Grid g, String strCor);

	public boolean moveGridTo(String strCor1, Color col, String strCor2);

	public boolean gridCanGoToPosition(Corner original, Corner newCorn);

	public boolean moveGridTo(Grid grid, Corner newCorner);

	public ArrayList<Corner> accessibleCornerForGrid(Grid g);

	public Board getMovableGrids();

	public boolean gridCanGoToPosition(String strCorn1, String strCor);

	public boolean allLevelsFreeAt(int x, int y);

	public boolean allLevelsFreeAt(Coord c);

	public List<AbstractPlayer> getListPlayers();

	public AbstractPlayer getOtherPlayer(AbstractPlayer player);

	public void resetListPlayers();

	public void addPlayer(AbstractPlayer player);

	public void setCurrentPlayer(AbstractPlayer currentPlayer);

	public AbstractPlayer getCurrentPlayer();

	public boolean isEmptyAt(Coord c);

	public boolean coordIsOnAGrid(Coord c);

	public boolean coordIsInTheSpace(Coord c);

	public boolean isCheck(AbstractPlayer player);

	ListPieces getPiecesOf(Color color);

}