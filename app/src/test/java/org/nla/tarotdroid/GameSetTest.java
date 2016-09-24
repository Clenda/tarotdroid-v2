package org.nla.tarotdroid;

import org.junit.Test;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot5Game;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class GameSetTest {

    private static final PlayerList players = new PlayerList();
    private static final StandardTarot5Game game1 = new StandardTarot5Game();
    private static final StandardTarot5Game game2 = new StandardTarot5Game();
    private static final BelgianTarot5Game game3 = new BelgianTarot5Game();
    private static final StandardTarot5Game game4 = new StandardTarot5Game();

    static {
        Player pm = new Player();
        pm.setName("PM");
        Player guillaume = new Player();
        guillaume.setName("Guillaume");
        Player arthur = new Player();
        arthur.setName("Arthur");
        Player ludo = new Player();
        ludo.setName("Ludo");
        Player nico = new Player();
        nico.setName("Nico");

        players.add(pm);
        players.add(ludo);
        players.add(arthur);
        players.add(guillaume);
        players.add(nico);
    }

    static {
        game1.setPlayers(players);
        game1.setBet(Bet.GARDE);
        game1.setLeadingPlayer(players.get(1));
        game1.setCalledPlayer(players.get(2));
        game1.setNumberOfOudlers(2);
    }

    static {
        game2.setPlayers(players);
        game2.setBet(Bet.GARDE);
        game2.setLeadingPlayer(players.get(1));
        game2.setCalledPlayer(players.get(2));
        game2.setNumberOfOudlers(2);
    }

    static {
        game3.setPlayers(players);
        game3.setFirst(players.get(1));
        game3.setSecond(players.get(2));
        game3.setThird(players.get(3));
        game3.setFourth(players.get(4));
        game3.setFifth(players.get(5));
    }

    static {
        game4.setPlayers(players);
        game4.setBet(Bet.GARDE);
        game4.setLeadingPlayer(players.get(1));
        game4.setCalledPlayer(players.get(2));
        game4.setNumberOfOudlers(2);
    }


    @Test
    public final void testGameSet() {
        GameSet gameSet = new GameSet();
        assertNotNull("new GameSet() returns null", gameSet);
    }


    @Test
    public final void testGetGameCount() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        assertEquals("GameSet.getGameCount() should return zero right after GameSet creation",
                     0,
                     gameSet.getGameCount());
        gameSet.addGame(game1);
        assertEquals(
                "GameSet.getGameCount() should return zero right after 1 call to GameSet.addGame()",
                1,
                gameSet.getGameCount());
    }


    @Test
    public final void testAddGame() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        gameSet.addGame(game1);
        assertEquals("GameSet.addGame() doesn't properly add 1 game", 1, gameSet.getGameCount());
        gameSet.addGame(game2);
        gameSet.addGame(game3);
        gameSet.addGame(game4);
        assertEquals("GameSet.addGame() doesn't properly add several games",
                     4,
                     gameSet.getGameCount());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testAddNullGame() {
        GameSet gameSet = new GameSet();
        gameSet.addGame(null);
    }


    @Test
    public final void testSetPlayers() {
        GameSet gameSet = new GameSet();
        gameSet.setPlayers(players);
        assertNotNull("PlayerList hasn't been properly set", gameSet.getPlayers());
        assertEquals(
                "Initial player list should be the same as the one returned by gameSet.getPlayers()",
                players,
                gameSet.getPlayers());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetNullPlayers() {
        GameSet gameSet = new GameSet();
        gameSet.setPlayers(null);
    }


    @Test
    public final void testGetPlayers() {
        GameSet gameSet = new GameSet();
        gameSet.setPlayers(players);
        assertEquals("PlayerList returned by gameSet.getPlayers() is of incorrect size",
                     5,
                     gameSet.getPlayers().size());
        assertEquals("1st player in the list returned by gameSet.getPlayers() is incorrect",
                     "PM",
                     gameSet.getPlayers().get(1).getName());
        assertEquals("2nd player in the list returned by gameSet.getPlayers() is incorrect",
                     "Ludo",
                     gameSet.getPlayers().get(2).getName());
        assertEquals("3rd player in the list returned by gameSet.getPlayers() is incorrect",
                     "Arthur",
                     gameSet.getPlayers().get(3).getName());
    }


    @Test
    public final void testGetGameSetScores() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        gameSet.addGame(game1);
        gameSet.addGame(game2);
        gameSet.addGame(game3);
        gameSet.addGame(game4);
        assertNotNull(
                "GameSet.getGameSetScores() should not return null after several succesful calls to GameSet.addGame()",
                gameSet.getGameSetScores());
    }


    @Test
    public final void testGetGameOfIndex() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        gameSet.addGame(game1);
        gameSet.addGame(game2);
        gameSet.addGame(game3);
        gameSet.addGame(game4);

        assertEquals("1st game returned by GameSet.getGameOfIndex() is incorrect",
                     game1,
                     gameSet.getGameOfIndex(1));
        assertEquals("2nd game returned by GameSet.getGameOfIndex() is incorrect",
                     game2,
                     gameSet.getGameOfIndex(2));
        assertEquals("3rd game returned by GameSet.getGameOfIndex() is incorrect",
                     game3,
                     gameSet.getGameOfIndex(3));
        assertEquals("4th game returned by GameSet.getGameOfIndex() is incorrect",
                     game4,
                     gameSet.getGameOfIndex(4));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testGetGameOfIndexWithIndexUpperThanUpperBound() {
        GameSet gameSet = new GameSet();
        gameSet.addGame(game1);
        gameSet.addGame(game2);
        gameSet.getGameOfIndex(3);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testGetGameOfIndexWithIndexLowerThanLowerBound() {
        GameSet gameSet = new GameSet();
        gameSet.addGame(game1);
        gameSet.addGame(game2);
        gameSet.getGameOfIndex(0);
    }


    @Test
    public final void testGetLastGame() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        gameSet.addGame(game1);
        assertEquals(
                "Game returned by GameSet.getLastGame() after 1 call to GameSet.addGame() is incorrect",
                game1,
                gameSet.getLastGame());
        gameSet.addGame(game2);
        gameSet.addGame(game3);
        gameSet.addGame(game4);
        assertEquals(
                "Game returned by GameSet.getLastGame() after 4 calls to GameSet.addGame() is incorrect",
                game4,
                gameSet.getLastGame());
    }


    @Test
    public final void testRemoveLastGame() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        gameSet.removeLastGame();
        assertEquals(
                "Call to GameSet.removeLastGame() after GameSet creation causes incorrect behaviour on the GameSet",
                0,
                gameSet.getGameCount());
        gameSet.addGame(game1);
        gameSet.removeLastGame();
        assertEquals("Game count should be zero after 1 insertion and 1 removal",
                     0,
                     gameSet.getGameCount());
        gameSet.addGame(game1);
        gameSet.addGame(game2);
        gameSet.addGame(game3);
        gameSet.addGame(game4);
        gameSet.removeLastGame();
        assertEquals("Game count should be 3 after 4 insertions and 1 removal",
                     3,
                     gameSet.getGameCount());
        assertEquals(
                "Game returned by GameSet.getLastGame() after after 4 insertions and 1 removal should be 3rd game",
                game3,
                gameSet.getLastGame());
    }


    @Test
    public final void testIterator() {
        GameSet gameSet = new GameSet();
        gameSet.setGameSetParameters(new GameSetParameters());
        gameSet.addGame(game1);
        gameSet.addGame(game2);
        gameSet.addGame(game3);
        gameSet.addGame(game4);

        ArrayList<BaseGame> listForTest = new ArrayList<BaseGame>();
        Iterator<BaseGame> iterator = gameSet.iterator();
        while (iterator.hasNext()) {
            listForTest.add(iterator.next());
        }

        assertEquals("1st game returned by iterator is incorrect", game1, listForTest.get(0));
        assertEquals("2nd game returned by iterator is incorrect", game2, listForTest.get(1));
        assertEquals("3rd game returned by iterator is incorrect", game3, listForTest.get(2));
        assertEquals("4th game returned by iterator is incorrect", game4, listForTest.get(3));
    }


    @Test
    public final void testSetGameSetParameters() {
        GameSet gameSet = new GameSet();
        GameSetParameters gameSetParameters = new GameSetParameters();
        gameSet.setGameSetParameters(gameSetParameters);
        GameSetParameters returnedGameSetParameters = gameSet.getGameSetParameters();
        assertEquals(
                "GameSet.getGameSetParameters() should return the same GameSetParameters object as the one set by GameSet.setGameSetParameters()",
                gameSetParameters,
                returnedGameSetParameters);
    }
}
