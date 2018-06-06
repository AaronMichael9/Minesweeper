import javax.swing.JFrame;

import Analyzers.Analyzer;
import Analyzers.DumbAnalyzer;
import Analyzers.PercentAnalyzer;
public class Main {
	

	final static int rows = 15;
	final static int cols = 30;
	final static int bombs = 40;
	
	public static void main(String[] args) {
		//Analyzer player = new DumbAnalyzer();
		Analyzer player = new PercentAnalyzer();
		FrameManager game = new FrameManager(player,rows,cols,bombs);
	}

}
