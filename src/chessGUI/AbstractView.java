package chessGUI;

import grids.Board;
import grids.Grid;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.TreeSet;

import javax.swing.JFrame;

import Global.Coord;

public interface AbstractView{


	/* Placement functions */
	public void attackBoardPlacement();

	public void mainBoardPlacement(Board boardList);

	public void smallBoardPlacement(Board boardList);

	public void piecesPlacement();

	public void deadPiecesPlacement();

	/* Cleaning function */
	public void cleanAttackBoards();

	public void boardCleaning(Board boardList);

	public void piecesCleaning();

	public void deadPiecesCleaning();

	/* Coords translation functions */
	public JPanelImage getCaseFrom3DCoords(Coord coord);

	/* Cases' and pieces' verification functions */
	public CoordGraph coordCaseAtPointer(int x, int y);

	public CoordGraph coordSmallCaseAtPointer(int x, int y);

	public boolean isPieceAtCoords(CoordGraph coordGraph);

	public boolean isSmalCaseOnBoard(CoordGraph coordGraph);

	/* Cases' "getters" */
	public JPanelImage getCaseFromXY(int x, int y);

	public JPanelImage getCaseFromCoords(CoordGraph coordGraph);

	public JPanelImage getCaseFromPointer(int x, int y);

	public JPanelImage getSmallCaseFromXY(int x, int y);

	public JPanelImage getSmallCaseFromCoords(CoordGraph coordGraph);

	public JPanelImage getSmallCaseFromPointer(int x, int y);

	public void refreshDeadPieces();

	public void refreshCurrentPlayer();

	public void hasPlayed();

	public int popupPlayer(String playerName);
	public void dispose();
	public void revalidate();
	public void addMouseListener(MouseListener listener);
	public void pack();

	public void setLocationRelativeTo(Component object);
}