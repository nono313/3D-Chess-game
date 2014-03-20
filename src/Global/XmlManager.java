package Global;

import grids.Grid;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;
import Players.AbstractPlayer;
import Players.BotPlayer;
import Players.HumanPlayer;
import Players.PlayerListener;
import chessGUI.*;

/**
 * Manage the backup process of a game into an XML file
 * uses JDom
 * 
 * @author Maxime Bourgeois
 * @author Nathan Olff
 *
 */
public class XmlManager {
	private XmlManager() {}
	
	private static Element root = new Element("Model");

	//Create a new JDom file at the root of the program
	private static org.jdom2.Document document = new Document(root);
	
	/**
	 * Encode the current game into the xml file.
	 * @param model
	 * @param fileName
	 */
    public static void encodeToFile(AbstractModel model, String fileName) {
    	//Save data about the attack grids (their original owner, their position)
    	Element attackBoards = new Element("AttackBoards");
    	for(Grid g : model.getBoard().getAttackBoards()) {
    		Element newGrid = new Element("Grid");
    		newGrid.addContent(g.getCorner().toString());
    		if(g.getDefaultOwnersColor().equals(Color.BLACK))
    			newGrid.setAttribute("defaultOwnersColor", "BLACK");
    		else
    			newGrid.setAttribute("defaultOwnersColor", "WHITE");
    		attackBoards.addContent(newGrid);
    	}
    	
    	/*
    	 * Save data about the min and max coordinates of the fixed grid.
    	 * Not really necessary here because the size or the position of these grids cannot be changed.
    	 */
    	
    	Element fixedBoards = new Element("FixedBoards");
    	for(Grid g : model.getBoard().getFixedLevels()) {
    		Element newGrid = new Element("Grid");
    		newGrid.addContent(""+g.getLevel());
    		newGrid.setAttribute("minCoordX", ""+g.getMinCoord().getX());
    		newGrid.setAttribute("minCoordY", ""+g.getMinCoord().getY());
    		newGrid.setAttribute("minCoordZ", ""+g.getMinCoord().getZ());
    		newGrid.setAttribute("minCoordX", ""+g.getMinCoord().getX());
    		fixedBoards.addContent(newGrid);
    	}
    	//Add to the root document
    	root.addContent(fixedBoards);
    	root.addContent(attackBoards);
    	//Save the name of the current player
    	Element currentPl = new Element("CurrentPlayer");
    	currentPl.addContent(model.getCurrentPlayer().getName());
    	root.addContent(currentPl);
    	/*
    	 * For each player, we save its name, its color, its type, its pieces
    	 */
    	for(AbstractPlayer player : model.getListPlayers()) {
    		Element elemPlayer = new Element("Player");
    		Element name = new Element("name");
    		name.addContent(player.getName());
    		elemPlayer.addContent(name);
    		Element color = new Element("color");
    		color.addContent(player.getColor().toString());
    		elemPlayer.addContent(color);
    		Element type = new Element("type");
    		type.addContent(player.getClass().getSimpleName());
    		elemPlayer.addContent(type);
    		Element listPieces;
   			listPieces = new Element("SetPieces");
    		
	    	for(Piece p : model.getPiecesOf(player.getColor())) {
	    		Element tmpElem = new Element("Piece");
	    		tmpElem.setAttribute("CoordX", ""+p.getCoordinates().getX());
	    		tmpElem.setAttribute("CoordY", ""+p.getCoordinates().getY());
	    		tmpElem.setAttribute("CoordZ", ""+p.getCoordinates().getZ());
	    		tmpElem.setAttribute("isAlive", ""+p.isAlive());
	    		tmpElem.addContent(p.getClass().getSimpleName());
	    		listPieces.addContent(tmpElem);
	    	}
	    	elemPlayer.addContent(listPieces);
	    	root.addContent(elemPlayer);
    	}
    	//Finally we save all this informations into an xml file
    	saveToXml(fileName);
    }
    /**
     * The data stored in the root element are stored in the xml file
     * @param file
     */
    private static void saveToXml(String file)
    {
       try
       {
          XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
          xmlOut.output(document, new FileOutputStream(file));
       }
       catch (java.io.IOException e){}
    }
    /**
     * Restore data from the file to the game
     * @param model
     * @param window
     * @param fileName
     */
    public static void decodeXML(AbstractModel model, AbstractView window, String fileName) {
    	Element elem;
        SAXBuilder sxb = new SAXBuilder();
        try
        {
           document = sxb.build(new File(fileName));
        }
        catch(Exception e){}

        elem = document.getRootElement();
        
        window.cleanAttackBoards();
        model.resetBoard();
       
       //Restore the Board
        
       List<Element> fixedBoards = elem.getChild("FixedBoards").getChildren();
       
       Iterator<Element> itFixedBoards = fixedBoards.iterator();
       while(itFixedBoards.hasNext()) {
    	   Element tmp = itFixedBoards.next();
    	   try {
    		   model.getBoard().addGrid(new Grid(new Coord(tmp.getAttribute("minCoordX").getIntValue(), tmp.getAttribute("minCoordY").getIntValue(), tmp.getAttribute("minCoordZ").getIntValue())));
    	   } catch (DataConversionException e) {
    		   e.printStackTrace();
    	   }
    	   
       }
       
       List<Element> attackBoards = elem.getChild("AttackBoards").getChildren();
       
       Iterator<Element> itAttackBoards = attackBoards.iterator();
       while(itAttackBoards.hasNext()) {
    	   Element tmp = itAttackBoards.next();
    	   if(tmp.getAttributeValue("defaultOwnersColor").equals("BLACK"))
    		   model.getBoard().addGrid(new Grid(tmp.getText(), Color.BLACK));
    	   else
    		   model.getBoard().addGrid(new Grid(tmp.getText(), Color.WHITE));
       }
        
       //Restore the players
       List<Element> listPlayers = elem.getChildren("Player");
        
       Iterator<Element> itPlayer = listPlayers.iterator();
      
       window.piecesCleaning();	
       model.resetPieces();
       model.resetListPlayers();
       
       while(itPlayer.hasNext()) {
    	   
    	   Element tmpPlayer = itPlayer.next();
    	   AbstractPlayer player = null;
    	   if(tmpPlayer.getChildText("type").equals("HumanPlayer"))
    		   player = new HumanPlayer(tmpPlayer.getChildText("name"));
    	   else
    		   player = new BotPlayer(tmpPlayer.getChildText("name"));
    	   
    	   
    	   player.addPlayerListener((PlayerListener)model);
    	   player.addPlayerListener((PlayerListener)window);
    	   model.addPlayer(player);
    	   
    	   // Restore the pieces of the player
    	   List<Element> listElemPlayers = tmpPlayer.getChild("SetPieces").getChildren();
    	   
	       Iterator<Element> i = listElemPlayers.iterator();
	       while(i.hasNext())
	       {
	    	   	Element courant = i.next();
	    	   	Piece p = null;
	    	   	Coord coord = new Coord(Integer.parseInt(courant.getAttributeValue("CoordX")), 
	    			   		Integer.parseInt(courant.getAttributeValue("CoordY")), 
	    			   		Integer.parseInt(courant.getAttributeValue("CoordZ")));
	    	   	switch(courant.getText()){
	    	   		case "Pawn":
	    	   			p = new Pawn(coord, player.getColor());
	    	   			break;
	    	   		case "Knight":
	    	   			p = new Knight(coord, player.getColor());
	    	   			break;
	    	   		case "Rook":
	    	   			p = new Rook(coord, player.getColor());
	    	   			break;
	    	   		case "Bishop":
	    	   			p = new Bishop(coord, player.getColor());
	    	   			break;
	    	   		case "Queen":
	    	   			p = new Queen(coord, player.getColor());
	    	   			break;
	    	   		case "King":
	    	   			p = new King(coord, player.getColor());
	    	   			break;
	    	   	}
	    	   	if(courant.getAttributeValue("isAlive").equals("false")) {
	    	   		p.kill();
	    	   	}
	    	   	model.addPiece(p);
	       }
       }
       // Restor the name of the current player
       if(document.getRootElement().getChild("CurrentPlayer").getText().equals("Player 1"))
    	   model.setCurrentPlayer(model.getListPlayers().get(0));
       else
    	   model.setCurrentPlayer(model.getListPlayers().get(1));
       
       // Refresh the UI
       window.refreshCurrentPlayer();
       window.attackBoardPlacement();
       window.piecesPlacement();	
       window.refreshDeadPieces();
       window.pack();
       window.revalidate();
		
       window.setLocationRelativeTo(null);
    }
}
