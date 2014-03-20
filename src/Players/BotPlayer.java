package Players;

import grids.Board;
import grids.Corner;
import grids.Grid;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import chessGUI.AbstractView;
import chessGUI.MouseManager;

import Global.AbstractModel;
import Global.Coord;
import Pieces.ListPieces;
import Pieces.Piece;


public class BotPlayer extends AbstractPlayer {
	
	/* Constructors */
	public BotPlayer(String name) {
		super(name);
	}
	public BotPlayer(String name, Color color) {
		super(name, color);
	}
	public BotPlayer() {
		super();
	}
	public boolean play(AbstractModel model, AbstractView view, MouseManager controller, int speed) {
		Piece randomPiece = null;
		TreeSet<Coord> treeMoves;
		Board movableGrids;
		Coord randomCoord = null;
		Random rnd = new Random();
		movableGrids = model.getMovableGrids();
		
		ListPieces piecesOf = model.getPieces().getLivingPieces(model.getPieces().getPiecesOf(this));
		if(model.isCheck(model.getOtherPlayer(model.getCurrentPlayer()))) {
			Piece king = model.getPieces().getKing(model.getOtherPlayer(model.getCurrentPlayer()));
			Iterator<Piece> it = model.getPieces().getPiecesOf(model.getCurrentPlayer()).iterator();
			randomPiece = null;
			while(it.hasNext() && randomPiece == null) {
				Piece p = it.next();
				if(model.attackSquares(p).contains(king.getCoordinates())) {
					randomPiece = p;
					randomCoord = king.getCoordinates();
				}
			}
			
		}
		
		if(randomPiece == null)
		{		
			if(rnd.nextInt(10) < 3 && movableGrids.size() > 0) {
				Grid gToMove = (Grid) selectAny(movableGrids);
				Corner tmpCorn = (Corner)selectAny(model.accessibleCornerForGrid(gToMove));
				//System.out.println("================================================================= : " + gToMove.toString() + " / " +tmpCorn);
				controller.moveGridTo(gToMove, tmpCorn);
			}
			else {
				do {
					randomPiece = selectAttackPiece(model, piecesOf);
					if(randomPiece == null) {
						randomPiece = selectMovablePiece(model, piecesOf);
					}
					treeMoves = model.accessibleSquares(randomPiece);
					//System.out.println("tree moves : " + treeMoves);
				}while(treeMoves.size() == 0);
				
					
				
				//System.out.println("random piece : " + randomPiece);
				//System.out.println("treeMoves : " + treeMoves);
				Set<Coord> attackCoords = model.attackSquares(treeMoves);
				//System.out.println(randomPiece);
				if(attackCoords.size() > 0 && new Random().nextInt(10) < 8) {
					//System.out.println("select any coord");
					randomCoord = (Coord) selectAny(attackCoords);
				}
				else
					randomCoord = (Coord) selectAny(treeMoves);
			}
		}
		if(randomPiece != null) {
			//System.out.println("random :\t" + randomCoord);
			Color backgroundColor = view.getCaseFrom3DCoords(randomPiece.getCoordinates()).getBackground();
			view.getCaseFrom3DCoords(randomPiece.getCoordinates()).setBackground(new Color(255,0,255, 100));
			view.getCaseFrom3DCoords(randomPiece.getCoordinates()).repaint();
			try {
				Thread.sleep(speed, 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			view.getCaseFrom3DCoords(randomPiece.getCoordinates()).setBackground(backgroundColor);
			view.getCaseFrom3DCoords(randomPiece.getCoordinates()).repaint();
			backgroundColor = view.getCaseFrom3DCoords(randomCoord).getBackground();
			view.getCaseFrom3DCoords(randomCoord).setBackground(new Color(255,0,255));
			/*view.movePieceFromCoords(randomPiece.getCoordinates().toCoordGraph(), randomCoord.toCoordGraph());*/
			/*view.piecesCleaning();
			model.moveFromCoordTo(randomPiece.getCoordinates(), randomCoord);
			view.piecesPlacement();*/
			controller.moveFromTo(randomPiece.getCoordinates(), randomCoord);
			view.getCaseFrom3DCoords(randomCoord).repaint();
			try {
				Thread.sleep(speed, 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			view.getCaseFrom3DCoords(randomCoord).setBackground(backgroundColor);
			view.getCaseFrom3DCoords(randomCoord).repaint();
			
		}
		this.fireHasPlayed();
		return true;		
	}
	private Object selectAny(Set set) {
		int size = set.size();
		Random rndGenerator = new Random();
		
		if(size != 0) {
			int item = rndGenerator.nextInt(size);
			int i = 0;
			for(Object obj : set)
			{
			    if (i == item)
			        return obj;
			    i = i + 1;
			}
		}
		//System.out.println("SELECT ANY NULL");
		return null;
	}
	private Object selectAny(List list) {
		int size = list.size();
		Random rndGenerator = new Random();
		if(size == 1) {
			return list.get(0);
		}
		else if(size != 0) {
			int item = rndGenerator.nextInt(size);
			return list.get(item);
		}
		return null;
	}
	private Piece selectAttackPiece(AbstractModel model, ListPieces set) {
		ListPieces tmpSet = new ListPieces();
		tmpSet.addAll(set);
		Piece p = null;
		do {
			p = (Piece) selectAny(tmpSet);
			if(p != null)
				tmpSet.remove(p);
		}while(tmpSet.size() > 0 && model.attackableSquares(p).size() == 0);
		return (tmpSet.size() == 0) ? null : p;
	}
	private Piece selectMovablePiece(AbstractModel model, ListPieces set) {
		ListPieces tmpSet = new ListPieces();
		//System.out.println("select movable pieces :");
		//System.out.println(set);
		//System.out.println("taille du set : " + set.size());
		tmpSet.addAll(set);
		Piece p = null;
		do {
			p = (Piece) selectAny(tmpSet);
			if(p != null)
				tmpSet.remove(p);
		}while(tmpSet.size() > 0 && model.accessibleSquares(p).size() == 0);
		//System.out.println(tmpSet);
		return (tmpSet.size() == 0 && p == null) ? null : p;
	}
}
