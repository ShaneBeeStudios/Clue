package com.shanebeestudios.clue;

import com.apple.eawt.Application;
import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.game.card.CardType;
import com.shanebeestudios.clue.game.Character;
import com.shanebeestudios.clue.game.card.Deck;
import com.shanebeestudios.clue.gui.Icon;
import com.shanebeestudios.clue.game.Room;
import com.shanebeestudios.clue.game.Weapon;
import com.shanebeestudios.clue.gui.DetectiveNotes;
import com.shanebeestudios.clue.game.card.Card;
import com.shanebeestudios.clue.gui.CardPanel;
import com.shanebeestudios.clue.gui.ControlPanel;
import com.shanebeestudios.clue.game.card.Solution;
import com.shanebeestudios.clue.player.ComputerPlayer;
import com.shanebeestudios.clue.player.HumanPlayer;
import com.shanebeestudios.clue.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ClueGame extends JFrame {

    private Deck deck;
    private Deck closetCards;
    private List<ComputerPlayer> cpuPlayers;
    private HumanPlayer humanPlayer;
    private Player whosTurn;
    private final Board board;
    private final String layout;
    private final JMenuBar menubar;
    private final DetectiveNotes notes;
    private static ClueGame game;
    private List<Player> allPlayers;
    private final ControlPanel controlPanel;


    public ClueGame(String layout) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Clue!");
        setSize(760, 850);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);

        menubar = new JMenuBar();
        setJMenuBar(menubar);
        this.layout = layout;
        deck = new Deck();
        closetCards = new Deck();
        cpuPlayers = new ArrayList<>();
        allPlayers = new ArrayList<>();
        humanPlayer = new HumanPlayer();
        board = new Board(layout, this);
        notes = new DetectiveNotes();
        controlPanel = new ControlPanel(this);
        loadConfigFiles();
        whosTurn = humanPlayer;
    }

    public boolean isHumanMustFinish() {
        return board.isHumanMustFinish();
    }

    public void setHumanMustFinish(boolean humanMustFinish) {
        board.setHumanMustFinish(humanMustFinish);
    }

    public Board getBoard() {
        return board;
    }

    public void loadConfigFiles() {
        loadPeople();
        loadDeck();
        deal();
        loadMenuBar();
        board.setPlayers(allPlayers);
        add(board, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(new CardPanel(this), BorderLayout.EAST);
    }

    private void loadMenuBar() {
        JMenu file = new JMenu("File");
        file.add(createDetectiveNotesItem());
        file.add(createFileExitItem());
        menubar.add(file);
    }

    private JMenuItem createFileExitItem() {
        JMenuItem item = new JMenuItem("Exit");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }
        item.addActionListener(new MenuItemListener());
        return item;
    }

    private JMenuItem createDetectiveNotesItem() {
        JMenuItem item = new JMenuItem("Detective Notes");
        class NotesListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                notes.setVisible(true);
            }
        }
        item.addActionListener(new NotesListener());
        return item;
    }

    public void loadPeople() {
        cpuPlayers = new ArrayList<>();

        // Create a mutable list of all characters
        List<Character> characters = new ArrayList<>();
        Collections.addAll(characters, Character.values());

        // Pick a random character for the human player
        int r = new Random().nextInt(6);
        Character random = Character.values()[r];
        humanPlayer = new HumanPlayer(random.getName(), random.getColor(), random.getXPos(), random.getYPos());
        characters.remove(random);

        // Load the rest of the characters as cpu players
        for (Character character : characters) {
            cpuPlayers.add(new ComputerPlayer(character.getName(), character.getColor(), character.getXPos(), character.getYPos()));
        }
        // Add everyone to the list of all players
        allPlayers.add(humanPlayer);
        allPlayers.addAll(cpuPlayers);
    }

    public void loadDeck() {
        deck = new Deck();
        for (Character character : Character.values()) {
            deck.add(new Card(character.getName(), CardType.PERSON));
        }
        for (Room room : Room.values()) {
            if (room != Room.CLOSET && room != Room.WALKWAY && room != Room.OUTSIDE) {
                deck.add(new Card(room.getName(), CardType.ROOM));
            }
        }
        for (Weapon weapon : Weapon.values()) {
            deck.add(new Card(weapon.getName(), CardType.WEAPON));
        }
    }

    public void deal() {
        int i = 0;
        humanPlayer.resetCards();
        for (Player a : cpuPlayers)
            a.resetCards();
        boolean weaponInSolution = false;
        boolean personInSolution = false;
        boolean roomInSolution = false;
        Collections.shuffle(deck);
        for (Card a : deck) {
            CardType theType = a.getCardType();
            if ((!weaponInSolution) && (theType == CardType.WEAPON)) {
                closetCards.add(a);
                weaponInSolution = true;
            } else if ((!personInSolution) && (theType == CardType.PERSON)) {
                closetCards.add(a);
                personInSolution = true;
            } else if ((!roomInSolution) && (theType == CardType.ROOM)) {
                closetCards.add(a);
                roomInSolution = true;
            } else if (i == 0) {
                humanPlayer.giveCard(a);
                i++;
            } else if (i > 0) {
                cpuPlayers.get(i - 1).giveCard(a);
                cpuPlayers.get(i - 1).getKnownCards().add(a);
                i++;
            }
            if (i > cpuPlayers.size())
                i = 0;
        }
    }

    public boolean checkAccusation(Solution solution) {
        String person = null;
        String weapon = null;
        String room = null;
        for (Card a : closetCards) {
            if (a.getCardType() == CardType.PERSON)
                person = a.getName();
            else if (a.getCardType() == CardType.WEAPON)
                weapon = a.getName();
            else if (a.getCardType() == CardType.ROOM)
                room = a.getName();
        }
        return solution.checkSolution(person, weapon, room);
    }

    public Card handleSuggestion(String thePerson, String theRoom, String theWeapon, Player thePlayer) {
        //lame hack forLoop dave
        for (Player x : allPlayers) {
            if (x.getName().equals(thePerson)) {
                x.setRow(thePlayer.getRow());
                x.setColumn(thePlayer.getColumn());
            }
        }


        ArrayList<Player> thesePlayers = new ArrayList<>();
        ArrayList<String> theseStrings = new ArrayList<>();
        ArrayList<Card> theseCards = new ArrayList<>();
        Card answer = null;
        theseStrings.add(theRoom);
        theseStrings.add(theWeapon);
        theseStrings.add(thePerson);
        thesePlayers.add(humanPlayer);
        thesePlayers.addAll(cpuPlayers);
        thesePlayers.remove(thePlayer);
        for (Player a : thesePlayers) {
            theseCards.addAll(a.getCards());
        }
        Collections.shuffle(theseCards);
        for (Card a : theseCards) {
            if (theseStrings.contains(a.getName())) {
                answer = a;
                break;
            }
        }
        return answer;
    }

    public int getDeckWeaponSize() {
        return getRoomSizeCardType(CardType.WEAPON);
    }

    public int getDeckPlayerSize() {
        return getRoomSizeCardType(CardType.PERSON);
    }

    public int getDeckRoomSize() {
        return getRoomSizeCardType(CardType.ROOM);
    }

    private int getRoomSizeCardType(CardType cardType) {
        int rooms = 0;
        for (Card a : deck) {
            if (a.getCardType() == cardType)
                rooms++;
        }
        return rooms;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck.clear();
        this.deck = deck;
    }

    public Deck getClosetCards() {
        return closetCards;
    }

    public void setClosetCards(Deck closetCards) {
        this.closetCards = closetCards;
    }

    public List<ComputerPlayer> getCpuPlayers() {
        return cpuPlayers;
    }

    public void setCpuPlayers(ArrayList<ComputerPlayer> cpuPlayers) {
        this.cpuPlayers = cpuPlayers;
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public void setHumanPlayer(HumanPlayer bob) {
        humanPlayer = bob;
    }

    public Player getWhosTurn() {
        return whosTurn;
    }

    public void setWhosTurn(Player whosTurn) {
        this.whosTurn = whosTurn;
    }

    public void addPlayer(ComputerPlayer player) {
        cpuPlayers.add(player);
    }

    public void addPlayer(HumanPlayer player) {
        humanPlayer = player;
    }

    public void resetPlayers() {
        humanPlayer = null;
        cpuPlayers = new ArrayList<>();
    }

    public void startHumanTurn() {
        humanPlayer.setCanMakeAccusation(true);
        controlPanel.getDietext().setText(rollDie());
        controlPanel.getWhoseturn().setText(humanPlayer.getName());
        board.setHumanMustFinish(true);
        humanPlayer.makeMove(board);
    }

    public void startComputerTurn(ComputerPlayer cpu) {
        controlPanel.getWhoseturn().setText(whosTurn.getName());
        int roll = Integer.parseInt(rollDie());
        controlPanel.getDietext().setText(String.valueOf(roll));
        cpu.makeMove(board, roll, this);
    }


    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(ArrayList<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }


    public String rollDie() {
        Random generator = new Random();
        int x = (Math.abs(generator.nextInt()) % 6) + 1;
        board.setDieRoll(x);
        return Integer.toString(x);
    }

    public static void main(String[] args) {
        try {
            Application application = Application.getApplication();
            application.setDockIconImage(Icon.CLUE_DOCK.getImage());
            application.setDockIconBadge("Clue"); // This doesn't seem to work :(
        } catch (Throwable ignore) {}
        game = new ClueGame("RoomLayoutNEW.csv");
        game.setVisible(true);
        JOptionPane.showMessageDialog(game, "You are " + game.getHumanPlayer().getName() + ",\nselect a highlighted cell to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE, Icon.CLUE_LOGO);
        game.startHumanTurn();
        game.humanPlayer.makeMove(game.board);
    }

}
