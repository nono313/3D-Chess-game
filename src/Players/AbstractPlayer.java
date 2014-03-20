package Players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractPlayer 
{
	/* Attributs */
	protected String name;
	protected Color color;
	protected static int numberOfPlayers = 0;
	
	protected List<PlayerListener> listPlayerListener = new ArrayList<PlayerListener>();

	/* Constructor(s) */
	public AbstractPlayer(String name) {
		this.name = name;
		numberOfPlayers++;
		
		if(numberOfPlayers%2==1)
			color = Color.WHITE;
		else
			color = Color.BLACK;
	}
	public AbstractPlayer(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	public AbstractPlayer(){
		this("Player ");
		if(numberOfPlayers%2==1)
			name = name + 1;
		else
			name = name + 2;
	}
	
	/* Getters & Setters */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	/* Listener functions */
	public void addPlayerListener(PlayerListener abstractModel)
	{
		this.listPlayerListener.add(abstractModel);
	}
	public void fireHasPlayed() {
		for (PlayerListener listener : this.listPlayerListener) {
			listener.hasPlayed();
		}
	}
	public String toString() {
		return name + " (" + getClass().getName() + ") : " + color;
	}
}
