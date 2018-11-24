package Analyzers;

import java.util.Scanner;

//for debugging the analyzer integration
public class HackAnalyzer implements Analyzer {

	Scanner scanner = new Scanner(System.in);
	
	
	@Override
	public Action nextMove(int[][] board) {
		int y = scanner.nextInt();
		int x = scanner.nextInt();
		int b = scanner.nextInt();
		return new Action(y,x,b==1);
	}

	@Override
	public void newGame(int[][] lastBoard, boolean won) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTesting() {
		return true;
	}

}
