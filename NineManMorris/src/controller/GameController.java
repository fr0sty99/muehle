package controller;

import view.BoardView;

public class GameController {
	public GameController() {
		
	}
	
	public void createAndShowGameView() {
		BoardView gameView = new BoardView();
		gameView.createAndShowFrame();
	}
}
