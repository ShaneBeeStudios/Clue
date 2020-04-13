package com.shanebeestudios.clue.boardTesting;

import com.shanebeestudios.clue.board.BadConfigFormatException;
import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.board.BoardCell;
import com.shanebeestudios.clue.board.RoomCell;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CRBoardInitTests {
	// I made this static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test
	private static Board board;
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 22; //was 22
	public static final int NUM_COLUMNS = 23; //was 23
	
	@BeforeClass
	public static void setUp() {
		board = new Board("ClueLayout.csv");
		//com.shanebeestudios.clue.board.loadConfigFiles();
	}
	@Test
	public void testRooms() {
		Map<Character, String> rooms = board.getRooms();
		// Ensure we read the correct number of rooms
		Assert.assertEquals(NUM_ROOMS, rooms.size());
		// Test retrieving a few from the hash, including the first
		// and last in the file and a few others
		Assert.assertEquals("Conservatory", rooms.get('C'));
		Assert.assertEquals("Ballroom", rooms.get('B'));
		Assert.assertEquals("Billiard room", rooms.get('R'));
		Assert.assertEquals("Dining room", rooms.get('D'));
		Assert.assertEquals("Walkway", rooms.get('W'));
	}
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		Assert.assertEquals(NUM_ROWS, board.getNumRows());
		Assert.assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	// Test a doorway in each direction, plus two cells that are not
	// a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		// Test one each RIGHT/LEFT/UP/DOWN
		RoomCell room = board.getRoomCellAt(4, 3);
		assertTrue(room.isDoorway());
		Assert.assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getRoomCellAt(4, 8);
		assertTrue(room.isDoorway());
		Assert.assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
		room = board.getRoomCellAt(15, 18);
		assertTrue(room.isDoorway());
		Assert.assertEquals(RoomCell.DoorDirection.LEFT, room.getDoorDirection());
		room = board.getRoomCellAt(14, 11);
		assertTrue(room.isDoorway());
		Assert.assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getRoomCellAt(14, 14);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(0, 6);
		assertFalse(cell.isDoorway());		

	}
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		int totalCells = board.getNumColumns() * board.getNumRows();
		Assert.assertEquals(506, totalCells);
		for (int i=0; i<board.getNumRows(); i++)
		{
			for(int j = 0; j < board.getNumColumns(); j++) {
			BoardCell cell = board.getCellAt(i , j);
			if (cell.isDoorway())
				numDoors++;
			}
		}
		System.out.println(numDoors);
		Assert.assertEquals(16, numDoors);
	}

	
	@Test
	public void testCalcIndex() {
		// Test each corner of the com.shanebeestudios.clue.board
 		Assert.assertEquals(NUM_COLUMNS-1, board.calcIndex(0, NUM_COLUMNS-1));
		Assert.assertEquals(483, board.calcIndex(NUM_ROWS-1, 0));
		Assert.assertEquals(505, board.calcIndex(NUM_ROWS-1, NUM_COLUMNS-1));
		// Test a couple others
		Assert.assertEquals(24, board.calcIndex(1, 1));
		Assert.assertEquals(66, board.calcIndex(2, 20));		
	}
	
	// Test a few room cells to ensure the room initial is
	// correct.
	@Test
	public void testRoomInitials() {
		Assert.assertEquals('C', board.getRoomCellAt(0, 0).getRoomClassifier());
		Assert.assertEquals('R', board.getRoomCellAt(4, 8).getRoomClassifier());
		Assert.assertEquals('B', board.getRoomCellAt(9, 0).getRoomClassifier());
		Assert.assertEquals('O', board.getRoomCellAt(21, 22).getRoomClassifier());
		Assert.assertEquals('K', board.getRoomCellAt(21, 0).getRoomClassifier());
	}
	
	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException {
		// overloaded Board ctor takes config file names
		Board b = new Board("ClueLayoutBadColumns.csv");
		// You may change these calls if needed to match your function names
		// My loadConfigFiles has a try/catch, so I can't call it directly to
		// see test throwing the BadConfigFormatException
		b.loadRoomConfig();
		b.loadBoardConfig();
	}
	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException {
		// overloaded Board ctor takes config file name
		Board b = new Board("ClueLayoutBadRoom.csv");
		b.loadRoomConfig();
		b.loadBoardConfig();
	}
	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException {
		// overloaded Board ctor takes config file name
		Board b = new Board("ClueLayout.csv");
		b.loadRoomConfig();
		b.loadBoardConfig();
	}
}


