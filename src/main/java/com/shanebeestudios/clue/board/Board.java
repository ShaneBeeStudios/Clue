/*
 * Class: Board
 * Authors: Brandon Rodriguez, Hunter Lang
 * This class is the main class pertaining to the clue com.shanebeestudios.clue.board.
 * Also contains logic to calculate targets and adjacencies
 * Relies on BadConfigFormatException, BoardCell, RoomCell, and WalkwayCell
 */

package com.shanebeestudios.clue.board;
import java.awt.Graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.shanebeestudios.clue.ClueGame;
import com.shanebeestudios.clue.player.HumanPlayer;
import com.shanebeestudios.clue.player.Player;
import com.shanebeestudios.clue.misc.SuggestDialog;
import com.shanebeestudios.clue.misc.SuggestDialog.SuggestType;

// Board class body

public class Board extends JPanel implements MouseListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;





	private ArrayList<Player> players;
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
		humanPlayer = players.get(0);
	}
	private ArrayList<BoardCell> cells;
	private Map<Character,String> rooms;
	private int numRows;
	private int numColumns;
	private boolean highlight;
	private int dieRoll;
	private int pixelModifier;

	// Filepaths for the configuration files
	private String csvFilepath, legendFilepath;

	// Array used to keep track of visited cells when the fucntion calcAdjacencies is called
	private boolean visited[];
	private Map<Integer, LinkedList<Integer>> adjacencyLists;
	private Set<BoardCell> targets;
	private Player humanPlayer;
	private boolean humanMustFinish;
	private ClueGame game;

	// Default constructor for com.shanebeestudios.clue.board. Simply initializes the values, nothing else
	public Board() {
		initialize();
	}
	
	public Board(String csv, String legend, ClueGame game) {
		this(csv, legend);
		this.game = game;
	}

	// Parameterized constructor, sets all the fields of com.shanebeestudios.clue.board using the configuration files
	public Board(String csv, String legend) {
		addMouseListener(this);
		setSize(700,700);
		initialize();
		csvFilepath = csv;
		legendFilepath = legend;

		humanMustFinish = true;
		adjacencyLists = new HashMap<Integer, LinkedList<Integer>>();
		targets = new HashSet<BoardCell>();

		loadConfigFiles();
		calcAdjacencies();
		
	}
	@Override
	public void mouseClicked(MouseEvent event) {
		boolean badCell = true;
		for(BoardCell c : targets) {
			if(c.containsClick(event.getX(), event.getY(), this)) {
				HumanPlayer p = (HumanPlayer) humanPlayer;
				p.setCanMakeAccusation(false);
				humanPlayer.setRow(c.getRow());
				humanPlayer.setColumn(c.getColumn());
				if(c.isRoom()) {
					new SuggestDialog(SuggestType.SUGGESTION, game);
				}
				repaint();

				unHighlightTargets();
				
				humanMustFinish = false;
				badCell = false;
				
			}
		}
		if(badCell)
			JOptionPane.showMessageDialog(this, "Please select a highlighted cell", "Invalid Move", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public boolean isHumanMustFinish() {
		return humanMustFinish;
	}

	public void setHumanMustFinish(boolean humanMustFinish) {
		this.humanMustFinish = humanMustFinish;
	}

	public void unHighlightTargets() {
		for(BoardCell c : targets) {
			c.highlight = false;
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}



	public void highlightTargets(int row, int column) {
		this.startTargets(this.calcIndex(row,column), dieRoll);
		for (BoardCell x : this.getTargets()) {
			x.highlight = true;
			this.repaint();
		}
		//System.out.println(pixelModifier);
		//int pixelModifier = Math.min(this.size().height/this.getNumRows(), this.size().width/this.getNumColumns());
		setSize(pixelModifier*numColumns, pixelModifier*numRows);
	}

	public void paintComponent(Graphics g) {
		int z = 0;
		for (BoardCell x: cells) {
			x.draw(g, this);
		}
		for (Player y: players) {
			y.draw(g, this);
		}
		setSize(pixelModifier*numColumns, pixelModifier*numRows);

	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
	
	// Initializes default values of cells, rooms, numRows, and numColumns
	private void initialize() {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		adjacencyLists = new HashMap<Integer, LinkedList<Integer>>();
		targets = new HashSet<BoardCell>();
		numRows = 0;
		numColumns = 0;
	}

	public void loadConfigFiles() {
		try {
	
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();

		} catch(BadConfigFormatException e) { // If one of those throws an error, catch it, print it to the screen
			System.out.println(e);
		}
	}

	// One of the heavy lifter functions for the loadConfigFiles method
	// Throws: BadConfigFormatException
	// Loads up the legend file, and populates the legend in the com.shanebeestudios.clue.board
	public void loadRoomConfig() throws BadConfigFormatException  {
		Scanner legendFile = null;
		try {
			legendFile = new Scanner(new File(legendFilepath));
		} catch (FileNotFoundException e) {
			// If we can't find the legend, throw an I/O exception under the BadConfigFormatException
			throw new BadConfigFormatException("I/O Error: " + legendFilepath + "not found!");
		}
		String[] legendSplit;

		while(legendFile.hasNextLine()) {
			legendSplit = legendFile.nextLine().split(", ");
			// If the line has more than one comma, throw an exception
			if(legendSplit.length > 2) {
				throw new BadConfigFormatException(legendFilepath + " contains data in an invalid format");
			}
			// Create a room definition using the legend information and put it into rooms 
			rooms.put(legendSplit[0].charAt(0), legendSplit[1]);
		}
		legendFile.close();
	}

	// Second heavy lifter function for the loadConfigFiles method
	// Throws: BadConfigFormatException
	// Load up the config for the com.shanebeestudios.clue.board, and create the cells
	public void loadBoardConfig() throws BadConfigFormatException {
		Scanner csvFile = null;
		try {
			// Attempt to create the scanner on the csv file
			csvFile = new Scanner(new File(csvFilepath));
		} catch (FileNotFoundException e) {
			// If something goes wrong, throw an exception
			throw new BadConfigFormatException("csv Filepath was invalid");
		}
		String csvLine;
		String[] csvSplit;

		while(csvFile.hasNextLine()) {
			csvLine = csvFile.nextLine();
			csvSplit = csvLine.split(",");
			++numRows;
			for(int i = 0; i < csvSplit.length; ++i) {

				// If the classifier is a w, make a new walkway cell
				if(csvSplit[i].charAt(0) == 'W') { 
					cells.add(new WalkwayCell());
				} 
				// Otherwise, it must be a room cell, or an invalid cell
				else {
					// If we can't find this cell type in the legend, throw an exception
					if(!rooms.containsKey(csvSplit[i].charAt(0))) throw new BadConfigFormatException("Unrecognized room detected" + csvSplit[i].charAt(0));
					cells.add(new RoomCell(csvSplit[i]));
				}
			}
		}

		// Calculate the number of columns from the rows and amount of cells
		numColumns = cells.size() / numRows;

		// If there was an error, i.e. the com.shanebeestudios.clue.board is not square, throw an exception
		if(numColumns*numRows != cells.size()) {
			throw new BadConfigFormatException("Columns bad");
		}
		csvFile.close();
		addCellAttributes();
		visited = new boolean[numRows * numColumns];
	}

	private void addCellAttributes() {
		//System.out.println(cells.size());
		for(int i = 0; i < cells.size(); i++) {
			cells.get(i).setRow(i / numColumns);
			cells.get(i).setColumn(i % numColumns);
		}
	}
	// Calculates the appropriate index on a 1D array given a row and column 
	public int calcIndex(int row, int col) {
		// Outlier / Bad Input Cases
		if (row >= numRows) return -1;
		else if (col >= numColumns) return -1;
		else if (col < 0) return -1;
		else if (row < 0) return -1;

		// If the input is valid, then calculate the index
		else {
			return row * numColumns + col;
		}
	}

	// Calculates the adjacencies for every cell on the com.shanebeestudios.clue.board, stores them into the adjacency list
	public void calcAdjacencies() {
		LinkedList<Integer> adjacency;

		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				adjacency = new LinkedList<Integer>();
				if(cells.get(calcIndex(i,j)).isDoorway()) {
					RoomCell thisCell = (RoomCell) cells.get(calcIndex(i,j));
					adjacency.add(calcIndex(i + thisCell.getDoorDirection().getX(), j + thisCell.getDoorDirection().getY()));
				} 
				else if(!cells.get(calcIndex(i,j)).isRoom()) {
					// Add the appropriate adjacency based upon the adjacency list function
					if(adjacencyLogic(i,j,i,j+1))
						adjacency.add(calcIndex(i,j+1));
					if(adjacencyLogic(i,j,i,j-1))
						adjacency.add(calcIndex(i,j-1));
					if(adjacencyLogic(i,j,i+1,j))
						adjacency.add(calcIndex(i+1,j));
					if(adjacencyLogic(i,j,i-1,j))
						adjacency.add(calcIndex(i-1,j));
				}
				adjacencyLists.put(calcIndex(i, j), adjacency);
			}
		}
	}

	// This function verifies that the adjacency is correct for the given cell, (i0,j0)
	private boolean adjacencyLogic(int i0, int j0, int i1, int j1) {
		// If calcIndex detects an issue, it is not correct. return false.
		if(calcIndex(i1,j1) != -1) {
			if(cells.get(calcIndex(i1,j1)).isDoorway()) {
				// Make a room cell from the doorway cell we're checking
				RoomCell thisRoom = (RoomCell) cells.get(calcIndex(i1,j1));
				// If the you remove the differences in distance between the cells and now the x's and y's are equal, return true 
				if(i1 + thisRoom.getDoorDirection().getX() == i0 && j1 + thisRoom.getDoorDirection().getY() == j0) return true;
				return false;
			}
			else {
				return !cells.get(calcIndex(i1,j1)).isRoom();
			}
		}
		return false;
	}

	// Start targets uses calcTargets to calculate the correct targets for moving in the game
	public void startTargets(int location, int steps) {
		targets = new HashSet<BoardCell>();
		visited[location] = true;
		calcTargets(location, steps);
		visited[location] = false;
	}

	// Does the heavy lifting for startTargets, populates the targets list for a current location given a number of steps
	private void calcTargets(int location, int steps) {

		LinkedList<Integer> adjacentCells = new LinkedList<Integer>();

		for(int adjCell : adjacencyLists.get(location)) {
			if(visited[adjCell] == false) {
				adjacentCells.add(adjCell);
			}
		}

		for(int adjCell : adjacentCells) {
			visited[adjCell] = true;
			BoardCell thisCell = cells.get(adjCell);
			if(steps == 1 || thisCell.isDoorway()) {
				targets.add(thisCell);
			} 
			else {
				calcTargets(adjCell, steps - 1);
			}
			visited[adjCell] = false;
		}
	}


	public RoomCell getRoomCellAt(int row, int column) {
		if(cells.get(calcIndex(row, column)).isRoom()) {
			return (RoomCell) cells.get(calcIndex(row, column));
		} else {
			return null;
		}
	}

	public BoardCell getCellAt(int row, int column) {
		return cells.get(calcIndex(row, column));
	}

	//
	// Getters for all standard instance variables
	//

	public Set<BoardCell> getTargets() {
		return targets;

	}

	public LinkedList<Integer> getAdjList(int location) {
		return adjacencyLists.get(location);
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public int getDieRoll() {
		return dieRoll;
	}

	public void setDieRoll(int dieRoll) {
		this.dieRoll = dieRoll;
	}

	public int getPixelModifier() {
		return pixelModifier;
	}

	public void setPixelModifier(int pixelModifier) {
		this.pixelModifier = pixelModifier;
	}
	
}
