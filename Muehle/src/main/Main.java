package main;

import controller.GameController;
import model.Board;
import view.MyView;

public class Main {	
	public static void main(String[] args) {		
		GameController gc = new GameController(new MyView(), new Board());
		// TODO: fix this ugly hack and re-create drawing-part
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		gc.paintGamePanel();
	}
}
