package grids;

import java.util.Comparator;


public class CompareGrid implements Comparator<Grid> {

	@Override
	public int compare(Grid a, Grid b) {
		return a.getMinCoord().compareTo(b.getMinCoord());
		/*return a.getLevel() - b.getLevel();*/
	}
}
