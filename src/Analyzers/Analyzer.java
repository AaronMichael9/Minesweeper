package Analyzers;

public interface Analyzer {
	
	public Action nextMove(int[][] board);
	public void newGame(int[][] lastBoard,boolean won);
}
//Implement Minesweeper AI here.
//Given A board state, it should return a move represented by the Action Class.
//NOTE:
//The given board state is represented by a matrix
//mines: -3 (you should never actually see this
//flag: -2 (not strictly necessary, but it's an option)
//unknown: -1
//clear: non-negative number

//The newGame function is called if the game is won/lost. The function is called with
//the solution to the previous board and a boolean representing if the program won
//(You don't need to do anything with this, but in case anyone wants to implement machine learning)

//The analyzer should not access the GameManager class or the FrameManager class
//(DO NOT just pass in the instance of the gameManager then just look up the answers)