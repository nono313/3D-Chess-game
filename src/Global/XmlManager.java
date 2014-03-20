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

public class XmlManager {
	private XmlManager() {}
	
	static Element racine = new Element("Model");

	//On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
	static org.jdom2.Document document = new Document(racine);
	   
    public static void encodeToFile(AbstractModel model, String fileName) {
        // ouverture de l'encodeur vers le fichier
    	//On crée un nouvel Element etudiant et on l'ajoute
        //en tant qu'Element de racine
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
    	racine.addContent(fixedBoards);
    	racine.addContent(attackBoards);
    	Element currentPl = new Element("CurrentPlayer");
    	currentPl.addContent(model.getCurrentPlayer().getName());
    	racine.addContent(currentPl);
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
	    	racine.addContent(elemPlayer);
    	}
        enregistre(fileName);
    }
    private static void enregistre(String fichier)
    {
       try
       {
          //On utilise ici un affichage classique avec getPrettyFormat()
          XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
          //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
          //avec en argument le nom du fichier pour effectuer la sérialisation.
          sortie.output(document, new FileOutputStream(fichier));
       }
       catch (java.io.IOException e){}
    }
    public static void decodeXML(AbstractModel model, View window, String fileName) {
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
        
       List<Element> fixedBoards = elem.getChild("FixedBoards").getChildren();
       
       Iterator<Element> itFixedBoards = fixedBoards.iterator();
       while(itFixedBoards.hasNext()) {
    	   Element tmp = (Element) itFixedBoards.next();
    	   try {
    		   model.getBoard().addGrid(new Grid(new Coord(tmp.getAttribute("minCoordX").getIntValue(), tmp.getAttribute("minCoordY").getIntValue(), tmp.getAttribute("minCoordZ").getIntValue())));
    	   } catch (DataConversionException e) {
    		   // TODO Auto-generated catch block
    		   e.printStackTrace();
    	   }
    	   
       }
       
       List<Element> attackBoards = elem.getChild("AttackBoards").getChildren();
       
       Iterator<Element> itAttackBoards = attackBoards.iterator();
       while(itAttackBoards.hasNext()) {
    	   Element tmp = (Element) itAttackBoards.next();
    	   if(tmp.getAttributeValue("defaultOwnersColor").equals("BLACK"))
    		   model.getBoard().addGrid(new Grid(tmp.getText(), Color.BLACK));
    	   else
    		   model.getBoard().addGrid(new Grid(tmp.getText(), Color.WHITE));
       }
       //System.out.println(model.getBoard());
        
       List<Element> listPlayers = elem.getChildren("Player");
        
       Iterator<Element> itPlayer = listPlayers.iterator();
      
       window.piecesCleaning();	
       model.resetPieces();
       model.resetListPlayers();
       
       while(itPlayer.hasNext()) {
    	   
	       //On crée une List contenant tous les noeuds "etudiant" de l'Element racine
    	   Element tmpPlayer = (Element) itPlayer.next();
    	   AbstractPlayer player = null;
    	   if(tmpPlayer.getChildText("type").equals("HumanPlayer"))
    		   player = new HumanPlayer(tmpPlayer.getChildText("name"));
    	   else
    		   player = new BotPlayer(tmpPlayer.getChildText("name"));
    	   
    	   
    	   player.addPlayerListener((PlayerListener)model);
    	   player.addPlayerListener((PlayerListener)window);
    	   model.addPlayer(player);
    	   
    	   List<Element> listEtudiants = tmpPlayer.getChild("SetPieces").getChildren();
    	   
	       //On crée un Iterator sur notre liste
	       Iterator<Element> i = listEtudiants.iterator();
	       while(i.hasNext())
	       {
	    	   	//On recrée l'Element courant à chaque tour de boucle afin de
	    	   	//pouvoir utiliser les méthodes propres aux Element comme :
	    	   	//sélectionner un nœud fils, modifier du texte, etc...
	    	   	Element courant = (Element)i.next();
	    	   	//On affiche le nom de l’élément courant
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
       if(document.getRootElement().getChild("CurrentPlayer").getText().equals("Player 1"))
    	   model.setCurrentPlayer(model.getListPlayers().get(0));
       else
    	   model.setCurrentPlayer(model.getListPlayers().get(1));
       window.refreshCurrentPlayer();
       window.attackBoardPlacement();
		
       window.piecesPlacement();	
       window.refreshDeadPieces();
       window.pack();
       window.revalidate();
		
       window.setLocationRelativeTo(null);
    }
}
