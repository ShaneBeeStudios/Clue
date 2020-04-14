/*
 * Class: Board
 * Authors: Brandon Rodriguez, Hunter Lang
 * This class is the main class pertaining to the clue com.shanebeestudios.clue.board.
 * Also contains logic to calculate targets and adjacencies
 * Relies on BadConfigFormatException, BoardCell, RoomCell, and WalkwayCell
 */

package com.shanebeestudios.clue.board;

import com.shanebeestudios.clue.ClueGame;
import com.shanebeestudios.clue.board.cell.BoardCell;
import com.shanebeestudios.clue.board.cell.OutsideCell;
import com.shanebeestudios.clue.board.cell.RoomCell;
import com.shanebeestudios.clue.board.cell.WalkwayCell;
import com.shanebeestudios.clue.game.Icon;
import com.shanebeestudios.clue.game.Rooms;
import com.shanebeestudios.clue.misc.SuggestDialog;
import com.shanebeestudios.clue.misc.SuggestDialog.SuggestType;
import com.shanebeestudios.clue.player.HumanPlayer;
import com.shanebeestudios.clue.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// Board class body

@SuppressWarnings("unused")
public class Board extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	private List<Player> players;
	private ArrayList<BoardCell> cells;
	private Map<Character,String> rooms;
	private int numRows;
	private int numColumns;
    private int dieRoll;
	private int pixelModifier;

	// Filepaths for the configuration files
	private String csvFilepath;

	// Array used to keep track of visited cells when the fucntion calcAdjacencies is called
	private boolean[] visited;
	private Map<Integer, LinkedList<Integer>> adjacencyLists;
	private Set<BoardCell> targets;
	private Player humanPlayer;
	private boolean humanMustFinish;
	private ClueGame game;

	// Default constructor for com.shanebeestudios.clue.board. Simply initializes the values, nothing else
	public Board() {
		initialize();
	}
	
	public Board(String csv, ClueGame game) {
		this(csv);
		this.game = game;
	}

	// Parameterized constructor, sets all the fields of com.shanebeestudios.clue.board using the configuration files
	public Board(String csv) {
		addMouseListener(this);
		setSize(700,700);
		initialize();
		csvFilepath = csv;

		humanMustFinish = true;
		adjacencyLists = new HashMap<>();
		targets = new HashSet<>();

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
			JOptionPane.showMessageDialog(this, "Please select a highlighted cell", "Invalid Move", JOptionPane.INFORMATION_MESSAGE, Icon.CLUE_LOGO);
	}
	
	public boolean isHumanMustFinish() {
		return humanMustFinish;
	}

	public void setHumanMustFinish(boolean humanMustFinish) {
		this.humanMustFinish = humanMustFinish;
	}

	public void unHighlightTargets() {
		for(BoardCell c : targets) {
			c.setHighlight(false);
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
			x.setHighlight(true);
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
    }
	
	// Initializes default values of cells, rooms, numRows, and numColumns
	private void initialize() {
		cells = new ArrayList<>();
		rooms = new HashMap<>();
		adjacencyLists = new HashMap<>();
		targets = new HashSet<>();
		numRows = 0;
		numColumns = 0;
	}

    public void loadConfigFiles() {
		try {
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();
		} catch(BadConfigFormatException e) { // If one of those throws an error, catch it, print it to the screen
			e.printStackTrace();
		}
	}

	// One of the heavy lifter functions for the loadConfigFiles method
	// Throws: BadConfigFormatException
	// Loads up the legend file, and populates the legend in the board
	public void loadRoomConfig() {
		for (Rooms room : Rooms.values()) {
		    rooms.put(room.getKey(), room.getName());
        }
	}

	// Second heavy lifter function for the loadConfigFiles method
	// Throws: BadConfigFormatException
	// Load up the config for the board, and create the cells
	public void loadBoardConfig() throws BadConfigFormatException {
		Scanner csvFile;
        // Attempt to create the scanner on the csv file
        InputStream s = ClueGame.class.getClassLoader().getResourceAsStream(csvFilepath);
        assert s != null;
        csvFile = new Scanner(s);
        String csvLine;
		String[] csvSplit;

		while(csvFile.hasNextLine()) {
			csvLine = csvFile.nextLine();
			csvSplit = csvLine.split(",");
			++numRows;
            for (String value : csvSplit) {
                if (value.length() == 0) {
                    System.out.println("Line error: " + numRows);
                }

                // If the classifier is a w, make a new walkway cell
                if (value.charAt(0) == 'W') {
                    cells.add(new WalkwayCell());
                }
                // If the classifier is an underscore, make a new outside cell
                else if (value.charAt(0) == '_') {
                    cells.add(new OutsideCell());
                }
                // Otherwise, it must be a room cell, or an invalid cell
                else {
                    // If we can't find this cell type in the legend, throw an exception
                    if (!rooms.containsKey(value.charAt(0)))
                        throw new BadConfigFormatException("Unrecognized room detected" + value.charAt(0));
                    cells.add(new RoomCell(value));
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

	// Calculates the adjacencies for every cell on the board, stores them into the adjacency list
	public void calcAdjacencies() {
		LinkedList<Integer> adjacency;

		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				adjacency = new LinkedList<>();
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
                return i1 + thisRoom.getDoorDirection().getX() == i0 && j1 + thisRoom.getDoorDirection().getY() == j0;
            }
			else {
				return !cells.get(calcIndex(i1,j1)).isRoom();
			}
		}
		return false;
	}

	// Start targets uses calcTargets to calculate the correct targets for moving in the game
	public void startTargets(int location, int steps) {
		targets = new HashSet<>();
		visited[location] = true;
		calcTargets(location, steps);
		visited[location] = false;
	}

	// Does the heavy lifting for startTargets, populates the targets list for a current location given a number of steps
	private void calcTargets(int location, int steps) {

        LinkedList<Integer> adjacentCells = new LinkedList<>();

        for (int adjCell : adjacencyLists.get(location)) {
            if (!visited[adjCell]) {
                adjacentCells.add(adjCell);
            }
        }

        for (int adjCell : adjacentCells) {
            visited[adjCell] = true;
            BoardCell thisCell = cells.get(adjCell);
            if (thisCell.isOutside()) continue; // TODO this is a test for outside cells
            if (steps == 1 || thisCell.isDoorway()) {
                targets.add(thisCell);
            } else {
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

    public void setPlayers(List<Player> players) {
        this.players = players;
        humanPlayer = players.get(0);
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
