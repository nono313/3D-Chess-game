package chessGUI;

import grids.Corner;
import grids.Grid;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.TreeSet;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import Global.AbstractModel;
import Global.Coord;
import Pieces.Bishop;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;
import Players.AbstractPlayer;
import Players.BotPlayer;
import Players.HumanPlayer;
import Players.PlayerListener;

public class MouseManager implements MouseListener
{
	/* Attributs */
	protected AbstractModel model;		
	protected AbstractView view;

	private CoordGraph lastColoredCaseCoord;
	private CoordGraph lastPressedCaseCoord;	
	private Grid lastColoredGrid;	
	private Grid lastPressedGrid;	

	private ArrayList<Corner> listAccessibleCorner = null;
	private TreeSet<Coord> listAccessibleSquares = null;

	/* Constructor(s) */
	public MouseManager(AbstractModel abstractModel, AbstractView window){
		this.model = abstractModel;
		this.view = window;

		lastColoredCaseCoord = null;
		lastColoredGrid = null;
		lastPressedCaseCoord = null;
		lastPressedGrid = null;				
	}

	/* ColoredLayout methods */	
	private void addAllColoredLayout(CoordGraph pointerCoordGraph, Piece pointerPiece)
	{	
		Color moveLayoutColor = new Color(0, 255, 0, 100);
		Color attackLayoutColor = new Color(255, 0, 0, 100);

		listAccessibleSquares = model.accessibleSquares(pointerPiece);
		
		for(Coord tmpCoord : listAccessibleSquares) 
		{
			if(model.getPieces().getPieceAt(tmpCoord) == null)
				view.getCaseFromCoords(tmpCoord.toCoordGraph()).addColoredLayout(moveLayoutColor);
			else
				view.getCaseFromCoords(tmpCoord.toCoordGraph()).addColoredLayout(attackLayoutColor);
		}			

		lastColoredCaseCoord = pointerCoordGraph;
	}
	private void cleanAllColoredLayout() 
	{
		if(lastColoredCaseCoord != null) 
		{
			Piece p = model.getPieces().getPieceAt(lastColoredCaseCoord.toCoord());
			
			if(listAccessibleSquares == null)
				listAccessibleSquares = model.accessibleSquares(p);

			for(Coord tmpCoord : listAccessibleSquares)
				view.getCaseFromCoords(tmpCoord.toCoordGraph()).cleanColoredLayout();
			
			listAccessibleSquares=null;
			lastColoredCaseCoord=null;
		}
	}

	private void addAllGridColoredLayout(Grid pointerGrid)
	{	
		Color moveLayoutColor = new Color(0, 255, 0, 100);
		int tmpX, tmpY;

		listAccessibleCorner = model.accessibleCornerForGrid(pointerGrid);
		
		for(Corner tmpCorner : listAccessibleCorner)
		{
			tmpX = tmpCorner.getMinCoord().toCoordGraph().getX();
			tmpY = tmpCorner.getMinCoord().toCoordGraph().getY();

			view.getSmallCaseFromCoords(new CoordGraph(tmpX, tmpY)).addColoredLayout(moveLayoutColor);
			view.getSmallCaseFromCoords(new CoordGraph(tmpX+1, tmpY)).addColoredLayout(moveLayoutColor);
			view.getSmallCaseFromCoords(new CoordGraph(tmpX, tmpY+1)).addColoredLayout(moveLayoutColor);
			view.getSmallCaseFromCoords(new CoordGraph(tmpX+1, tmpY+1)).addColoredLayout(moveLayoutColor);
		}

		lastColoredGrid = pointerGrid;
	}
	private void cleanAllGridColoredLayout() 
	{
		int tmpX, tmpY;

		if(lastPressedGrid != null) 
		{
			if(lastColoredGrid != null)
				listAccessibleCorner = model.accessibleCornerForGrid(lastColoredGrid);
			else if(lastPressedGrid != null)
				listAccessibleCorner = model.accessibleCornerForGrid(lastPressedGrid);

			for(Corner tmpCorner : listAccessibleCorner)
			{
				tmpX = tmpCorner.getMinCoord().toCoordGraph().getX();
				tmpY = tmpCorner.getMinCoord().toCoordGraph().getY();

				view.getSmallCaseFromCoords(new CoordGraph(tmpX, tmpY)).cleanColoredLayout();
				view.getSmallCaseFromCoords(new CoordGraph(tmpX+1, tmpY)).cleanColoredLayout();
				view.getSmallCaseFromCoords(new CoordGraph(tmpX, tmpY+1)).cleanColoredLayout();
				view.getSmallCaseFromCoords(new CoordGraph(tmpX+1, tmpY+1)).cleanColoredLayout();
			}

			listAccessibleCorner=null;
			lastColoredGrid=null;
		}
	}

