package main;

import controller.GameController;
import model.Board;
import view.MyView;

public class Main {
	public static void main(String[] args) {
		GameController gc = new GameController(new MyView(), new Board());
		gc.paintGamePanel();
	}
}
