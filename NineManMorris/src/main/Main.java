package main;

import controller.GameController;
import model.BoardModel;
import view.MyWindow;

public class Main {
	public static void main(String[] args) {
		GameController gc = new GameController(new MyWindow(), new BoardModel());
		gc.paintGamePanel();
	}
}
