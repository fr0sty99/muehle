package controller;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import constants.Owner;
import model.BoardModel;
import model.Direction;
import model.Node;
import model.NodeSet;
import model.Piece;
import view.MyWindow;

public class GameController {
	MyWindow theView;
	BoardModel theModel;
	MyWindow myWindow;
	ArrayList<Node> nodeList = new ArrayList<>(); // all nodes
	NodeSet[] nodeSets = new NodeSet[16]; // all nodeSets

	
	public GameController(MyWindow theView, BoardModel theModel) {

		preparePlayers();
		
		
		this.theView = theView;
		this.theModel = theModel;
		
		this.theView.boardView.addDragAndDropListener(new DragAndDropListener());
		
		System.out.println("paintGrid");
		
		// TODO: tell the View to create the Grid with the nodes and the playerView with the piecesToSet with the data from the model
	
		this.theView.messageView.setMessage("Player One's turn. " + " --------------- " + "Set one of you piece on the grid.");
		
		
		
		// prepare for game: draw coins to set
		this.theView.playerView.drawCoins(9, Owner.PLAYER1);
		this.theView.playerView.drawCoins(9, Owner.PLAYER2);
	}
	
		// 	// index works as following:
	    //	0---------1---------2
		//	|		  |	    	|
		//	|  3------4------5  |
		//	|  |	  |      |  |
		//	|  |   6--7--8   |  |
		//  9-10--11	 12-13-14
		//  |  |  15-16--17  |  |
		//  |  |      |      |  |
		//  |  18----19-----20	|
		//	|		  |			|
		//	21-------22--------23

	public void createNodeSets() {
		int index = 0; // index of the nodes
		int dist = 3; // distance between nodes, this gets decremented after each iteration of the loop
		
		int nodeSetIndex = 0; // index of the nodeSets
		
		Node firstNode = null;
		
		for(int i = 0; i < 3; i++) { // used for the firstNodes coords
			// top NodeStet
			
			firstNode = new Node(i, i, index);
			index++;
			
			nodeList.add(firstNode);
			nodeSets[nodeSetIndex] = new NodeSet(firstNode);
			
			Node tmp = createNode(Direction.RIGHT, firstNode, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = createNode(Direction.RIGHT, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// right NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(tmp); // first of this nodeSet is the last from the Last nodeSet
			
			tmp = createNode(Direction.BOTTOM, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = createNode(Direction.BOTTOM, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// bottom NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(tmp); // first of this nodeSet is the last from the Last nodeSet
			
			tmp = createNode(Direction.LEFT, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = createNode(Direction.LEFT, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// left NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(tmp);
			
			tmp = createNode(Direction.TOP, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = firstNode;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// decrement dist because the triangles get smaller
			dist--;
		}
		
		// center nodeSets consist of already created nodes
		int firstNodeNodeSetIndex = 0;
		int secondtNodeNodeSetIndex = 4;
		int thirdNodeNodeSetIndex = 8;
		
		for(int i = 0; i < 4; i++) {
			nodeSets[nodeSetIndex] = new NodeSet(nodeSets[firstNodeNodeSetIndex].getSecondNode(), nodeSets[secondtNodeNodeSetIndex].getSecondNode(), nodeSets[thirdNodeNodeSetIndex].getSecondNode());
			nodeSetIndex++;
			firstNodeNodeSetIndex++;
			secondtNodeNodeSetIndex++;
			thirdNodeNodeSetIndex++;
		}
		
		System.out.println(Arrays.deepToString(nodeSets));
	}
	
	public Node createNode(Direction d, Node lastNode, int index, int dist) {
		switch(d) {
		case TOP:
			return new Node(lastNode.getX(), lastNode.getY() - dist, index);
		case RIGHT:
			return new Node(lastNode.getX() + dist, lastNode.getY(), index);
		case BOTTOM:
			return new Node(lastNode.getX(), lastNode.getY() + dist, index);
		case LEFT:
			return new Node(lastNode.getX() - dist, lastNode.getY(), index);
		}
		System.out.println("GameController::createNode() --- could not create node");
		return null; // could not create node
	}
	
	
	
	public void paintGrid() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.theView.boardView.drawGrid(nodeSets);
		
		/*
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Graphics2D g2d = (Graphics2D) this.theView.boardView.getGraphics();

		g2d.setStroke(new BasicStroke(10)); // line thickness
		g2d.setColor(Color.BLACK);
		
	// draw grid	
		// outer ring
		g2d.drawLine(50, 50, 450, 50);
		g2d.drawLine(450, 50, 450, 450);
		g2d.drawLine(450, 450, 50, 450);
		g2d.drawLine(50, 50, 50, 450);
		
		// middle ring
		g2d.drawLine(130, 130, 370, 130);
		g2d.drawLine(130, 130, 130, 370);
		g2d.drawLine(130, 370, 370, 370);
		g2d.drawLine(370, 130, 370, 370);
		
		// center ring
		g2d.drawLine(210, 210, 290, 210);
		g2d.drawLine(290, 210, 290, 290);
		g2d.drawLine(290, 290, 210, 290);
		g2d.drawLine(210, 290, 210, 210);
		
		// ring connections
		g2d.drawLine(50, 250, 210, 250);
		g2d.drawLine(250, 50, 250, 210);
		g2d.drawLine(290, 250, 450, 250);
		g2d.drawLine(250, 450, 250, 290);
		
	// draw pieces 
		// locations per index:
		// 0 = 50,50
		// 1 = 250,50
		// etc.
		// find a solution to render this efficiently and dyncamically
		
		*/
	}
	
	public void draw(Graphics g, Piece[] board) {
		for (Piece piece : board) {
			switch(piece.belongsTo) {
			case PLAYER1: 
				//g.drawRect(piece.index, y, width, height);
				break;
			case PLAYER2:
				
				break;
				
			case NOPLAYER:
				
				break;
			default: 
				
				break;
			}
		}
	}
	
	class DragAndDropListener implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("MouseClicked: " +e.getY() + " | " + e.getX());
			
			// TODO: move or set piece if possible
		}

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("MousePressed: " +e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("MouseReleased: " +e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			System.out.println("MouseEntered: " +e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}

		@Override
		public void mouseExited(MouseEvent e) {
			System.out.println("MouseExited: " + e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}
	}
	
	public void preparePlayers() {
		
		
	}
	
	public void runGame() {
		// Phase 1 
		// Set Pieces
		
		// 1. set Message: its whites turn, you have to set a piece to the board
		// 2. wait for playerInput ( drag & drop ) 
		
		// Phase 2 
		// Move Pieces
		
		// Phase 3 (End-Phase)
		// Jump with Pieces
	}
	
	
	
}
