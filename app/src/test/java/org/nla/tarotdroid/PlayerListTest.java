package org.nla.tarotdroid;

import org.junit.Test;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class PlayerListTest {


    private static final String Player1Name = "player1";


    private static final String Player2Name = "player2";


    private static final String Player3Name = "player3";


    private static final String Player4Name = "player4";


    private static final String Player5Name = "player5";


    private static final String Player6Name = "player6";


    private PlayerList createListOf6Players() {

        // players creation
        Player player1 = new Player();
        player1.setName(Player1Name);

        Player player2 = new Player();
        player2.setName(Player2Name);

        Player player3 = new Player();
        player3.setName(Player3Name);

        Player player4 = new Player();
        player4.setName(Player4Name);

        Player player5 = new Player();
        player5.setName(Player5Name);

        Player player6 = new Player();
        player6.setName(Player6Name);

        PlayerList players = new PlayerList();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);

        return players;
    }


    @Test
    public final void testFirstToFifthPlayerSubListInAListOfSixPlayers() {
        PlayerList fullPlayerList = this.createListOf6Players();
        PlayerList firstFivePlayers = fullPlayerList.subList(1, 5);

        assertEquals(firstFivePlayers.get(1).getName(), Player1Name);
        assertEquals(firstFivePlayers.get(2).getName(), Player2Name);
        assertEquals(firstFivePlayers.get(3).getName(), Player3Name);
        assertEquals(firstFivePlayers.get(4).getName(), Player4Name);
        assertEquals(firstFivePlayers.get(5).getName(), Player5Name);
    }


    @Test
    public final void testSecondToSixthPlayerSubListInAListOfSixPlayers() {
        PlayerList fullPlayerList = this.createListOf6Players();
        PlayerList secondToSixthPlayers = fullPlayerList.subList(2, 6);

        assertEquals(secondToSixthPlayers.get(1).getName(), Player2Name);
        assertEquals(secondToSixthPlayers.get(2).getName(), Player3Name);
        assertEquals(secondToSixthPlayers.get(3).getName(), Player4Name);
        assertEquals(secondToSixthPlayers.get(4).getName(), Player5Name);
        assertEquals(secondToSixthPlayers.get(5).getName(), Player6Name);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCorrectSizeOfASublist() {
        PlayerList fullPlayerList = this.createListOf6Players();
        PlayerList firstFivePlayers = fullPlayerList.subList(1, 5);
        firstFivePlayers.get(6);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testLowerBoundOfSublist() {
        PlayerList fullPlayerList = this.createListOf6Players();
        fullPlayerList.subList(0, 5);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testUpperBoundOfSublist() {
        PlayerList fullPlayerList = this.createListOf6Players();
        fullPlayerList.subList(1, 8);
    }


    @Test
    public final void testPlayerList() {
        PlayerList players = new PlayerList();
        assertNotNull("new PlayerList() should not return null object", players);
    }


    @Test
    public final void testGetPlayers() {
        PlayerList players = createListOf6Players();
        List<Player> playersAsList = players.getPlayers();
        assertNotNull("PlayerList() should not return null object", playersAsList);
        assertEquals("PlayerList().size() should be equal to PlayerList().getPlayers().size()",
                     players.size(),
                     players.getPlayers().size());
    }


    @Test
    public final void testSetPlayerName() {
        PlayerList players = createListOf6Players();
        String newPlayer1Name = "modified player name";
        players.setPlayerName(1, newPlayer1Name);
        assertEquals("The name of the first player should have been modified",
                     newPlayer1Name,
                     players.get(1).getName());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetPlayerNameWithIndexLowerThanLowerBound() {
        PlayerList players = createListOf6Players();
        players.setPlayerName(0, "an exception is gonna be thrown...");
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetPlayerNameWithIndexHigherThanUpperBound() {
        PlayerList players = createListOf6Players();
        players.setPlayerName(7, "an exception is gonna be thrown...");
    }


    @Test
    public final void testAddString() {
        PlayerList players = new PlayerList();
        players.add(Player1Name);
        assertNotNull(
                "A call to PlayerList.get() after a call to PlayerList.add() should not return a null object",
                players.get(1));
        assertEquals("After a call to PlayerList.add() PlayerList.size() should return 1",
                     1,
                     players.size());
        assertEquals(
                "The name of the added player should be same as the name of the returned player",
                Player1Name,
                players.get(1).getName());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testAddNullString() {
        PlayerList players = new PlayerList();
        String nullString = null;
        players.add(nullString);
    }


    @Test
    public final void testAddPlayer() {
        PlayerList players = new PlayerList();
        Player player1 = new Player();
        player1.setName(Player1Name);
        players.add(player1);
        assertNotNull(
                "A call to PlayerList.get() after a call to PlayerList.add() should not return a null object",
                players.get(1));
        assertEquals("After a call to PlayerList.add() PlayerList.size() should return 1",
                     1,
                     players.size());
        assertEquals("The added player should be same as the returned player",
                     player1,
                     players.get(1));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testAddNullPlayer() {
        PlayerList players = new PlayerList();
        Player nullPlayer = null;
        players.add(nullPlayer);
    }


    @Test
    public final void testRemoveString() {
        PlayerList players = createListOf6Players();
        Player toBeRemoved = players.get(1);
        players.remove(toBeRemoved.getName());
        assertEquals("After a call to PlayerList.remove() PlayerList.size() should return 5",
                     5,
                     players.size());
        assertTrue("Player1 should no longer be in the PlayerList", !players.contains(toBeRemoved));
    }


    @Test
    public final void testRemovePlayer() {
        PlayerList players = createListOf6Players();
        Player toBeRemoved = players.get(1);
        players.remove(toBeRemoved);
        assertEquals("After a call to PlayerList.remove() PlayerList.size() should return 5",
                     5,
                     players.size());
        assertTrue("Player1 should no longer be in the PlayerList", !players.contains(toBeRemoved));
    }


    @Test
    public final void testSize() {
        PlayerList players = createListOf6Players();
        assertEquals("After a call to PlayerList.remove() PlayerList.size() should return 6",
                     6,
                     players.size());
    }


    @Test
    public final void testContains() {
        PlayerList players = createListOf6Players();
        Player toTest = players.get(1);
        assertTrue("Player1 should no longer be in the PlayerList", players.contains(toTest));
    }


    @Test
    public final void testGet() {
        PlayerList players = new PlayerList();
        Player player2 = new Player();
        players.add(Player1Name);
        players.add(player2);
        assertEquals("Player.get() should return Player1", Player1Name, players.get(1).getName());
        assertEquals("Player.get(2) should be equal to the added player object",
                     player2,
                     players.get(2));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testGetWithIndexLowerThanLowerBound() {
        PlayerList players = createListOf6Players();
        players.get(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testGetWithIndexHigherThanUpperBound() {
        PlayerList players = createListOf6Players();
        players.get(8);
    }


    @Test
    public final void testIterator() {
        PlayerList players = createListOf6Players();
        int i = 0;
        for (Player player : players) {
            ++i;
            assertNotNull("No iterated player should be null", player);
        }
        assertEquals("Loop should have iterated 6 times", 6, i);
    }


    @Test
    public final void testGetNextPlayer() {
        PlayerList players = createListOf6Players();
        Player player1 = players.get(1);
        Player player2 = players.get(2);
        Player player3 = players.get(3);
        Player player4 = players.get(4);
        Player player5 = players.get(5);
        Player player6 = players.get(6);

        assertEquals("Player after " + Player1Name + " should be " + Player2Name,
                     Player2Name,
                     players.getNextPlayer(player1).getName());
        assertEquals("Player after " + Player2Name + " should be " + Player3Name,
                     Player3Name,
                     players.getNextPlayer(player2).getName());
        assertEquals("Player after " + Player3Name + " should be " + Player4Name,
                     Player4Name,
                     players.getNextPlayer(player3).getName());
        assertEquals("Player after " + Player4Name + " should be " + Player5Name,
                     Player5Name,
                     players.getNextPlayer(player4).getName());
        assertEquals("Player after " + Player5Name + " should be " + Player6Name,
                     Player6Name,
                     players.getNextPlayer(player5).getName());
        assertEquals("Player after " + Player6Name + " should be " + Player1Name,
                     Player1Name,
                     players.getNextPlayer(player6).getName());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testGetNextPlayerWithNullPlayer() {
        PlayerList players = createListOf6Players();
        players.getNextPlayer(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testGetNextPlayerWithPlayerNotInList() {
        PlayerList players = createListOf6Players();
        players.getNextPlayer(new Player());
    }

}

