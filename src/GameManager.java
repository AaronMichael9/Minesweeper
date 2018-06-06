import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Analyzers.Action;
import Analyzers.Analyzer;

public class GameManager {

	public static enum Mode {
		mine, clear
	}

	private int rows;
	private int cols;
	private int bombs;
	private Analyzer player;

	public Mode mode;
	private FrameManager frameManager;
	private boolean AUI;// allow user input
	public int remainingTiles;
	public int remainingBombs;
	public boolean won;
	public boolean lost;
	public int wins;
	public int losses;
	private int[][] board;
	private boolean[][] revealed;
	private int[][] playerView;
	private boolean[][] flagged;
	private int tileCount;

	public GameManager(FrameManager fm, Analyzer player, int rows, int cols, int bombs) {
		mode = Mode.clear;
		frameManager = fm;
		wins = 0;
		losses = 0;
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		this.player = player;
		tileCount = 0;
	}

	public void switchMode() {
		if (mode == Mode.clear)
			mode = Mode.mine;
		else
			mode = Mode.clear;
		frameManager.setSwitch(mode);
	}

	public void chooseTile(int y, int x) {
		Queue<Pair> toRev = new LinkedList<>();
		toRev.add(new Pair(y, x));
		while (!toRev.isEmpty()) {
			Pair tile = toRev.poll();
			y = tile.a;
			x = tile.b;
			if (y >= 0 && y < rows && x >= 0 && x < cols && !revealed[y][x]) {
				revealed[y][x] = true;
				if (flagged[y][x]) {
					flagged[y][x] = false;
					remainingBombs++;
				}
				int n = board[y][x];
				frameManager.reveal(y, x, n);
				// you lost
				if (n == -3) {
					lost = true;
					for (int i = 0; i < rows; i++)
						for (int j = 0; j < cols; j++)
							if (board[i][j] == -3)
								frameManager.reveal(i, j, -3);
				} else {
					remainingTiles--;
					tileCount++;
				}
				if (n == 0) {
					for (int i = y - 1; i <= y + 1; i++)
						for (int j = x - 1; j <= x + 1; j++)
							toRev.add(new Pair(i, j));
				}
			}
		}
		frameManager.updateTiles();
		frameManager.updateBombs();
	}

	public void flagTile(int y, int x) {
		if (y < 0 || x < 0 || y >= rows || x >= cols)
			return;
		if (revealed[y][x])
			return;
		if (flagged[y][x]) {
			frameManager.reveal(y, x, -1);
			remainingBombs++;
		} else {
			frameManager.reveal(y, x, -2);
			flagged[y][x] = true;
			remainingBombs--;
		}
		frameManager.updateBombs();
	}

	public void pressSwitchButton() {
		if (AUI && !won && !lost) {
			AUI = false;
			switchMode();
			AUI = true;
		}
	}

	public void pressTile(int y, int x) {
		if (AUI && !won && !lost) {
			if (mode == Mode.clear) {
				AUI = false;
				chooseTile(y, x);
				AUI = true;
			} else {
				AUI = false;
				flagTile(y, x);
				AUI = true;
			}
		}
	}

	public void pressReset() {
		if (AUI) {
			AUI = false;
			System.out.println("*");
			startGame();
		}
	}

	// Also a restart function. Should reset everything except player and
	// records
	public void startGame() {
		lost = false;
		won = false;
		remainingTiles = Main.rows * Main.cols - Main.bombs;
		remainingBombs = Main.bombs;
		mode = Mode.clear;
		board = new int[rows][cols];
		revealed = new boolean[rows][cols];
		flagged = new boolean[rows][cols];
		for (int i = 0; i < bombs; i++) {
			int y = -1;
			int x = -1;
			while (y == -1 || board[y][x] == -3) {
				y = randY();
				x = randX();
			}
			board[y][x] = -3;
		}
		for (int y = 0; y < rows; y++)
			for (int x = 0; x < cols; x++) {
				if (board[y][x] != -3)
					board[y][x] = count(y, x);
				frameManager.reveal(y, x, -1);
			}
		frameManager.updateTiles();
		frameManager.updateBombs();
		if (player == null)
			AUI = true;
	}

	public int randY() {
		return (int) (Math.random() * rows);
	}

	public int randX() {
		return (int) (Math.random() * cols);
	}

	public int count(int y, int x) {
		int n = 0;
		for (int i = y - 1; i <= y + 1; i++)
			for (int j = x - 1; j <= x + 1; j++)
				if (i >= 0 && j >= 0 && i < rows && j < cols && (i != y || j != x) && board[i][j] == -3)
					n++;
		return n;
	}

	public void generatePlayerView() {
		if (won || lost) // should never happen
			return;
		playerView = new int[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < rows; j++) {
				if (revealed[i][j])
					playerView[i][j] = board[i][j];
				else if (flagged[i][j])
					playerView[i][j] = -2;
				else
					playerView[i][j] = -1;
			}
	}

	public static class Pair {
		public int a;
		public int b;

		public Pair(int a, int b) {
			this.a = a;
			this.b = b;
		}
	}

	public void initialGame() {
		startGame();
		if (player == null) {
			//let the (human) player play
			return;
		}
		while (wins + losses < 1000) {
			generatePlayerView();
			Action next = player.nextMove(playerView);
			if (next.flag)
				flagTile(next.y, next.x);
			else
				chooseTile(next.y, next.x);
			if (won || lost) {
				if (won)
					wins++;
				else
					losses++;
				player.newGame(board, won);
				System.out.println(wins+"/"+(wins+losses));
				startGame();
			}
		}
		frameManager.close();
		System.out.println("Final Results:");
		System.out.println(wins+" wins");
		System.out.println(losses+" losses");
		System.out.println(tileCount/1000.0 + " average tiles correct");
	}
}
