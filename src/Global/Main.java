package Global;

import java.awt.Component;

import javax.swing.JOptionPane;

import chessGUI.AbstractView;
import chessGUI.MenuListener;
import chessGUI.MouseManager;
import chessGUI.View;

import Players.*;


public class Main
{
	/* Constructor */
	public Main() 
	{	
		/*List<AbstractPlayer> listPlayers = new ArrayList<AbstractPlayer>();		

		AbstractPlayer player1 = new HumanPlayer();
		AbstractPlayer player2 = new HumanPlayer();
		
		listPlayers.add(player1);
		listPlayers.add(player2);*/
		
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
			
			model.setGameOver(false);
			model.newGame();
			model.resetListPlayers();
			model.resetPieces();
			controller.createPlayers();
			model.initializePieces();
			
			controller.refreshAll();
			
		
			model.setGameOver(false);
						
			while(!model.isGameOver() && !model.isStopped())
			{
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/*if(controller.getCurrentPlayer().getClass().equals(BotPlayer.class)) {*/
				if(model.getCurrentPlayer() instanceof BotPlayer) {
					((BotPlayer)model.getCurrentPlayer()).play(model, view, controller, 500);
					//System.out.println(model.getCurrentPlayer());
				}
			}
			if(model.isStopped()) {
				awnser = JOptionPane.YES_OPTION;
				//System.out.println("is stopped");
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