	/* Attack-Move verification */
	public boolean isAccessible(Coord destination) {
		boolean toBeReturn = false;
		
		if(listAccessibleSquares != null && listAccessibleSquares.contains(destination))
			toBeReturn = true;
		else if(listAccessibleCorner != null)
		{
			for(Corner tmpCorner : listAccessibleCorner)
			{
				if(tmpCorner.containsCoord(destination))
					toBeReturn = true;
			}
		}
			
		return toBeReturn;			
	}
	public void moveFromTo(CoordGraph from, CoordGraph to) {
		moveFromTo(from.toCoord(), to.toCoord());
	}
	public void moveFromTo(Coord from, Coord to) {
		view.piecesCleaning();
		model.moveFromCoordTo(from, to);
		view.piecesPlacement();
		Piece p = model.getPieces().getPieceAt(to);
		if(p instanceof Pawn && ((p.getColor().equals(Color.WHITE) && ((to.getX() == 8 && to.getZ() == 6) || to.getX() == 9) ||
				(p.getColor().equals(Color.BLACK) && ((to.getX() == 1 && to.getZ() == 2) || to.getX() == 0))))) 
		{
			promotion(p);
			view.piecesCleaning();
			view.piecesPlacement();
		}

		view.deadPiecesCleaning();		
		view.deadPiecesPlacement();	
		
		view.revalidate();
	}
	public void moveGridTo(Grid gToMove, Corner tmpCorn) {
		view.cleanAttackBoards();
		view.piecesCleaning();
		
		model.moveGridTo(gToMove, tmpCorn);
		
		view.attackBoardPlacement();
		view.piecesPlacement();
	}
	public void refreshAll() {
		view.cleanAttackBoards();
		view.piecesCleaning();		
		
		view.attackBoardPlacement();
		
		view.piecesPlacement();	
		view.revalidate();
	}	
		
	/* Methods for MouseListener */
	public void mouseClicked(MouseEvent e){}

