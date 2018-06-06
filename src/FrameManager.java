import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FrameManager {
	
	private int rows;
	private int cols;
	private int bombs;
	
	private JFrame frame;
	private JButton mSwitch;
	private JButton resetButton;
	private GameManager gameManager;
	private JLabel progress;
	private JLabel bombR;
	private Icon mine;
	private Icon flag;
	private Icon notflag;
	private Icon black;
	private Icon green;
	private JButton[][] tiles = new JButton[15][30];
	
	
	
	public FrameManager(Analyzer player,int rows,int cols,int bombs){
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		validate();
		gameManager = new GameManager(this,player,rows,cols,bombs);
		setUpFrame();
		startGame();
	}
	private void setUpFrame(){
		createFrame();
		loadImages();
		addOtherStuff();
		addBoard();		
		upkeepFrame();
	}
	private void startGame(){
		mSwitch.setIcon(notflag);
		for (JButton[] buttons:tiles)
			for (JButton button:buttons)
				button.setIcon(black);
		gameManager.startGame();
	}
	private void createFrame(){
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.getContentPane().setLayout(new BorderLayout());
	}
	private void upkeepFrame(){
		frame.pack();
		frame.setSize(1200, 1000);
		frame.setVisible(true);
	}
	
	
	
	private void loadImages(){
		try{
			Image imine = ImageIO.read(new File("assets/mine.png"));
			imine = imine.getScaledInstance(50, 50,0);
			mine = new ImageIcon(imine);
			Image iflag = ImageIO.read(new File("assets/flag.png"));
			iflag = iflag.getScaledInstance(50, 50,0);
			flag = new ImageIcon(iflag);
			Image inotflag = ImageIO.read(new File("assets/notflag.png"));
			inotflag = inotflag.getScaledInstance(50, 50,0);
			notflag = new ImageIcon(inotflag);
			Image igreen = ImageIO.read(new File("assets/green.jpg"));
			igreen = igreen.getScaledInstance(50, 50, 0);
			green = new ImageIcon(igreen);
			Image iblack = ImageIO.read(new File("assets/black.png"));
			iblack = iblack.getScaledInstance(50, 50, 0);
			black = new ImageIcon(iblack);
		}
		catch(Exception e){
			System.out.println("failed to load images");
		}
	}
	private void addOtherStuff(){
		mSwitch = new JButton();
		mSwitch.addActionListener(new modeSwitchListener(gameManager));
		mSwitch.setIcon(notflag);
		JPanel holder = new JPanel();
		frame.add(holder,BorderLayout.PAGE_START);
		holder.add(mSwitch,BorderLayout.CENTER);
		progress = new JLabel();
		bombR = new JLabel();
		holder.add(progress,BorderLayout.LINE_START);
		holder.add(bombR,BorderLayout.LINE_END);
		resetButton = new JButton();
		JPanel oholder = new JPanel();
		frame.add(oholder, BorderLayout.PAGE_END);
		oholder.add(resetButton,BorderLayout.CENTER);
		resetButton.setText("New Game");
	}
	public void setSwitch(GameManager.Mode mode){
		if (mode==GameManager.Mode.clear){
			mSwitch.setIcon(notflag);
		}
		else{
			mSwitch.setIcon(flag);
		}
	}
	
	public static class modeSwitchListener implements ActionListener {
		GameManager gm;
		modeSwitchListener(GameManager gm){
			this.gm = gm;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			gm.pressSwitchButton();
		}
	}
	public static class gameTileListener implements ActionListener {
		GameManager gm;
		int y;
		int x;
		//x,y are zero based
		//y is measured from top
		gameTileListener(GameManager gm,int y,int x){
			this.y = y;
			this.x = x;
			this.gm = gm;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			gm.pressTile(y,x);
		}
	}
	public static class resetListener implements ActionListener {
		GameManager gm;
		resetListener(GameManager gm){
			this.gm = gm;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			gm.pressReset();
		}
	}
	
	public void addBoard(){
		JPanel table = new JPanel();
		frame.add(table,BorderLayout.CENTER);
		table.setLayout(new GridLayout(15,30));
		for (int i=0;i<15*30;i++){
			JButton tile = new JButton();
			tile.setSize(20, 20);
			int y = i/30;
			int x = i%30;
			tile.addActionListener(new gameTileListener(gameManager,y,x));
			tiles[y][x]=tile;
			table.add(tile);
		}		
	}
	public void updateTiles(){
		int n = gameManager.remainingTiles;
		progress.setText("Remaining tiles: "+n);
	}
	public void updateBombs(){
		int n = gameManager.remainingBombs;
		bombR.setText("Remaining bombs: "+n);
	}
	public void validate(){
		if (rows<=0 || bombs<=0 || cols<=0){
			System.out.println("no");
			System.exit(0);}
		if (bombs>=rows*cols)
			bombs = rows*cols - 1;
		//have fun
	}
	public void reveal(int y,int x,int n){
		if (n==-3)
			tiles[y][x].setIcon(mine);
		else if (n==0)
			tiles[y][x].setIcon(green);
		else{
			//tiles[y][x].setIcon(green);
			tiles[y][x].setText(n+"");
		}
	}
	public void putFlag(int y,int x){
		tiles[y][x].setIcon(flag);
	}
}
