package org.nla.tarotdroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nla.tarotdroid.biz.MapPlayersScores;
import org.nla.tarotdroid.biz.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapPlayersScoresTest {

    private Player nicol;
    private Player jk;
    private Player ludas;
    private Player guillaume;
    private Player arthur;

    @Before
    public void setUp() throws Exception {
        this.nicol = new Player("NicoL");
        this.jk = new Player("JK");
        this.arthur = new Player("Arthur");
        this.ludas = new Player("Ludas");
        this.guillaume = new Player("Guillaume");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMapPlayersScores() {
        MapPlayersScores playerScores = new MapPlayersScores();
        assertNotNull("instance of MapPlayersScores must not be null", playerScores);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithPlayerNull() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithPlayerWithNoScore() {
        MapPlayersScores playerScores = new MapPlayersScores();
        assertEquals("An exception should have been thrown...",
                     playerScores.get(this.nicol).intValue(),
                     0);
    }

    @Test
    public void testPut() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 200);
        playerScores.put(this.jk, 300);
        playerScores.put(this.ludas, 400);
        playerScores.put(this.nicol, 500);
        assertTrue("Arthur should be in player scores", playerScores.containsKey(this.arthur));
        assertTrue("Guillaume should be in player scores",
                   playerScores.containsKey(this.guillaume));
        assertTrue("JK should be in player scores", playerScores.containsKey(this.jk));
        assertTrue("Ludas should be in player scores", playerScores.containsKey(this.ludas));
        assertTrue("NicoL should be in player scores", playerScores.containsKey(this.nicol));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullPlayer() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(null, 100);
        assertTrue("An exception should have been thrown...",
                   playerScores.containsKey(this.arthur));
    }

    @Test
    public void testContainsKey() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        assertTrue("Arthur should be in player scores", playerScores.containsKey(this.arthur));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsKeyWithNullPlayer() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        assertTrue("An exception should have been thrown...", playerScores.containsKey(null));
    }

    @Test
    public void testGetPlayerRank() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 200);
        playerScores.put(this.jk, 300);
        playerScores.put(this.ludas, 400);
        playerScores.put(this.nicol, 500);
        assertEquals("NicoL should be 1st", 1, playerScores.getPlayerRank(this.nicol));
        assertEquals("Ludas should be 2nd", 2, playerScores.getPlayerRank(this.ludas));
        assertEquals("Jk should be 3rd", 3, playerScores.getPlayerRank(this.jk));
        assertEquals("Guillaume should be 4th", 4, playerScores.getPlayerRank(this.guillaume));
        assertEquals("Arthur should be 5th", 5, playerScores.getPlayerRank(this.arthur));
    }

    // TODO Fix this test...
    @Test
    public void testGetPlayerRankWithEqualRanks() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 100);
        playerScores.put(this.jk, 200);
        playerScores.put(this.ludas, 300);
        playerScores.put(this.nicol, 300);
        assertEquals("NicoL should be 1st", 1, playerScores.getPlayerRank(this.nicol));
        assertEquals("Ludas should be 1st", 1, playerScores.getPlayerRank(this.ludas));
        assertEquals("Jk should be 2nd",
                     2,
                     playerScores.getPlayerRank(this.jk));                          // <= PROBLEM
        assertEquals("Guillaume should be 3rd",
                     3,
                     playerScores.getPlayerRank(this.guillaume));            // <= PROBLEM
        assertEquals("Arthur should be 3rd",
                     3,
                     playerScores.getPlayerRank(this.arthur));                   // <= PROBLEM
    }

    @Test
    public void testGetFirstPlayer() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 200);
        playerScores.put(this.jk, 300);
        playerScores.put(this.ludas, 400);
        playerScores.put(this.nicol, 500);
        assertEquals("NicoL should be 1st", this.nicol, playerScores.getFirstPlayer());
    }

    @Test
    public void testGetFirstPlayerWithEqualRanks() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 100);
        playerScores.put(this.jk, 200);
        playerScores.put(this.ludas, 300);
        playerScores.put(this.nicol, 300);
        assertNull("No player is 1st", playerScores.getFirstPlayer());
    }

    @Test
    public void testGetMaxScore() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 200);
        playerScores.put(this.jk, 300);
        playerScores.put(this.ludas, 400);
        playerScores.put(this.nicol, 500);
        assertEquals("Incorrect max score", 500, playerScores.getMaxScore());
    }

    @Test
    public void testGetMinScore() {
        MapPlayersScores playerScores = new MapPlayersScores();
        playerScores.put(this.arthur, 100);
        playerScores.put(this.guillaume, 200);
        playerScores.put(this.jk, 300);
        playerScores.put(this.ludas, 400);
        playerScores.put(this.nicol, 500);
        assertEquals("Incorrect min score", 100, playerScores.getMinScore());
    }
}
