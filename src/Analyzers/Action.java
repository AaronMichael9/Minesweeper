package Analyzers;

public class Action {
	
	public int y;
	public int x;
	public boolean flag;
	
	public Action(int y,int x){
		this.y = y;
		this.x = x;
		flag = false;
	}
	public Action(int y,int x,boolean flag){
		this.y = y;
		this.x = x;
		this.flag = flag;
	}
}