	public void mousePressed(MouseEvent e)
	{
		CoordGraph pointerCoordGraph = view.coordCaseAtPointer(e.getX(), e.getY());	
		
		/* Case in the mainGrid */
		if(pointerCoordGraph != null)
		{
			/* If no case is "focused" or if the user have clicked on a different one */
			if(lastPressedCaseCoord == null || !pointerCoordGraph.toCoord().equals(lastPressedCaseCoord))
			{	
				Piece pointerPiece = model.getPieces().getPieceAt(pointerCoordGraph.toCoord());

				/* If there is a piece on this case and it belongs to the current player */
				if(pointerPiece != null && pointerPiece.belongsTo(model.getCurrentPlayer()))
				{
					/* Clean the case focus effect */
					if(lastPressedCaseCoord != null)
						this.cleanAllColoredLayout();					

					/* Add the focus effect */
					lastPressedCaseCoord=pointerCoordGraph;
					this.addAllColoredLayout(pointerCoordGraph, pointerPiece);
					
					view.revalidate();
				}
				/* If there is no piece, that a case is "focused" and that the case is accessible */
				else if(lastPressedCaseCoord != null && isAccessible(pointerCoordGraph.toCoord()))
				{
					/* Move the piece */
					moveFromTo(lastPressedCaseCoord, pointerCoordGraph);			

					/* Clean the case focus effect */
					this.cleanAllColoredLayout();
					
					model.getCurrentPlayer().fireHasPlayed();
				}
			}		
			/* If a movable grid was "focused", we clean his focus effect */
			if(lastColoredGrid != null)
			{
				this.cleanAllGridColoredLayout();
				lastPressedGrid=null;	
			}
		}
		else
		{
			pointerCoordGraph = view.coordSmallCaseAtPointer(e.getX(), e.getY());
			
			/* Case in the smallGrid */
			if(pointerCoordGraph != null)
			{
				Grid pointerGrid = model.getBoard().getGridContaining(pointerCoordGraph.toCoord());				
				boolean isOnAccessibleCorner = this.isAccessible(pointerCoordGraph.toCoord());
				
				/* If no grid is "focused" or if the user have clicked on a different one */
				if(lastPressedGrid == null || pointerGrid == null || (pointerGrid != null && lastPressedGrid != null && !pointerGrid.equals(lastPressedGrid)))
				{     
					/* Click on a grid (first click) */
					if(pointerGrid != null)
					{
						/* If the board is movable */
						if(pointerGrid.getCorner() != null)
						{					
							/* If the board belongs to the current player */
							if(pointerGrid.belongsTo(model.getCurrentPlayer()))
							{
								/* Clean the grid focus effect */
								if(lastPressedGrid != null)
									this.cleanAllGridColoredLayout();

								/* Add the focus effect */
								lastPressedGrid = pointerGrid;
								this.addAllGridColoredLayout(lastPressedGrid);
								
								view.revalidate();
							}
							/* Clean the grid focus effect */
							else
							{
								this.cleanAllGridColoredLayout();
								lastPressedGrid=null;	
							}
						}
						/* If board is not a movable one */
						else
						{
							this.cleanAllGridColoredLayout();
							lastPressedGrid=null;	
						}
					}
					/* Try to access to a corner (second click) */
					else if(isOnAccessibleCorner)
					{
						Coord pointerCoord = pointerCoordGraph.toCoord();

						/* Find the corner and move the grid to the corner */
						for(Corner tmpCorner : listAccessibleCorner)
						{
							if(tmpCorner.containsCoord(pointerCoord))
							{
								/* Clean the grid focus effect */
								this.cleanAllGridColoredLayout();	

								/* Move the grid */
								this.moveGridTo(lastPressedGrid, tmpCorner);

								lastPressedGrid=null;	
							}
						}
						if(lastPressedGrid != null)	
						{
							/* Clean the grid focus effect */
							this.cleanAllGridColoredLayout();	
							lastPressedGrid=null;	
						}
						model.getCurrentPlayer().fireHasPlayed();			
					}
				}
			}
			/* If a case was "focused", we clean his focus effect */
			if(lastPressedCaseCoord != null)
				this.cleanAllColoredLayout();
		}
	}
	public void mouseReleased(MouseEvent e){
		CoordGraph pointerCoordGraph = view.coordCaseAtPointer(e.getX(), e.getY());

		if(pointerCoordGraph != null && lastPressedCaseCoord != null)
		{	        		

			if(!pointerCoordGraph.equals(lastPressedCaseCoord))
			{			
				/* Then move the piece */
				if(isAccessible(pointerCoordGraph.toCoord()))
				{
					moveFromTo(lastPressedCaseCoord, pointerCoordGraph);
					this.cleanAllColoredLayout();
					model.getCurrentPlayer().fireHasPlayed();
				}
				else if(lastColoredCaseCoord != null)  
					this.cleanAllColoredLayout();
				lastPressedCaseCoord=null;
			}			
		}		
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public void createPlayers() {
		model.resetListPlayers();
		AbstractPlayer p1, p2;
		if(view.popupPlayer("Player 1") == JOptionPane.YES_OPTION)
			p1 = new HumanPlayer();
		else
			p1 = new BotPlayer();
		
		if(view.popupPlayer("Player 2") == JOptionPane.YES_OPTION)
			p2 = new HumanPlayer();
		else
			p2 = new BotPlayer();
		
		model.addPlayer(p1);
		model.addPlayer(p2);
		for(AbstractPlayer player : model.getListPlayers()) {
			player.addPlayerListener((PlayerListener) model);
			player.addPlayerListener((PlayerListener) view);
		}		
		model.setCurrentPlayer(p1);
	}
	public void promotion(Piece p) {
		UIManager.put("OptionPane.cancelButtonText", "Cancel");
		
		Object[] possibilities = {"Queen", "Bishop", "Knight", "Rook"};
		String input = (String) JOptionPane.showInputDialog(null, "Your pawn can be promoted.\nSelect its new type : ", "Promotion", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
		
		Piece newP = null;
		if(input.equals("Rook"))
			newP = new Rook(p.getCoordinates(), p.getColor());
		else if(input.equals("Knight"))
			newP = new Knight(p.getCoordinates(), p.getColor());
		else if(input.equals("Bishop"))
			newP = new Bishop(p.getCoordinates(), p.getColor());
		else
			newP = new Queen(p.getCoordinates(), p.getColor());
		model.getPieces().remove(p);
		if(newP != null)
			model.getPieces().add(newP);
		view.piecesCleaning();	
		view.piecesPlacement();
	}
}
