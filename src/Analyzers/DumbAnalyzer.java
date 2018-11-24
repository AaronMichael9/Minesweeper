package Analyzers;

public class DumbAnalyzer implements Analyzer{

	//Aaron Michael 6/6/2018
	//First attempt at a Minesweeper algorithm
	//Very basic logic (actually just RNG)
	//Has lost over 5000 games in a row
	//245 to 255 tiles revealed each game
	//On the bright side, very fast
	//Proof of concept the interface can run
	
	@Override
	public Action nextMove(int[][] board) {
		int rows = board.length;
		int cols = board[0].length;
		return new Action((int) (rows*Math.random()),(int) (cols*Math.random()));
	}

	@Override
	public void newGame(int[][] lastBoard, boolean won) {}
	public boolean isTesting(){
		return false;
	}
}
