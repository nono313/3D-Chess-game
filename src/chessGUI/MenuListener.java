package chessGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import Global.AbstractModel;
import Global.XmlManager;

public class MenuListener implements ActionListener
{
	/* Attributes */
	private static View window;
	private static AbstractModel model;
	private static MouseManager controller;
	private String menuType;
	
	/* Constructor(s) */
	public MenuListener(View window, AbstractModel model, String menuType)
	{
		this.window = window;
		this.model = model;
		this.menuType = menuType;
	}
	public MenuListener(String menuType)
	{
		this.menuType = menuType;
	}

	public static void setController(MouseManager mouse) {
		controller = mouse;
	}
	static public void restartGame() {		
		window.cleanAttackBoards();
		window.piecesCleaning();
		
		model.resetBoard();
		model.initializeBoard();		
		
		window.attackBoardPlacement();
		
		model.resetPieces();
		window.refreshDeadPieces();
		/*window.piecesPlacement();
		model.setCurrentPlayer(model.getListPlayers().get(0));*/
		/*window.refreshCurrentPlayer();*/
		model.resetListPlayers();
		
		
		window.pack();
		window.revalidate();
		model.setGameOver(false);
		
	}
	
	/* ActionListener methods */
	public void actionPerformed(ActionEvent arg0) 
	{		
		/* gameMenu actions */
		if(menuType.equals("newGame")) {
			model.stopGame();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			restartGame();
			controller.refreshAll();
		}
		else if(menuType.equals("save")) {
			XmlManager.encodeToFile(model, "user.xml");
		}
		else if(menuType.equals("load")) {
			XmlManager.decodeXML(model, window, "user.xml");
		}		
		/* aboutMenu actions */
		else if(menuType.equals("rules"))
		{
			JOptionPane.showMessageDialog(window, "Read the file Rules.txt", "Rules", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}