package view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import constants.AppColors;
import model.Node;
import model.NodeSet;

// This class is responsible for representing the board aka playground.
public class BoardView extends JPanel {
	public int scale = 70;
	public int offSet = 40;

	public BoardView(int viewWidth, int viewHeight) {
		Dimension boardPanelDimension = new Dimension(viewWidth, viewHeight);
		setBackground(AppColors.panelDefaultColor);
		setPreferredSize(boardPanelDimension);
		setMaximumSize(boardPanelDimension);
	}
	
	public void addDragAndDropListener(MouseListener mouseListener) {
		this.addMouseListener(mouseListener);
	}
	
	public void drawGrid(NodeSet[] sets) {
		Graphics2D g = (Graphics2D) getGraphics();
		
		// draw rectangular lines
		for (NodeSet nodeSet : sets) {
			// draw a line from every first to every last Node of every NodeSet
			
			// attention: if "sets"-Array is not filled correctly by the createNodeSets method in GameController, we create a nullPointer
			Node startNode = nodeSet.getFirstNode();
			Node destinationNode = nodeSet.getThirdNode();
			
			g.setStroke(new BasicStroke(10));
			g.drawLine(startNode.getX()*scale + offSet, startNode.getY()*scale + offSet, destinationNode.getX()*scale + offSet, destinationNode.getY()*scale + offSet);
		}
	}
	
}
