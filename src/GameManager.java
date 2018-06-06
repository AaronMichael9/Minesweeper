import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

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

	public GameManager(FrameManager fm, Analyzer player, int rows, int cols, int bombs) {
		mode = Mode.clear;
		frameManager = fm;
		wins = 0;
		losses = 0;
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		this.player = player;
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
				System.out.println(n);
				frameManager.reveal(y, x, n);
				// you lost
				if (n == -3) {
					lost = true;
				} else {
					remainingTiles--;
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

	public void pressSwitchButton() {
		if (AUI && !won && !lost) {
			AUI = false;
			switchMode();
			AUI = true;
		}
	}

	public void pressTile(int y, int x) {
		if (AUI && !won && !lost) {
			AUI = false;
			chooseTile(y, x);
			AUI = true;
		}
	}

	public void pressReset() {
		if (AUI) {
			AUI = false;
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
			for (int x = 0; x < cols; x++)
				if (board[y][x] != -3)
					board[y][x] = count(y, x);
		frameManager.updateTiles();
		frameManager.updateBombs();
		if (player==null)
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
		// should never happen
		if (won || lost)
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
}
