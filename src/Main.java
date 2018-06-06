import javax.swing.JFrame;
public class Main {
	

	final static int rows = 15;
	final static int cols = 30;
	final static int bombs = 40;
	
	public static void main(String[] args) {
		FrameManager game = new FrameManager(null,rows,cols,bombs);
	}

}
