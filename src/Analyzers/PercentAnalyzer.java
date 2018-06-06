package Analyzers;

import java.util.LinkedList;
import java.util.Queue;

//Aaron Michael 6/6/2018
//Second attempt
//more advanced logic than the first attempt
//first square is a guess
//then tries to location squares that are definitely safe/mined
//(only looks one step ahead unfortunately)
//same result as the previous analyzer, no wins
//slightly higher tiles revealed
//295 to 305



public class PercentAnalyzer implements Analyzer {
	boolean firstMove;
	double[][] chance;
	Queue<Action> queue;
	int rows;
	int cols;

	public PercentAnalyzer() {
		firstMove = true;
		queue = new LinkedList<Action>();
	}

	@Override
	public Action nextMove(int[][] board) {
		if (!queue.isEmpty())
			return queue.poll();
		rows = board.length;
		cols = board[0].length;
		chance = new double[rows][cols];
		if (firstMove) {
			firstMove = false;
			return new Action(0, 0);
		}
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				if (board[i][j] > 0) {
					calculate(board, i, j);
					if (!queue.isEmpty())
						return queue.poll();
				}
		
		
		//if the code reaches here, it found no certainty
		//now it guesses based on calculated odds
		//most of the time, it will choose a blank spot disjointed from any numbers
		//since there are 40 bombs in 450 squares, that is better odds that even 1 in 8
		
		//find best odds
		double min = 1;
		for (int i=0;i<rows;i++)
			for (int j=0;j<rows;j++)
				if (board[i][j]==-1 && chance[i][j]<min)
					min = chance[i][j];
		//find square
		for (int i=0;i<rows;i++)
			for (int j=0;j<rows;j++)
				if (board[i][j]==-1 && chance[i][j]<=min)
					return new Action(i,j);
		
		//should never get here, but just in case
		return new Action((int) (Math.random()*rows),(int) (Math.random()*cols));
	}

	@Override
	public void newGame(int[][] lastBoard, boolean won) {
	}

	public void calculate(int[][] board, int y, int x) {
		int bombs = board[y][x];
		int found = 0;
		int unknown = 0;
		for (int i = y - 1; i <= y + 1; i++)
			for (int j = x - 1; j <= x + 1; j++)
				if (j >= 0 && j < cols && i >= 0 && i < rows && (j != x || i != y)) {
					if (board[i][j] == -1)
						unknown++;
					if (board[i][j] == -2)
						found++;
				}
		for (int i = y - 1; i <= y + 1; i++)
			for (int j = x - 1; j <= x + 1; j++)
				if (i >= 0 && i < rows && j >= 0 && j < cols && (i != y || j != x) && board[i][j] == -1) {
					// remaining bombs = 0
					if (bombs - found == 0)
						queue.add(new Action(i, j, false));
					// remaining bombs = unknown squares
					else if (bombs - found == unknown)
						queue.add(new Action(i, j, true));
					else
						chance[i][j] = Math.max(chance[i][j], (bombs - found) / (unknown - 0.0));
				}

	}

}
