package Global;

import java.awt.Component;

import javax.swing.JOptionPane;

import chessGUI.AbstractView;
import chessGUI.MenuListener;
import chessGUI.MouseManager;
import chessGUI.View;

import Players.*;

/**
 * Main class of the game.<br>
 * This is where the 3 components of the MVC pattern are created and linked together.<br>
 * If also contains the main() function called at the launch of the program.
 * 
 * @author Maxime Bourgeois
 * @author Nathan Olff
 *
 */
public class Main
{
	/* Constructor */
	public Main() 
	{	
	
		/* Create the blocs */
		AbstractModel model = new Model();		
		AbstractView view = new View(model);	
		
		MouseManager controller = new MouseManager(model, view);
		MenuListener.setController(controller);
		
		/* Add the MouseListener */
		view.addMouseListener(controller);
		
		/* Launch the game */
		int awnser;
		do
		{	
			view.pack();
			view.revalidate();
			view.setLocationRelativeTo(null);
			
			model.newGame();
			
			controller.createPlayers();
			model.initializePieces();
			
			controller.refreshAll();
			
		
			model.setGameOver(false);
						
			while(!model.isGameOver() && !model.isStopped())	// Main loop of a game
			{
				/* 
				 * If the current player is a bot, we call the play() method.
				 * The human player is managed directly from the MouseManager
				 */
				if(model.getCurrentPlayer() instanceof BotPlayer) {
					((BotPlayer)model.getCurrentPlayer()).play(model, view, controller, 500);
				}
			}
			if(model.isStopped()) {
				awnser = JOptionPane.YES_OPTION;
			}
			else {
				String message = "" + model.getOtherPlayer(model.getCurrentPlayer()).getName() + " won the game !\nCongratulation " + model.getOtherPlayer(model.getCurrentPlayer()).getName() + "\n\nWant to play again ?";
				awnser = JOptionPane.showConfirmDialog((Component) view, message, "Game is over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}
			if(awnser == JOptionPane.YES_OPTION)
			{
				MenuListener.restartGame();
			}
		}while(awnser == JOptionPane.YES_OPTION);
		
		view.dispose();
	}
	
	/* Main */
	public static void main(String[] args)
	{
		new Main();
	}	
}
